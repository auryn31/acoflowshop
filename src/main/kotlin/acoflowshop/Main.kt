package acoflowshop

import aco.Ant
import java.io.File
import java.util.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.*

private val c = 1.0
private val alpha = 1.0
private val beta = 5.0
private val evaporation = 0.005
private val Q = 500.0
private val antFactor = 0.1
private val randomFactor = 0.01
private val iterations = 1000
private val STORAGE_SIZE = 20
private val FILE_NAME = "current"

private val jobList: List<Job> = createRandomJobList(100)
//        listOf(
//        Job(1, 1, 1, 0),
//        Job(2, 2, 2, 1),
//        Job(5, 2, 2, 2),
//        Job(2, 2, 2, 3),
//        Job(9, 2, 2, 4),
//        Job(4, 1, 1, 5),
//        Job(4, 1, 1, 6),
//        Job(6, 1, 1, 7),
//        Job(4, 1, 1, 8),
//        Job(8, 1, 1, 9)
//)
private val numberOfJobs = jobList.size
private val numberOfAnts = (numberOfJobs * antFactor).toInt()
private var ants: MutableList<Ant> = (0..numberOfAnts).map { i -> Ant() }.toMutableList()
private var pheromone: MutableList<MutableList<Double>> = initEmptyPheromonMatrix(numberOfJobs)


fun main(args: Array<String>) = runBlocking<Unit> {

    createLoggingFile(FILE_NAME)

    optimize(1000)

}

/**
 * Jobliste zufällig erzeugen um damit zu rechnen
 */
fun createRandomJobList(length: Int): List<Job> {

    val jobList = mutableListOf<Job>()

    for (i in 0 until length) {
        val maschineOne = Random().nextInt(100)
        val maschineTwo = Random().nextInt(100)
        val storage = Random().nextInt(10) + 1
        jobList.add(Job(maschineOne, maschineTwo, storage, i))
    }

    return jobList
}


/**
 * löschen des Logs der letzten iteration und erstellen eines neuen Files
 */
fun createLoggingFile(name: String) {
    File("${name}.csv").delete()
    File("${name}.csv").createNewFile()
}

/**
 * schreiben der iteration in ein csv
 */
fun appendCSVEntry(fileName: String, iteration: Int, currentLength: Int, durationInMs: Long) {
    File("${fileName}.csv").appendText("${iteration},${currentLength},${durationInMs}\n")
}

/**
 * Jobwahl asynchron
 */
fun selectJobsAsync(ants: List<Ant>, jobList: List<Job>, pheromonMatrix: MutableList<MutableList<Double>>, numberOfJobs: Int) = produce<Ant> {
    ants.forEach {
        val ant = Ant()
        for (i in 0 until numberOfJobs) {
            ant.selectNextJobAndAddToJobQue(jobList, pheromonMatrix)
        }
        ant.calculateDuration(STORAGE_SIZE)
        send(ant)
    }
}

/**
 * optimieren der ameisen
 */
suspend fun optimize(iterations: Int) {
    var solutionNumber = 0
    val bestGlobalAnt = Ant()
    val start = System.currentTimeMillis()

    while (solutionNumber < iterations) {

//        println("################### - iteration: ${solutionNumber} - ###################")
        //linear --> ist schneller
        for (i in 0 until numberOfJobs) {
            ants.forEach {
                it.selectNextJobAndAddToJobQue(jobList, pheromone)
            }
        }
        ants.forEach { it.calculateDuration(STORAGE_SIZE) }

        //parallel
//        val newAntList = mutableListOf<Ant>()
//        selectJobsAsync(ants, jobList, pheromone, numberOfJobs).consumeEach { newAntList.add(it) }
//
//        ants = newAntList

        val bestAnt = findBestAnt(ants)
        if (bestAnt != null) {
            pheromone = updatePheromoneForAnt(bestAnt, pheromone, evaporation)
            println(bestAnt.jobQue)

            bestGlobalAnt.calculateDuration(STORAGE_SIZE)
            updateGlobalBestAnt(bestGlobalAnt, bestAnt)
        }
//        println(pheromone)

        ants.forEach { it.reset() }
        solutionNumber++
//        println("best ant: ${bestGlobalAnt.jobQue} with length: ${bestGlobalAnt.duration}")
//        println("TIME ${System.currentTimeMillis() - start}")
//        println()
        appendCSVEntry(FILE_NAME, solutionNumber, bestGlobalAnt.duration!!, (System.currentTimeMillis() - start))
    }
}

private fun updateGlobalBestAnt(bestGlobalAnt: Ant, bestAnt: Ant) {
    val currentDuration = bestAnt.duration
    val globalDuration = bestGlobalAnt.duration
    if (bestGlobalAnt.jobQue.size == 0) {
        bestGlobalAnt.jobQue = bestAnt.jobQue
        bestGlobalAnt.calculateDuration(STORAGE_SIZE)
    } else {
        if (currentDuration != null && globalDuration != null) {
            if (currentDuration < globalDuration) {
                bestGlobalAnt.jobQue = bestAnt.jobQue
            }
        }
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