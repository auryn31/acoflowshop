package logger_helper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import global.Config
import global.LoggingParameter
import mu.KotlinLogging
import java.io.File

private const val FILE_NAME = "current"
private val LOGGER = KotlinLogging.logger {}
private val mapper = ObjectMapper().registerModule(KotlinModule())
private val config = mapper.readValue(File("src/main/resources/Config.json"), Config::class.java)

object CsvLogging {
    /**
     * löschen des Logs der letzten iteration und erstellen eines neuen Files
     */
    fun createLoggingFile() {
        if (config !== null && config.fileLogging) {
            File("$FILE_NAME.csv").delete()
            File("$FILE_NAME.csv").createNewFile()
        }
    }

    /**
     * schreiben der iteration in ein csv
     */
    fun appendCSVEntry(iteration: Int, currentLength: Int, durationInMs: Long, evaluationIteration: Int) {
        LOGGER.info { "${iteration} - ${currentLength} - ${durationInMs}" }
        if (config !== null && config.fileLogging) {
            File("$FILE_NAME.csv").appendText("$iteration,$currentLength, $durationInMs,$evaluationIteration\n")
        }
    }

    fun writeNextEntry() {
        LOGGER.info { "${LoggingParameter.iteration} - ${LoggingParameter.bestDuration} - ${LoggingParameter.currentTime}" }
        if (config !== null && config.fileLogging) {
            File("$FILE_NAME.csv").appendText("${LoggingParameter.iteration},${LoggingParameter.bestDuration},${LoggingParameter.currentTime},${LoggingParameter.evaluationIteration}\n")
        }
    }
}