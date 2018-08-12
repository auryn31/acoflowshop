package acoflowshop

import aco.Ant
import kotlinx.coroutines.experimental.channels.produce
import mu.KotlinLogging
import java.util.logging.Logger

private val logger = KotlinLogging.logger {}

class ACO {
    companion object {
        /**
         * optimieren der ameisen
         */
        fun optimize(ants: MutableList<Ant>, jobList: List<Job>, storageSize: Int, evaporation: Double, iterations: Int, seedList: List<Job>): Ant {
            var pheromone: MutableList<MutableList<Double>> = ACO.initEmptyPheromonMatrix(jobList.size)
//            var pheromone: MutableList<MutableList<Double>> = ACO.initWithSeed(jobList.size, seedList, evaporation)
            var solutionNumber = 0
            val bestGlobalAnt = Ant()
            val start = System.currentTimeMillis()

            while (solutionNumber < iterations) {

                logger.info { "################### - iteration: ${solutionNumber} - ###################" }
                //linear --> ist schneller
                for (i in 0 until jobList.size) {
                    ants.forEach {
                        it.selectNextJobAndAddToJobQue(jobList, pheromone)
                    }
                }
                ants.forEach { it.calculateDuration(storageSize) }

                //parallel
//        val newAntList = mutableListOf<Ant>()
//        selectJobsAsync(ants, jobList, pheromone, numberOfJobs).consumeEach { newAntList.add(it) }
//
//        ants = newAntList

                val bestAnt = findBestAnt(ants)
                if (bestAnt != null) {
                    pheromone = updateJobPosPheromoneForAnt(bestAnt, pheromone, evaporation)
//                    println(bestAnt.jobQue)

                    bestGlobalAnt.calculateDuration(storageSize)
                    updateGlobalBestAnt(bestGlobalAnt, bestAnt, storageSize)
//                    pheromone = updatePheromoneForAnt(bestGlobalAnt, pheromone, evaporation * 0.8)
                }
                logger.info { pheromone }

                ants.forEach { it.reset() }
                solutionNumber++
                logger.info { "best ant: ${bestGlobalAnt.jobQue} with length: ${bestGlobalAnt.duration}" }
                logger.info { "TIME ${System.currentTimeMillis() - start}" }

                CsvLogging.appendCSVEntry(solutionNumber, bestGlobalAnt.duration!!, (System.currentTimeMillis() - start))
            }
            return bestGlobalAnt
        }


        fun optimizeJobJob(ants: MutableList<Ant>, jobList: List<Job>, storageSize: Int, evaporation: Double, iterations: Int, seedList: List<Job>): Ant {
            var pheromone: MutableList<MutableList<Double>> = ACO.initEmptyPheromonMatrix(jobList.size)
            var solutionNumber = 0
            val bestGlobalAnt = Ant()
            val start = System.currentTimeMillis()

            while (solutionNumber < iterations) {
                logger.info{ "Iteration: ${solutionNumber}" }

                for (i in 0 until jobList.size) {
                    ants.forEach {
                        it.selectNextJobAndAddToJobQue(jobList, pheromone)
                    }
                }
                ants.forEach { it.calculateDuration(storageSize) }

                val bestAnt = findBestAnt(ants)
                if (bestAnt != null) {
                    pheromone = updateJobJobPheromoneForAnt(bestAnt, pheromone, evaporation * 0.75)

                    bestGlobalAnt.calculateDuration(storageSize)
                    updateGlobalBestAnt(bestGlobalAnt, bestAnt, storageSize)
                    pheromone = updateJobJobPheromoneForAnt(bestGlobalAnt, pheromone, evaporation * 0.25)
                }
                logger.info { pheromone }

                ants.forEach { it.reset() }
                solutionNumber++
                CsvLogging.appendCSVEntry(solutionNumber, bestGlobalAnt.duration!!, (System.currentTimeMillis() - start))
            }
            return bestGlobalAnt
        }


        /**
         * die beste armeise der Liste suchen
         */

        fun findBestAnt(ants: List<Ant>): Ant? {
            return ants.sortedBy { it.duration }.firstOrNull()
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
            var emtpyList = initEmptyPheromonMatrix(size)
            val ant = Ant()
            ant.jobQue = seedList.toMutableList()
            for (i in 0 until 10) {
                emtpyList = updateJobPosPheromoneForAnt(ant, emtpyList, evaporation)
            }
            return emtpyList
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
                        if(j+1 != pheromonMatrix.size)
                            pheromonMatrix[i][ant.jobQue[j+1].id] += evaporatedValue
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

        private fun updateGlobalBestAnt(bestGlobalAnt: Ant, bestAnt: Ant, storageSize: Int) {
            val currentDuration = bestAnt.duration
            val globalDuration = bestGlobalAnt.duration
            if (bestGlobalAnt.jobQue.size == 0) {
                bestGlobalAnt.jobQue = bestAnt.jobQue
                bestGlobalAnt.calculateDuration(storageSize)
            } else {
                if (currentDuration != null && globalDuration != null) {
                    if (currentDuration < globalDuration) {
                        bestGlobalAnt.jobQue = bestAnt.jobQue
                    }
                }
            }
        }

        /**
         * Jobwahl asynchron
         */
        fun selectJobsAsync(ants: List<Ant>, jobList: List<Job>, storageSize: Int, pheromonMatrix: MutableList<MutableList<Double>>, numberOfJobs: Int) = produce<Ant> {
            ants.forEach {
                val ant = Ant()
                for (i in 0 until numberOfJobs) {
                    ant.selectNextJobAndAddToJobQue(jobList, pheromonMatrix)
                }
                ant.calculateDuration(storageSize)
                send(ant)
            }
        }
    }
}