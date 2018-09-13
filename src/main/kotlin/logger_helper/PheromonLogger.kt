package logger_helper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.mongodb.MongoClient
import global.Config
import org.bson.Document
import java.io.File

private const val FILE_NAME = "pheromon"

object PheromonLogger {

    val mongo = MongoClient()
    val db = PheromonLogger.mongo.getDatabase("pheromon")
    val collection = PheromonLogger.db.getCollection("pheromonValues")
    val mapper = ObjectMapper().registerModule(KotlinModule())
    val config = PheromonLogger.mapper.readValue(File("src/main/resources/Config.json"), Config::class.java)
    /**
     * löschen des Logs der letzten iteration und erstellen eines neuen Files
     */
    fun createLoggingFile() {
        if (PheromonLogger.config !== null && PheromonLogger.config.dbLogging) {
            File("$FILE_NAME.json").delete()
            File("$FILE_NAME.json").createNewFile()
            File("$FILE_NAME.json").appendText("[")
        }
    }

    fun endLogging() {
        if (PheromonLogger.config !== null && PheromonLogger.config.dbLogging) {
            File("$FILE_NAME.json").appendText("]")
        }
    }

    fun writeEntryIntoDB(iteration: Int, pheromonList: MutableList<MutableList<Double>>) {
        if (PheromonLogger.config !== null && PheromonLogger.config.dbLogging) {
            val document = Document()
            document.put("_id", iteration)
            document.put("pheromon", pheromonList.map { it.map { (it * 100).toInt() } })
            PheromonLogger.collection.insertOne(document)
        }

    }

    fun initDB() {
        if (PheromonLogger.config !== null && PheromonLogger.config.dbLogging) {
            PheromonLogger.collection.drop()
        }
    }

    /**
     * schreiben der iteration in ein csv
     */
    fun appendPheromonEntry(pheromonList: MutableList<MutableList<Double>>) {
        var json = "["
        for (pheromonRow in pheromonList) {
            json += "${pheromonRow.map { (it * 100).toInt() }},"
        }
        json = json.subSequence(0, json.length - 1).toString()
        json += "],\n"
        File("$FILE_NAME.json").appendText(json)

    }
}