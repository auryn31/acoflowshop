package acoflowshop

import org.junit.Test
import kotlin.test.assertEquals

class JobTest {

    @Test
    fun testToString() {
        val job = Job(1,1,1,1,1,1,1)
        assertEquals("Job(1, 1, 1, 1)", job.toString())
    }

    @Test
    fun testEqual() {
        val job1 = Job(1,1,1,1,1,1,1)
        val job = Job(1,1,1,1,1,1,1)
        assertEquals(job1, job)
    }
}