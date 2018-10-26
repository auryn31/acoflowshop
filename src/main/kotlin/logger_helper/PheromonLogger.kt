package logger_helper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import global.ACOConfig
import org.bson.Document
import java.io.File

private const val FILE_NAME = "pheromon"

object PheromonLogger {

    var mongo: MongoClient? = null
    var db: MongoDatabase? = null
    var collection: MongoCollection<Document>? = null
    val mapper = ObjectMapper().registerModule(KotlinModule())
    val config = mapper.readValue(File("src/main/resources/ACOConfig.json"), ACOConfig::class.java)
    /**
     * löschen des Logs der letzten iteration und erstellen eines neuen Files
     */
    fun createLoggingFile() {
        if (config !== null && config.dbLogging) {
            File("$FILE_NAME.json").delete()
            File("$FILE_NAME.json").createNewFile()
            File("$FILE_NAME.json").appendText("[")
        }
    }

    fun endLogging() {
        if (config !== null && config.dbLogging) {
            File("$FILE_NAME.json").appendText("]")
        }
    }

    fun writeEntryIntoDB(iteration: Int, pheromonList: List<List<Double>>) {
        if (config !== null && config.dbLogging) {
            val document = Document()
            document.put("_id", iteration)
            document.put("pheromon", pheromonList.map { it.map { (it * 100).toInt() } })
            collection?.insertOne(document)
        }

    }

    fun initDB() {
        if (config !== null && config.dbLogging) {
            mongo = MongoClient()
            db = mongo?.getDatabase("pheromon")
            collection = db?.getCollection("pheromonValues")
            collection?.drop()
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