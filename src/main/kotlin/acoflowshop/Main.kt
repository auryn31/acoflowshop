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

private val logger = KotlinLogging.logger {}

private val jobList: List<Job> = Helper.readJobListFromFile("100Jobs")//.subList(0, 50)
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
        aica.optimize(jobList, aicaConfig)
        LoggingParameter.reset()
    }

}

fun calculateWithMeanCompletionTimeForACO() {

    PheromonLogger.initDB()
    LoggingParameter.reset()
    val bestACO = ACO.optimize(jobList, acoConfig)

    logger.warn { " ACO: ${bestACO.second} with ${LoggingParameter.evaluationIteration} evaluations" }
    logger.info { bestACO.first }
}