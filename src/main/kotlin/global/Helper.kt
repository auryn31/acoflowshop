package global

import acoflowshop.Job
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.util.*

object Helper {

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
            val durationM1 = Random().nextInt(30)
            val durationM2 = Random().nextInt(30)
            val setupM1 = Random().nextInt(30)
            val setupM2 = Random().nextInt(30)
            val reworkM1 = ((0.3 * Random().nextDouble() + 0.3) * durationM1).toInt()
            val reworkM2 = ((0.3 * Random().nextDouble() + 0.3) * durationM2).toInt()
            jobList.add(
                    Job(
                            id = i,
                            durationMachineOne = durationM1,
                            durationMachineTwo = durationM2,
                            setupTimeMachineOne = setupM1,
                            setupTimeMachineTwo = setupM2,
                            reworktimeMachineOne = reworkM1,
                            reworktimeMachineTwo = reworkM2
                    )
            )
        }
        return jobList
    }
}