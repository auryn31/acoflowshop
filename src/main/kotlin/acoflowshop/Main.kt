package acoflowshop

import aco.ACO
import aco.Ant
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import global.Config
import imperialistic.AICA
import imperialistic.createRandomJobList
import logger_helper.CsvLogging
import logger_helper.PheromonLogger
import mu.KotlinLogging
import java.io.File


private const val STORAGE_SIZE = 5
private val logger = KotlinLogging.logger {}

private val jobList: List<Job> = createRandomJobList(20)


fun main(args: Array<String>) {
    val mapper = ObjectMapper().registerModule(KotlinModule())
    val config = mapper.readValue(File("src/main/resources/Config.json"), Config::class.java)
    if (config !== null) {
        calculateWithMeanCompletionTime(config)
    }

    AICA.optimizeForMCT(jobList, 392, 152)
}

fun calculateWithMakespan(config: Config) {
    val ants: MutableList<Ant> = (0..(config.antFactor * jobList.size).toInt()).map { i -> Ant() }.toMutableList()
    val start = System.currentTimeMillis()
    val ant1 = Ant()
    ant1.jobQue = jobList.toMutableList()
    val length = ant1.calculateDurationWithNEH(STORAGE_SIZE)
    val duration = System.currentTimeMillis() - start

    CsvLogging.createLoggingFile()
    PheromonLogger.initDB()
    val bestACO = ACO.optimize(ants, jobList, STORAGE_SIZE, config.evaporation, config.Q, ant1.jobQue)
    CsvLogging.appendCSVEntry(config.Q + 1, length, duration, fak(jobList.size))

    logger.info { bestACO.jobQue }
    logger.info { getShortestSchedule(bestACO.jobQue, STORAGE_SIZE) }

//    Plotter.plotResults(getShortestSchedule(bestACO.jobQue, STORAGE_SIZE), "TEST")

    logger.warn { "NEH/ACO = ${length.toDouble() / bestACO.duration!!.toDouble()} " }
}

fun fak(num: Int): Int {
    var result = 1
    for (n in 1 until num + 1) {
        result *= n
    }
    return result
}

fun calculateWithMeanCompletionTime(config: Config) {
    val ants: MutableList<Ant> = (0..(config.antFactor * jobList.size).toInt()).map { i -> Ant() }.toMutableList()
    val ant1 = Ant()
    ant1.jobQue = jobList.toMutableList()

    CsvLogging.createLoggingFile()
    PheromonLogger.initDB()
    val bestACO = ACO.optimizeForMCT(ants, jobList, config.evaporation, config.Q)

    logger.warn { " ACO: ${bestACO.getDurationForMCT()!!}" }
    logger.info { bestACO.jobQue }
}