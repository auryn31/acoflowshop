package acoflowshop

import aco.ACO
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import global.*
import imperialistic.AICA
import logger_helper.CsvLogging
import logger_helper.LoggingParameter
import logger_helper.PheromonLogger
import mu.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import java.io.*

private val logger = KotlinLogging.logger {}

private val jobList: List<Job> = Helper.readJobListFromFile("100Jobs").subList(0, 50)
private val mapper = ObjectMapper().registerModule(KotlinModule())

fun main(args: Array<String>) {
    val path = args[0]
//    evaluateParamsForACOWithConfiguration(path, listOf("003", "004", "005", "006"))
    evaluateParamsForACOWithConfiguration(path, listOf("00", "01", "03", "05", "07", "09"))

//    val acoConfig = mapper.readValue(File("$path/ACOConfig.json"), ACOConfig::class.java)!!
//    val aicaConfig = mapper.readValue(File("$path/AICAConfig.json"), AICAConfig::class.java)!!
//
//    CsvLogging.fileLogging = acoConfig.fileLogging
//    CsvLogging.createLoggingFile()
//    LoggingParameter.reset()
//
//    for (i in 0 until 5) {
//        CsvLogging.fileName = "$path/current_iteration_$i"
//        CsvLogging.createLoggingFile()
//        calculateWithMeanCompletionTimeForACO(acoConfig)
//        val aica = AICA(aicaConfig)
//        aica.optimize(jobList, aicaConfig)
//        LoggingParameter.reset()
//    }
}

fun evaluateParamsForACOWithConfiguration(path: String, configs: List<String>){
    val acoConfigs = configs.map { mapper.readValue(File("$path/$it.json"), ACOConfig::class.java)!! }
    for(i in 0 until acoConfigs.size){
        CsvLogging.fileLogging = acoConfigs[i].fileLogging
        for (j in 0 until 10) {
            CsvLogging.fileName = "$path/config_${i}_iteration_$j"
            CsvLogging.createLoggingFile()
            calculateWithMeanCompletionTimeForACO(acoConfigs[i])
            LoggingParameter.reset()
        }
    }
    calculateMean(path, configs)
}

fun evaluateACOforEvaporation(path: String){
    val configs = listOf("true", "false")
    evaluateParamsForACOWithConfiguration(path, configs)
}

fun evaluateACOforAntFactor(path: String){
    val configs = listOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10")
    evaluateParamsForACOWithConfiguration(path, configs)
}

fun calculateMean(path:String, inputFiles: List<String>) {
    var fileReader: BufferedReader? = null
    var csvParser: CSVParser? = null
    var fileWriter: FileWriter? = null
    var csvPrinter: CSVPrinter? = null

    for(i in 0 until inputFiles.size) {
        val listOfRecords = mutableListOf(listOf<Double>())
        val listOfCalculationTime = mutableListOf(listOf<Int>())
        listOfRecords.removeAt(0)
        listOfCalculationTime.removeAt(0)
        for(j in 0 until 10) {
            try {
                fileReader = BufferedReader(FileReader("$path/config_${i}_iteration_$j.csv"))
                csvParser = CSVParser(fileReader,
                        CSVFormat.DEFAULT)
                val csvRecords = csvParser.records

                val entries = csvRecords.map { it[1].toDouble() }
                val timeEntries = csvRecords.map { it[2].toInt() }
                listOfRecords.add(entries)
                listOfCalculationTime.add(timeEntries)
            } catch (e: Exception) {
                println("Reading CSV Error!")
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvParser!!.close()
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error!")
                    e.printStackTrace()
                }
            }
        }

        val meanList = mutableListOf<Double>()
        val meanTimeList = mutableListOf<Double>()
        for(i in 0 until listOfRecords[0].size) {
            var mean = 0.0
            var meanTime = 0.0
            for(j in 0 until 9) {
                mean += listOfRecords[j][i]
                meanTime += listOfCalculationTime[j][i]
            }
            meanList.add(mean / 9)
            meanTimeList.add(meanTime / 9000)
        }

        try {
            fileWriter = FileWriter("$path/config_${i}_mean.csv")
            csvPrinter = CSVPrinter(fileWriter, CSVFormat.DEFAULT)

            for (i in 0 until meanList.size) {
                csvPrinter.printRecord(i, meanList[i], meanTimeList[i])
            }

            println("Write CSV successfully!")
        } catch (e: Exception) {
            println("Writing CSV error!")
            e.printStackTrace()
        } finally {
            try {
                fileWriter!!.flush()
                fileWriter.close()
                csvPrinter!!.close()
            } catch (e: IOException) {
                println("Flushing/closing error!")
                e.printStackTrace()
            }
        }
    }


}

fun calculateWithMeanCompletionTimeForACO(acoConfig: ACOConfig) {

    PheromonLogger.initDB()
    LoggingParameter.reset()
    val bestACO = ACO.optimize(jobList, acoConfig)

    logger.warn { " ACO: ${bestACO.second} with ${LoggingParameter.evaluationIteration} evaluations" }
    logger.info { bestACO.first }
}