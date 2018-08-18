package acoflowshop

import com.mongodb.MongoClient
import mu.KotlinLogging
import org.bson.Document
import java.io.File

private val FILE_NAME = "pheromon"
private val logger = KotlinLogging.logger {}

class PheromonLogger {
    companion object {
        val mongo = MongoClient()
        val db = mongo.getDatabase("pheromon")
        val collection = db.getCollection("pheromonValues")
        /**
         * löschen des Logs der letzten iteration und erstellen eines neuen Files
         */
        fun createLoggingFile() {
            File("${FILE_NAME}.json").delete()
            File("${FILE_NAME}.json").createNewFile()
            File("${FILE_NAME}.json").appendText("[")
        }

        fun endLogging(){
            File("${FILE_NAME}.json").appendText("]")
        }

        fun writeEntryIntoDB(iteration: Int, pheromonList: MutableList<MutableList<Double>>){
            val document = Document()
            document.put("_id", iteration)
            document.put("pheromon", pheromonList.map { it.map { (it*100).toInt() } })
            collection.insertOne(document)

        }

        fun initDB(){
            collection.drop()
        }

        /**
         * schreiben der iteration in ein csv
         */
        fun appendPheromonEntry(pheromonList: MutableList<MutableList<Double>>) {
            var json = "["
            for (pheromonRow in pheromonList) {
                json += "${pheromonRow.map { (it*100).toInt() }},"
            }
            json = json.subSequence(0, json.length-1).toString()
            json += "],\n"
            File("${FILE_NAME}.json").appendText(json)
        }
    }
}