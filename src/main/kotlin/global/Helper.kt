package global

import acoflowshop.Job
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.commons.math3.distribution.ExponentialDistribution
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object Helper {

    private val expHelper = ExponentialDistribution(0.05)
    fun writeJobListToFile(jobList: List<Job>, fileName: String) {
        File("$fileName.json").createNewFile()
        val mapper = jacksonObjectMapper()
        mapper.writeValue(File("$fileName.json"), jobList)
    }

    fun readJobListFromFile(fileName: String): List<Job> {
        val mapper = jacksonObjectMapper()
        val jobList: List<Job> =  mapper.readValue(File("$fileName.json"))
        return jobList
    }

    fun createRandomJobList(length: Int): List<Job> {

        val jobList = mutableListOf<Job>()

        for (i in 0 until length) {
            val durationM1 = Random().nextInt(30)+1
            val durationM2 = Random().nextInt(30)+1
            val setupM1 = Random().nextInt(30)+1
            val setupM2 = Random().nextInt(30)+1
            val reworkM1 = ((0.3 * Random().nextDouble() + 0.3) * durationM1).toInt()+1
            val reworkM2 = ((0.3 * Random().nextDouble() + 0.3) * durationM2).toInt()+1
            jobList.add(
                    Job(
                            id = i,
                            durationMachineOne = durationM1,
                            durationMachineTwo = durationM2,
                            setupTimeMachineOne = setupM1,
                            setupTimeMachineTwo = setupM2,
                            reworktimeMachineOne = reworkM1,
                            reworktimeMachineTwo = reworkM2,
                            probabilityOfRework = this.getNextProbabilityOfRework()
                    )
            )
        }
        return jobList
    }

    fun getNextProbabilityOfRework(): Double {
        var returnValue: Double
        do {
            returnValue = expHelper.sample()
        } while (returnValue >= 1.0 || returnValue <= 0.0)
        val df = DecimalFormat("#.####")
        df.roundingMode = RoundingMode.CEILING
        df.decimalFormatSymbols = DecimalFormatSymbols(Locale.ENGLISH)
        return df.format(returnValue).toDouble()
    }

    fun createHashMapFromJobList(jobs: List<Job>): HashMap<Job, Int>{
        val jobPosMap = hashMapOf<Job, Int>()
        for (i in jobs) {
            jobPosMap.put(i, i.id)
        }
        return jobPosMap
    }
}

fun fak(num: Int): Int {
    var result = 1
    for (n in 1 until num + 1) {
        result *= n
    }
    return result
}