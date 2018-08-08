package acoflowshop

import aco.Ant
import java.util.*
import kotlinx.coroutines.experimental.*

private val c = 1.0
private val alpha = 1.0
private val beta = 5.0
private val evaporation = 0.005
private val Q = 500.0
private val antFactor = 0.1
private val randomFactor = 0.01
private val iterations = 1000
private val STORAGE_SIZE = 20


private val jobList: List<Job> = createRandomJobList(10)
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

    CsvLogging.createLoggingFile()
    ACO.optimize(ants, jobList, STORAGE_SIZE, evaporation,100)

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