package acoflowshop

import aco.Ant
import java.util.*
import kotlinx.coroutines.experimental.*
import mu.KotlinLogging

//private val c = 1.0
//private val alpha = 1.0
//private val beta = 5.0
private val evaporation = 0.01 //0.05 bei job x pos
private val Q = 1000 // 1000 bei job x pos
private val antFactor = 0.6 //0.6 bei job x pos
private val STORAGE_SIZE = 10
private val logger = KotlinLogging.logger {}

private val jobList: List<Job> = createRandomJobList(30) //bis 25 bei job x pos gegen neh
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



fun main(args: Array<String>) = runBlocking<Unit> {

    val start = System.currentTimeMillis()
    val ant1 = Ant()
    ant1.jobQue = jobList.toMutableList()
    val length = ant1.calculateDurationWithNEH(STORAGE_SIZE)
    val duration = System.currentTimeMillis() - start

    CsvLogging.createLoggingFile()
    val bestACO = ACO.optimizeJobJob(ants, jobList, STORAGE_SIZE, evaporation,Q, ant1.jobQue)
    CsvLogging.appendCSVEntry(Q+1, length, duration)

    logger.info { bestACO.jobQue }
    logger.info { getShortestSchedulePair(bestACO.jobQue, STORAGE_SIZE) }

//    Plotter.plotResults(getShortestSchedulePair(bestACO.jobQue, STORAGE_SIZE), "TEST")

    logger.warn { "NEH/ACO = ${length.toDouble() / bestACO.duration!!.toDouble()} " }
}

/**
 * Jobliste zufällig erzeugen um damit zu rechnen
 */
fun createRandomJobList(length: Int): List<Job> {

    val jobList = mutableListOf<Job>()

    for (i in 0 until length) {
        val maschineOne = Random().nextInt(10)
        val maschineTwo = Random().nextInt(10)
        val storage = Random().nextInt(10)
        jobList.add(Job(maschineOne, maschineTwo, storage, i))
    }

    return jobList
}