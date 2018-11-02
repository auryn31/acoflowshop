package logger_helper

import mu.KotlinLogging
import java.io.File


private val LOGGER = KotlinLogging.logger {}

object CsvLogging {

    var fileLogging = false
    var fileName = "current"

    /**
     * löschen des Logs der letzten iteration und erstellen eines neuen Files
     */
    fun createLoggingFile() {
        if (fileLogging) {
            File("$fileName.csv").delete()
            File("$fileName.csv").createNewFile()
        }
    }

    /**
     * schreiben der iteration in ein csv
     */
    fun appendCSVEntry(iteration: Int, currentLength: Int, durationInMs: Long, evaluationIteration: Int) {
        LOGGER.info { "${iteration} - ${currentLength} - ${durationInMs}" }
        if (fileLogging) {
            File("$fileName.csv").appendText("$iteration,$currentLength, $durationInMs,$evaluationIteration\n")
        }
    }

    fun writeNextEntry() {
        LOGGER.info { "${LoggingParameter.iteration} - ${LoggingParameter.bestDuration} - ${LoggingParameter.currentTime}" }
        if (fileLogging) {
            File("$fileName.csv")
                    .appendText("${LoggingParameter.iteration},${LoggingParameter.bestDuration},${LoggingParameter.currentTime},${LoggingParameter.evaluationIteration},${LoggingParameter.reworkTimeInPercentage}\n")
        }
    }
}