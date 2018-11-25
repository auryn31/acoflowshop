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
        val jobList = Helper.createRandomJobList(50)
        Helper.writeJobListToFile(jobList, "jsonJobTest")
        val file = File("jsonJobTest.json")
        assertTrue(file.delete())
    }

//    @Test
//    fun generateJobs(){
//        val jobList = Helper.createRandomJobList(200)
//        Helper.writeJobListToFile(jobList, "200/jobs")
//    }


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
            rand += Helper.getNextProbabilityOfRework()
        }
        assertTrue(rand/counter < 0.06)
        assertTrue(rand/counter > 0.04)
    }

}