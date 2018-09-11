package acoflowshop

import aco.ACO
import aco.Ant
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import global.Config
import java.util.*
import mu.KotlinLogging
import java.io.File


private const val STORAGE_SIZE = 5
private val logger = KotlinLogging.logger {}

private val jobList: List<Job> = createRandomJobList(50)


fun main(args: Array<String>) {
    val mapper = ObjectMapper().registerModule(KotlinModule())
    val config = mapper.readValue(File("src/main/resources/Config.json"), Config::class.java)
    if (config !== null) {
        calculateWithMeanCompletionTime(config)
    }
}

fun calculateWithMakespan(config: Config){
    val ants: MutableList<Ant> = (0..(config.antFactor* jobList.size).toInt()).map { i -> Ant() }.toMutableList()
    val start = System.currentTimeMillis()
    val ant1 = Ant()
    ant1.jobQue = jobList.toMutableList()
    val length = ant1.calculateDurationWithNEH(STORAGE_SIZE)
    val duration = System.currentTimeMillis() - start

    CsvLogging.createLoggingFile()
    PheromonLogger.initDB()
    val bestACO = ACO.optimize(ants, jobList, STORAGE_SIZE, config.evaporation, config.Q, ant1.jobQue)
    CsvLogging.appendCSVEntry(config.Q+1, length, duration, fak(jobList.size))

    logger.info { bestACO.jobQue }
    logger.info { getShortestSchedule(bestACO.jobQue, STORAGE_SIZE) }

//    Plotter.plotResults(getShortestSchedule(bestACO.jobQue, STORAGE_SIZE), "TEST")

    logger.warn { "NEH/ACO = ${length.toDouble() / bestACO.duration!!.toDouble()} " }
}

fun fak(num: Int): Int {
    var result = 1
    for(n in 1 until num+1) {
        result *= n
    }
    return result
}

fun calculateWithMeanCompletionTime(config: Config){
    val ants: MutableList<Ant> = (0..(config.antFactor* jobList.size).toInt()).map { i -> Ant() }.toMutableList()
    val ant1 = Ant()
    ant1.jobQue = jobList.toMutableList()

    CsvLogging.createLoggingFile()
    PheromonLogger.initDB()
    val bestACO = ACO.optimizeForMCT(ants, jobList, config.evaporation, config.Q)

    logger.info { bestACO.jobQue }
}

/**
 * Jobliste zufällig erzeugen um damit zu rechnen
 */
fun createRandomJobList(length: Int): List<Job> {

    val jobList = mutableListOf<Job>()

    for (i in 0 until length) {
        val durationM1 = Random().nextInt(30)
        val durationM2 = Random().nextInt(30)
        val setupM1 = Random().nextInt(30)
        val setupM2 = Random().nextInt(30)
        val reworkM1 = ((0.3 * Random().nextDouble() + 0.3) * durationM1).toInt()
        val reworkM2 = ((0.3 * Random().nextDouble() + 0.3) * durationM2).toInt()
        jobList.add(
                Job(
                    id = i,
                    durationMachineOne = durationM1,
                    durationMachineTwo = durationM2,
                    setupTimeMachineOne = setupM1,
                    setupTimeMachineTwo = setupM2,
                    reworktimeMachineOne = reworkM1,
                    reworktimeMachineTwo = reworkM2
                )
        )
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
    return jobList
}