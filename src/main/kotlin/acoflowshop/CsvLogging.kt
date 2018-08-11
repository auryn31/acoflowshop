package acoflowshop

import mu.KotlinLogging
import java.io.File

private val FILE_NAME = "current"
private val logger = KotlinLogging.logger {}

class CsvLogging {

    companion object {
        /**
         * löschen des Logs der letzten iteration und erstellen eines neuen Files
         */
        fun createLoggingFile() {
            File("${FILE_NAME}.csv").delete()
            File("${FILE_NAME}.csv").createNewFile()
        }

        /**
         * schreiben der iteration in ein csv
         */
        fun appendCSVEntry(iteration: Int, currentLength: Int, durationInMs: Long) {
            logger.info { "${iteration} - ${currentLength} - ${durationInMs}" }
            File("${FILE_NAME}.csv").appendText("${iteration},${currentLength},${durationInMs}\n")
        }
    }
}