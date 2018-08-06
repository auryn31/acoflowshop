package acoflowshop

import aco.Ant
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AntTest {


    @Test
    fun selectNextJobTest(){
        val ant = Ant()
        val jobList = listOf(
                Job(1,1,1, 0),
                Job(1,1,1, 1)
        )
        val pheromonMatrix = listOf(
                listOf(1.0,0.0),
                listOf(0.0,1.0)
        )
        val nextJob = ant.selectNextJob(jobList, pheromonMatrix)
        assertEquals(0, nextJob.id)
    }

    @Test
    fun createHashmap(){
        val ant = Ant()
        val jobList = listOf(
                Job(1,1,1, 0),
                Job(1,1,1, 1)
        )
        val pheromonMatrix = listOf(
                listOf(0.7,0.3),
                listOf(0.3,0.7)
        )
        val hashMap = ant.createHashmap(jobList, pheromonMatrix)
        val solutionMap = hashMapOf<Double, Job>(
                Pair(1.0, Job(1,1,1, 0)),
                Pair(0.30000000000000004, Job(1,1,1, 1))
        )
        assertEquals(solutionMap, hashMap)
    }

    @Test
    fun scheduledNotScheduledTest(){
        val ant = Ant()
        val job = Job(1,1,1, 2)
        ant.jobQue = mutableListOf(
                Job(1,1,1, 0),
                Job(1,1,1, 1)
        )
        assertFalse(ant.scheduled(job))
    }

    @Test
    fun scheduledScheduledTest(){
        val ant = Ant()
        val job = Job(1,1,1, 2)
        ant.jobQue = mutableListOf(
                Job(1,1,1, 0),
                Job(1,1,1, 1),
                job
        )
        assertTrue(ant.scheduled(job))
    }
}