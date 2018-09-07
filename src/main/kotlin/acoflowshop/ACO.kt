package acoflowshop

import aco.Ant
import kotlinx.coroutines.experimental.channels.produce
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class ACO {
    companion object {
        /**
         * optimieren der ameisen
         */
        fun optimize(ants: MutableList<Ant>, jobList: List<Job>, storageSize: Int, evaporation: Double, iterations: Int, seedList: List<Job>? = null): Ant {
            var pheromone: MutableList<MutableList<Double>> = ACO.initEmptyPheromonMatrix(jobList.size)
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


                val bestAnt = findBestAnt(ants)
                if (bestAnt != null) {
                    pheromone = updateJobPosPheromoneForAnt(bestAnt, pheromone, evaporation)
//                    pheromone = updateAllAnts(ants, bestAnt, pheromone, evaporation)

                    bestGlobalAnt.calculateDuration(storageSize)
                    updateGlobalBestAnt(bestGlobalAnt, bestAnt, storageSize)
//                    pheromone = updateJobPosPheromoneForAnt(bestGlobalAnt, pheromone, evaporation * 0.2)
                }
                logger.info { pheromone }

                ants.forEach { it.reset() }
                solutionNumber++
                logger.info { "best ant: ${bestGlobalAnt.jobQue} with length: ${bestGlobalAnt.duration}" }
                logger.info { "TIME ${System.currentTimeMillis() - start}" }

                CsvLogging.appendCSVEntry(solutionNumber, bestGlobalAnt.duration!!, (System.currentTimeMillis() - start))
                PheromonLogger.writeEntryIntoDB(solutionNumber, pheromone)
            }
            return bestGlobalAnt
        }

        // MTC = Mean Completion Time --> Durchschnittliche Fertigstellungszeit
        fun optimizeForMCT(ants: MutableList<Ant>, jobList: List<Job>, evaporation: Double, iterations: Int): Ant {
            var pheromone: MutableList<MutableList<Double>> = ACO.initEmptyPheromonMatrix(jobList.size)
            var solutionNumber = 0
            val bestGlobalAnt = Ant()
            val start = System.currentTimeMillis()

            while (solutionNumber < iterations) {

                logger.info { "################### - iteration: ${solutionNumber} - ###################" }
                for (i in 0 until jobList.size) {
                    ants.forEach {
                        it.selectNextJobAndAddToJobQue(jobList, pheromone)
                    }
                }
                ants.forEach { it.calculateDurationWithMCT() }
                val bestAnt = findBestAntForMCT(ants)
                if (bestAnt != null) {
                    pheromone = updateJobPosPheromoneForAnt(bestAnt, pheromone, evaporation)
                    bestGlobalAnt.calculateDurationWithMCT()
                    updateGlobalBestAntForACIS(bestGlobalAnt, bestAnt)
                }
                logger.info { pheromone }

                ants.forEach { it.reset() }
                solutionNumber++
                logger.info { "best ant: ${bestGlobalAnt.jobQue} with length: ${bestGlobalAnt.duration}" }
                logger.info { "TIME ${System.currentTimeMillis() - start}" }
                CsvLogging.appendCSVEntry(solutionNumber, bestGlobalAnt.durationForMCT!!, (System.currentTimeMillis() - start))
                PheromonLogger.writeEntryIntoDB(solutionNumber, pheromone)
            }
            return bestGlobalAnt
        }

        // update all ants
        private fun updateAllAnts(ants: MutableList<Ant>, bestAnt: Ant, pheromone: MutableList<MutableList<Double>>, evaporation: Double): MutableList<MutableList<Double>> {
            var newPheromonList = pheromone
            val bestAnts = ants.filter { it.duration == bestAnt.duration }
            bestAnts.forEach {
                newPheromonList = updateJobPosPheromoneForAnt(it, newPheromonList, evaporation)
        }
            return newPheromonList
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

        fun findBestAntForMCT(ants: List<Ant>): Ant? {
            return ants.sortedBy { it.durationForMCT }.firstOrNull()
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
            for (i in 0 until 400) {
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

        private fun updateGlobalBestAntForACIS(bestGlobalAnt: Ant, bestAnt: Ant) {
            val currentDuration = bestAnt.durationForMCT
            val globalDuration = bestGlobalAnt.durationForMCT
            if (bestGlobalAnt.jobQue.size == 0) {
                bestGlobalAnt.jobQue = bestAnt.jobQue
                bestGlobalAnt.calculateDurationWithMCT()
            } else {
                if (currentDuration != null && globalDuration != null) {
                    if (currentDuration < globalDuration) {
                        bestGlobalAnt.jobQue = bestAnt.jobQue
                    }
                }
            }
        }
    }
}