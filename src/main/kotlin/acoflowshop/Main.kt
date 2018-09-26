package acoflowshop

import aco.ACO
import aco.Ant
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import global.*
import imperialistic.AICA
import logger_helper.CsvLogging
import logger_helper.LoggingParameter
import logger_helper.PheromonLogger
import mu.KotlinLogging
import java.io.File


private const val STORAGE_SIZE = 5
private val logger = KotlinLogging.logger {}

private val jobList: List<Job> = Helper.readJobListFromFile("100Jobs")
private val mapper = ObjectMapper().registerModule(KotlinModule())
private val acoConfig = mapper.readValue(File("src/main/resources/ACOConfig.json"), ACOConfig::class.java)!!
private val aicaConfig = mapper.readValue(File("src/main/resources/AICAConfig.json"), AICAConfig::class.java)!!

fun main(args: Array<String>) {

    CsvLogging.fileLogging = acoConfig.fileLogging
    CsvLogging.createLoggingFile()
    LoggingParameter.reset()

    for (i in 0 until 5) {
        CsvLogging.fileName = "results/current_iteration_$i"
        CsvLogging.createLoggingFile()
        calculateWithMeanCompletionTimeForACO()
        val aica = AICA(aicaConfig)
        aica.optimizeForMCT(jobList)
        LoggingParameter.reset()
    }

}

fun calculateWithMakespan() {
    val ants: MutableList<Ant> = (0..(acoConfig.antFactor * jobList.size).toInt()).map { Ant() }.toMutableList()
    val start = System.currentTimeMillis()
    val ant1 = Ant()
    ant1.jobQue = jobList.toMutableList()
    val length = ant1.calculateDurationWithNEH(STORAGE_SIZE)
    val duration = System.currentTimeMillis() - start

    CsvLogging.createLoggingFile()
    PheromonLogger.initDB()
    val bestACO = ACO.optimize(ants, jobList, STORAGE_SIZE, acoConfig.evaporation, acoConfig.maxIterations, ant1.jobQue)
    CsvLogging.appendCSVEntry(acoConfig.maxIterations + 1, length, duration, fak(jobList.size))

    logger.info { bestACO.jobQue }
    logger.info { getShortestSchedule(bestACO.jobQue, STORAGE_SIZE) }

//    Plotter.plotResults(getShortestSchedule(bestACO.jobQue, STORAGE_SIZE), "TEST")

    logger.warn { "NEH/ACO = ${length.toDouble() / bestACO.duration!!.toDouble()} " }
}

fun calculateWithMeanCompletionTimeForACO() {

    PheromonLogger.initDB()
    LoggingParameter.reset()
    val bestACO = ACO.optimizeForMCT(jobList, acoConfig)

    logger.warn { " ACO: ${bestACO.getDurationForMCT(1)!!} with ${LoggingParameter.evaluationIteration} evaluations" }
    logger.info { bestACO.jobQue }
}