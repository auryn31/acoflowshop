package acoflowshop

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.mongodb.MongoClient
import global.Config
import org.bson.Document
import java.io.File

private const val FILE_NAME = "pheromon"

object PheromonLogger {

    val mongo = MongoClient()
    val db = mongo.getDatabase("pheromon")
    val collection = db.getCollection("pheromonValues")
    val mapper = ObjectMapper().registerModule(KotlinModule())
    val config = mapper.readValue(File("src/main/resources/Config.json"), Config::class.java)
    /**
     * löschen des Logs der letzten iteration und erstellen eines neuen Files
     */
    fun createLoggingFile() {
        if (config !== null && config.dbLogging) {
            File("${FILE_NAME}.json").delete()
            File("${FILE_NAME}.json").createNewFile()
            File("${FILE_NAME}.json").appendText("[")
        }
    }

    fun endLogging() {
        if (config !== null && config.dbLogging) {
            File("${FILE_NAME}.json").appendText("]")
        }
    }

    fun writeEntryIntoDB(iteration: Int, pheromonList: MutableList<MutableList<Double>>) {
        if (config !== null && config.dbLogging) {
            val document = Document()
            document.put("_id", iteration)
            document.put("pheromon", pheromonList.map { it.map { (it * 100).toInt() } })
            collection.insertOne(document)
        }

    }

    fun initDB() {
        if (config !== null && config.dbLogging) {
            collection.drop()
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
        File("${FILE_NAME}.json").appendText(json)

    }
}