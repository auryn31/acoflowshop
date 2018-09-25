package global

import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HelperTest {

    @Test
    fun testCreateJoblist(){
        val jobList = Helper.createRandomJobList(10)
        assertEquals(10, jobList.size)
    }

    @Test
    fun testWriteJobListToFile(){
        val jobList = Helper.createRandomJobList(100)
        Helper.writeJobListToFile(jobList, "jsonJobTest")
        val file = File("jsonJobTest.json")
        assertTrue(file.delete())
    }

    @Test
    fun testReadJobListFromFile(){
        val jobList = Helper.createRandomJobList(10)
        Helper.writeJobListToFile(jobList, "jsonJobTest")
        val newJobs = Helper.readJobListFromFile("jsonJobTest")
        assertEquals(jobList, newJobs)
        assertTrue(File("jsonJobTest.json").delete())
    }

    @Test
    fun testGetNextProbabilityOfRework(){
        var rand = 0.0
        val counter = 10000
        for(i in 0 until counter) {
            println(Helper.getNextProbabilityOfRework())
            rand += Helper.getNextProbabilityOfRework()
        }
        assertTrue(rand/counter < 0.05)
        assertTrue(rand/counter > 0.04)
    }

}