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
        suspend fun optimize(ants: MutableList<Ant>, jobList: List<Job>, storageSize: Int, evaporation: Double, iterations: Int) {
            var pheromone: MutableList<MutableList<Double>> = ACO.initEmptyPheromonMatrix(jobList.size)
            var solutionNumber = 0
            val bestGlobalAnt = Ant()
            val start = System.currentTimeMillis()

            while (solutionNumber < iterations) {

        logger.info {"################### - iteration: ${solutionNumber} - ###################"}
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
                    pheromone = updatePheromoneForAnt(bestAnt, pheromone, evaporation)
//                    println(bestAnt.jobQue)

                    bestGlobalAnt.calculateDuration(storageSize)
                    updateGlobalBestAnt(bestGlobalAnt, bestAnt, storageSize)
                }
                logger.info {pheromone}

                ants.forEach { it.reset() }
                solutionNumber++
                logger.info {"best ant: ${bestGlobalAnt.jobQue} with length: ${bestGlobalAnt.duration}"}
                logger.info {"TIME ${System.currentTimeMillis() - start}"}

                CsvLogging.appendCSVEntry(solutionNumber, bestGlobalAnt.duration!!, (System.currentTimeMillis() - start))
            }
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
         * Verdunsten der Pheromone und hinzufügen der Gesamtmenge an verdunsteten Pheromonen zum passenden Job
         */
        fun updatePheromoneForAnt(ant: Ant, pheromonMatrix: MutableList<MutableList<Double>>, evaporation: Double): MutableList<MutableList<Double>> {
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