package aco

import acoflowshop.Job
import acoflowshop.calculateDurationForMCT
import acoflowshop.findBestOrderForNextJob
import global.ACOConfig
import global.Helper
import logger_helper.CsvLogging
import logger_helper.LoggingParameter
import logger_helper.PheromonLogger
import mu.KotlinLogging
import simulation.Simulation

private val logger = KotlinLogging.logger {}

object ACO: Simulation<ACOConfig> {

    // MTC = Mean Completion Time --> Durchschnittliche Fertigstellungszeit
    override fun optimize(jobList: List<Job>, config: ACOConfig): Pair<List<Job>, Double> {
        val ants: MutableList<Ant> = (0..(config.antFactor * jobList.size).toInt()).map { Ant() }.toMutableList()
        var pheromone: MutableList<MutableList<Double>>? = null
        var eliteAnt: Ant? = null
        val start = System.currentTimeMillis()
        var solutionNumber = 0

        if (config.initMatrixWithNEH || config.withEliteSolution) {

            val nehSolution = calculateNEHSolution(jobList)
            val nehList = nehSolution.first
            val nehDuration = nehSolution.second

            if (config.withEliteSolution) {
                eliteAnt = Ant()
                eliteAnt.jobQue = nehList
                eliteAnt.setDurationForMCT(nehDuration.first, nehDuration.second)
            }

            if (config.initMatrixWithNEH) {
                pheromone = initWithSeed(jobList.size, nehList, config.evaporation)
            }
        }
        if (pheromone == null) {
            pheromone = initEmptyPheromonMatrix(jobList.size)
        }

        val bestGlobalAnt = Ant()
        val jobHashmap = Helper.createHashMapFromJobList(jobList)

        while (solutionNumber < config.maxIterations) {

            logger.info { "################### - iteration: ${solutionNumber} - ###################" }
            for (i in 0 until jobList.size) {
                ants.forEach {
                    it.selectNextJobAndAddToJobQue(jobHashmap, pheromone!!)
                }
            }
            ants.forEach { it.calculateDurationWithMCT(solutionNumber) }
            val bestAnt = findBestAntForMCT(ants, solutionNumber)
            if (bestAnt != null) {
                pheromone = updateJobPosPheromoneForAnt(bestAnt, pheromone!!, config.evaporation)
                if(config.withEliteSolution) {
                    pheromone = updateJobPosPheromoneForAnt(eliteAnt!!, pheromone, config.evaporation)
                }
                updateGlobalBestAntForACIS(bestGlobalAnt, bestAnt, solutionNumber)

                if (config.withEliteSolution && bestAnt.getDurationForMCT(solutionNumber)!! < eliteAnt!!.getDurationForMCT(solutionNumber)!!) {
                    eliteAnt.setDurationForMCT(bestAnt.getDurationForMCT(solutionNumber)!!, bestAnt.reworkPercentage!!)
                }
            }
            logger.info { pheromone }

            ants.forEach { it.reset() }
            solutionNumber++
            logger.info { "best ant: ${bestGlobalAnt.jobQue} with length: ${bestGlobalAnt.getDuationForMTCWithourRecalculation()}" }
            logger.info { "TIME ${System.currentTimeMillis() - start}" }
            LoggingParameter.iteration = solutionNumber
            LoggingParameter.bestDuration = bestGlobalAnt.getDurationForMCT(solutionNumber)!!
            LoggingParameter.currentTime = System.currentTimeMillis() - start
            LoggingParameter.reworkTimeInPercentage = bestGlobalAnt.reworkPercentage!!
//            LoggingParameter.iteration = solutionNumber
//            LoggingParameter.bestDuration = eliteAnt.getDurationForMCT(solutionNumber)!!
//            LoggingParameter.currentTime = System.currentTimeMillis() - start
//            LoggingParameter.reworkTimeInPercentage = eliteAnt.reworkPercentage!!
            CsvLogging.writeNextEntry()
            PheromonLogger.writeEntryIntoDB(solutionNumber, pheromone!!)
        }
        return Pair(bestGlobalAnt.jobQue.toList(), bestGlobalAnt.getDurationForMCT(10)!!)
    }

    private fun calculateNEHSolution(jobList: List<Job>): Pair<MutableList<Job>, Pair<Double, Double>> {
        val jobs = jobList.sortedBy { it.durationMachineOne + it.durationMachineTwo }
        var nehList = mutableListOf<Job>()
        for (job in jobs) {
            nehList = findBestOrderForNextJob(nehList, job).toMutableList()
        }
        val nehDuration = calculateDurationForMCT(nehList)
        logger.warn { "NEH duration: $nehDuration" }
        return Pair(nehList, nehDuration)
    }

    internal fun findBestAntForMCT(ants: List<Ant>, iteration: Int): Ant? {
        return ants.sortedBy { it.getDurationForMCT(iteration) }.firstOrNull()
    }

    /**
     * leere pheromonmatrix erstellen
     */
    fun initEmptyPheromonMatrix(size: Int): MutableList<MutableList<Double>> {
        val pheromonValue = 1.0 / size.toDouble()
        return (0 until size).map { (0 until size).map { pheromonValue }.toMutableList() }.toMutableList()
    }

    /**
     * start mit einer vorinitialisierten matrix die nicht überall gleich ist, sondern mit dem neh startete
     */
    fun initWithSeed(size: Int, seedList: List<Job>, evaporation: Double): MutableList<MutableList<Double>> {
        var emptyList = initEmptyPheromonMatrix(size)
        val ant = Ant()
        ant.jobQue = seedList.toMutableList()
        for (i in 0 until 400) {
            emptyList = updateJobPosPheromoneForAnt(ant, emptyList, evaporation)
        }
        return emptyList
    }

    /**
     * Job x Position
     * Verdunsten der Pheromone und hinzufügen der Gesamtmenge an verdunsteten Pheromonen zum passenden Job
     */
    fun updateJobPosPheromoneForAnt(ant: Ant, pheromonMatrix: MutableList<MutableList<Double>>, evaporation: Double): MutableList<MutableList<Double>> {
        for (i in 0 until pheromonMatrix.size) {
            var evaporatedValue = 0.0
            for (j in 0 until pheromonMatrix[i].size) {
                val evaporationValue = pheromonMatrix[i][j] * evaporation
                pheromonMatrix[i][j] -= evaporationValue
                evaporatedValue += evaporationValue
            }
            pheromonMatrix[i][ant.jobQue[i].id] += evaporatedValue
        }
        return pheromonMatrix
    }

    /**
     * Job x Job
     * Verdunsten der Pheromone und hinzufügen der Gesamtmenge an verdunsteten Pheromonen zum passenden Job
     */
    fun updateJobJobPheromoneForAnt(ant: Ant, pheromonMatrix: MutableList<MutableList<Double>>, evaporation: Double): MutableList<MutableList<Double>> {
        for (i in 0 until pheromonMatrix.size) {
            var evaporatedValue = 0.0
            for (j in 0 until pheromonMatrix[i].size) {
                val evaporationValue = pheromonMatrix[i][j] * evaporation
                pheromonMatrix[i][j] -= evaporationValue
                evaporatedValue += evaporationValue
            }
            for (j in 0 until ant.jobQue.size) {
                if (ant.jobQue[j].id == i) {
                    if (j + 1 != pheromonMatrix.size)
                        pheromonMatrix[i][ant.jobQue[j + 1].id] += evaporatedValue
                    else
                        pheromonMatrix[i][ant.jobQue[0].id] += evaporatedValue
                }
            }
        }
        return pheromonMatrix
    }

    /**
     * ist der job i nach job j
     */
    fun followJobJJobI(ant: Ant, i: Int, j: Int): Boolean {
        for (k in 0..ant.jobQue.size - 2) {
            if (ant.jobQue[k].id == i && ant.jobQue[k + 1].id == j) {
                return true
            }
        }
        return false
    }

    private fun updateGlobalBestAntForACIS(bestGlobalAnt: Ant, bestAnt: Ant, iteration: Int) {
        val currentDuration = bestAnt.getDurationForMCT(iteration)
        val globalDuration = bestGlobalAnt.getDurationForMCT(iteration)
        if (bestGlobalAnt.jobQue.size == 0) {
            bestGlobalAnt.jobQue = bestAnt.jobQue
            bestGlobalAnt.getDurationForMCT(iteration)
        } else if (currentDuration != null && globalDuration != null && currentDuration < globalDuration) {
            bestGlobalAnt.jobQue = bestAnt.jobQue
            bestGlobalAnt.setDurationForMCT(currentDuration, bestAnt.reworkPercentage!!)
        }
    }
}