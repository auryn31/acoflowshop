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
        val jobList = Helper.createRandomJobList(10)
        Helper.writeJobListToFile(jobList, "src/test/resources/jsonTest")
        assertTrue(File("src/test/resources/100Jobs.json").isFile)
    }

    @Test
    fun testReadJobListFromFile(){
        val jobList = Helper.createRandomJobList(10)
        Helper.writeJobListToFile(jobList, "src/test/resources/jsonTest")
        assertTrue(File("src/test/resources/100Jobs.json").isFile)
        val newJobs = Helper.readJobListFromFile("src/test/resources/jsonTest")
        assertEquals(jobList, newJobs)
    }

}