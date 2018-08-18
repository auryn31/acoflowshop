package acoflowshop

import aco.Ant
import java.util.*
import kotlinx.coroutines.experimental.*
import mu.KotlinLogging

//private val c = 1.0
//private val alpha = 1.0
//private val beta = 5.0
private val evaporation = 0.05 //0.05 bei job x pos
private val Q = 200 // 1000 bei job x pos
private val antFactor = 1 //0.6 bei job x pos
private val STORAGE_SIZE = 5
private val logger = KotlinLogging.logger {}

//1001,49467,3915
//0.991342511873985
private val jobList: List<Job> = listOf(Job(279, 279, 5, 0), Job(828, 828, 2, 1), Job(539, 539, 9, 2), Job(455, 455, 1, 3), Job(144, 144, 8, 4), Job(60, 60, 8, 5), Job(156, 156, 3, 6), Job(420, 420, 9, 7), Job(180, 180, 4, 8), Job(627, 627, 6, 9), Job(618, 618, 3, 10), Job(806, 806, 8, 11), Job(267, 267, 2, 12), Job(93, 93, 1, 13), Job(230, 230, 4, 14), Job(329, 329, 4, 15), Job(252, 252, 2, 16), Job(730, 730, 0, 17), Job(129, 129, 2, 18), Job(696, 696, 9, 19), Job(964, 964, 1, 20), Job(357, 357, 7, 21), Job(29, 29, 3, 22), Job(465, 465, 6, 23), Job(584, 584, 0, 24), Job(966, 966, 2, 25), Job(556, 556, 7, 26), Job(300, 300, 9, 27), Job(483, 483, 7, 28), Job(332, 332, 2, 29), Job(661, 661, 7, 30), Job(971, 971, 3, 31), Job(577, 577, 2, 32), Job(838, 838, 7, 33), Job(816, 816, 8, 34), Job(627, 627, 0, 35), Job(468, 468, 0, 36), Job(478, 478, 3, 37), Job(185, 185, 3, 38), Job(367, 367, 6, 39), Job(925, 925, 2, 40), Job(875, 875, 0, 41), Job(766, 766, 8, 42), Job(630, 630, 1, 43), Job(762, 762, 7, 44), Job(538, 538, 4, 45), Job(84, 84, 2, 46), Job(828, 828, 3, 47), Job(607, 607, 5, 48), Job(53, 53, 4, 49), Job(732, 732, 5, 50), Job(300, 300, 5, 51), Job(823, 823, 7, 52), Job(619, 619, 9, 53), Job(724, 724, 3, 54), Job(314, 314, 3, 55), Job(883, 883, 5, 56), Job(386, 386, 1, 57), Job(384, 384, 2, 58), Job(612, 612, 8, 59), Job(321, 321, 8, 60), Job(639, 639, 2, 61), Job(818, 818, 2, 62), Job(742, 742, 1, 63), Job(76, 76, 0, 64), Job(676, 676, 8, 65), Job(268, 268, 4, 66), Job(621, 621, 7, 67), Job(910, 910, 9, 68), Job(426, 426, 9, 69), Job(227, 227, 6, 70), Job(416, 416, 2, 71), Job(822, 822, 3, 72), Job(959, 959, 9, 73), Job(311, 311, 7, 74), Job(1, 1, 8, 75), Job(55, 55, 4, 76), Job(699, 699, 2, 77), Job(260, 260, 2, 78), Job(815, 815, 1, 79), Job(611, 611, 8, 80), Job(326, 326, 9, 81), Job(400, 400, 6, 82), Job(852, 852, 2, 83), Job(56, 56, 3, 84), Job(518, 518, 2, 85), Job(788, 788, 0, 86), Job(310, 310, 0, 87), Job(220, 220, 1, 88), Job(197, 197, 2, 89), Job(367, 367, 7, 90), Job(2, 2, 2, 91), Job(612, 612, 6, 92), Job(148, 148, 5, 93), Job(121, 121, 8, 94), Job(138, 138, 9, 95), Job(343, 343, 0, 96), Job(215, 215, 2, 97), Job(907, 907, 2, 98), Job(596, 596, 2, 99)).subList(0, 30) //bis 25 bei job x pos gegen neh
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
    PheromonLogger.initDB()
    val bestACO = ACO.optimize(ants, jobList, STORAGE_SIZE, evaporation,Q, ant1.jobQue)
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
        val maschineOne = Random().nextInt(1000)
        val maschineTwo = Random().nextInt(1000)
        val storage = Random().nextInt(10)
        jobList.add(Job(maschineOne, maschineTwo, storage, i))
    }
    return jobList
}

fun createRandomJobListWithSameLength(length: Int): List<Job> {
    val jobList = mutableListOf<Job>()
    for (i in 0 until length) {
        val jobLength = Random().nextInt(1000)
        val storage = Random().nextInt(10)
        jobList.add(Job(jobLength, jobLength, storage, i))
    }
//    println(jobList)
    return jobList
}