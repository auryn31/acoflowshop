package aco

import acoflowshop.Job
import global.ACOConfig
import global.Helper
import global.Heuristik
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AntTest {


    @Test
    fun selectNextJobTest() {
        val ant = Ant()
        val jobList = listOf(
                Job(1, 1, 1, 0),
                Job(1, 1, 1, 1)
        )
        val pheromonMatrix = listOf(
                listOf(1.0, 0.0),
                listOf(0.0, 1.0)
        )
        val jobs = Helper.createHashMapFromJobList(jobList)
        val nextJob = ant.selectNextJob(jobs, pheromonMatrix, ACOConfig(0.0, 0, 0.0, false, false, false, 0.0))
        assertEquals(0, nextJob.id)
    }

    @Test
    fun createHashmap() {
        val ant = Ant()
        val jobList = listOf(
                Job(0, 0, 0, 0),
                Job(1, 1, 1, 1)
        )
        val pheromonMatrix = listOf(
                listOf(0.7, 0.3),
                listOf(0.3, 0.7)
        )
        val jobs = Helper.createHashMapFromJobList(jobList)
        val hashMap = ant.createHashmap(jobs, pheromonMatrix, ACOConfig(0.0, 0, 0.0, false, false, false, 0.0))
        val solutionMap = hashMapOf<Double, Job>(
                Pair(1.0, Job(0, 0, 0, 0)),
                Pair(0.30000000000000004, Job(1, 1, 1, 1))
        )
        assertEquals(solutionMap, hashMap)
    }

    @Test
    fun scheduledNotScheduledTest() {
        val ant = Ant()
        val job = Job(1, 1, 1, 2)
        ant.jobQue = mutableListOf(
                Job(1, 1, 1, 0),
                Job(1, 1, 1, 1)
        )
        assertFalse(ant.scheduled(job))
    }

    @Test
    fun scheduledScheduledTest() {
        val ant = Ant()
        val job = Job(1, 1, 1, 2)
        ant.jobQue = mutableListOf(
                Job(1, 1, 1, 0),
                Job(1, 1, 1, 1),
                job
        )
        assertTrue(ant.scheduled(job))
    }

    @Test
    fun findKeyTest() {
        val ant = Ant()
        val hashMap = hashMapOf<Double, Job>(
                Pair(1.0, Job(1, 1, 1, 0)),
                Pair(0.5, Job(1, 1, 1, 1))
        )
        val pheromonList = listOf(1.0, 0.5)
        val nextJob = hashMap[ant.findKey(0.4, pheromonList)]
        assertEquals(0, nextJob?.id)
    }

    @Test
    fun findKeyTestLaterJob() {
        val ant = Ant()
        val hashMap = hashMapOf<Double, Job>(
                Pair(1.0, Job(1, 1, 1, 0)),
                Pair(0.312412, Job(1, 1, 1, 1)),
                Pair(0.12523521, Job(1, 1, 1, 2))
        )
        val pheromonList = hashMap.keys.sorted().toList()
        val nextJob = hashMap[ant.findKey(0.3111, pheromonList)]
        assertEquals(1, nextJob?.id)
    }

    @Test
    fun testFindBestAntForMCT() {
        val ant = Ant()
        val jobList = listOf(
                Job(1, 1, 1, 0),
                Job(1, 1, 1, 1)
        )
        ant.jobQue = jobList.toMutableList()

        val bestAnt = ACO.findBestAntForMCT(listOf(ant), 1)
        assertEquals(ant, bestAnt)
    }

    @Test
    fun testFindNoBestAntForMCT() {
        val bestAnt = ACO.findBestAntForMCT(listOf(), 1)
        assertNull(bestAnt)
    }

    @Test
    fun `heuristicForSameJobLength with some values` () {
        val ant = Ant()
        val jobList = listOf(
                Job(1, 1, 1, 0),
                Job(1, 1, 1, 1)
        )
        ant.jobQue = jobList.toMutableList()
        val pheromonMatrix = mutableListOf(
                mutableListOf(0.3, 0.3, 0.3),
                mutableListOf(0.3, 0.3, 0.3),
                mutableListOf(0.3, 0.3, 0.3)
        )
        val currentJob = Job(1, 1, 1, 2)
        assertEquals(0.2152558101249671, ant.heuristicForSameJobLength(pheromonMatrix, 2, currentJob,0.3, 2.0))
    }

    @Test
    fun `heuristicForSameJobLength with an empty beta` () {
        val ant = Ant()
        val jobList = listOf(
                Job(1, 1, 1, 0),
                Job(1, 1, 1, 1)
        )
        ant.jobQue = jobList.toMutableList()
        val pheromonMatrix = mutableListOf(
                mutableListOf(0.3, 0.3, 0.3),
                mutableListOf(0.3, 0.3, 0.3),
                mutableListOf(0.3, 0.3, 0.3)
        )
        val currentJob = Job(1, 1, 1, 2)
        assertEquals(1.0, ant.heuristicForSameJobLength(pheromonMatrix, 2, currentJob,0.0, 0.3))
    }

    @Test
    fun `heuristicForSameJobLengthSum with an empty beta` () {
        val ant = Ant()
        val jobList = listOf(
                Job(1, 1, 1, 0),
                Job(1, 1, 1, 1)
        )
        ant.jobQue = jobList.toMutableList()
        val pheromonMatrix = mutableListOf(
                mutableListOf(0.3, 0.3, 0.3),
                mutableListOf(0.3, 0.3, 0.3),
                mutableListOf(0.3, 0.3, 0.3)
        )
        val jobsToSchedule = hashMapOf<Job, Int>(
                Pair(Job(1, 1, 1, 2), 2)
        )
        assertEquals(0.3, ant.heuristicForSameJobLengthSum(jobsToSchedule, pheromonMatrix, 2, 0.0))
    }

    @Test
    fun `create an hashmap and evaluate without beta` () {
        val ant = Ant()
        val jobList = listOf(
                Job(1, 1, 1, 0),
                Job(1, 1, 1, 1)
        )
        ant.jobQue = jobList.toMutableList()
        val pheromonMatrix = mutableListOf(
                mutableListOf(0.3, 0.3, 0.3),
                mutableListOf(0.3, 0.3, 0.3),
                mutableListOf(0.3, 0.3, 0.3)
        )
        val jobsToSchedule = hashMapOf<Job, Int>(
                Pair(Job(1, 1, 1, 2), 2)
        )
        val config = ACOConfig(0.03, 100, 0.4, heuristic = Heuristik.SAME_JOB_LENGTH)
        assertEquals(hashMapOf(Pair(1.0, Job(1, 1, 1, 2))), ant.createHashmap(jobsToSchedule, pheromonMatrix, config))
    }


    @Test
    fun `create an hashmap and evaluate with beta` () {
        val ant = Ant()
        val jobList = listOf(
                Job(1, 1, 1, 0),
                Job(1, 1, 1, 1)
        )
        ant.jobQue = jobList.toMutableList()
        val pheromonMatrix = mutableListOf(
                mutableListOf(0.3, 0.3, 0.3),
                mutableListOf(0.3, 0.3, 0.3),
                mutableListOf(0.3, 0.3, 0.3)
        )
        val jobsToSchedule = hashMapOf<Job, Int>(
                Pair(Job(1, 1, 1, 2), 2)
        )
        val config = ACOConfig(0.03, 100, 0.4, heuristic = Heuristik.SAME_JOB_LENGTH, beta = 0.3)
        assertEquals(hashMapOf(Pair(1.0, Job(1, 1, 1, 2))), ant.createHashmap(jobsToSchedule, pheromonMatrix, config))
    }

    @Test
    fun `create an hashmap and evaluate with beta and different jobs` () {
        val ant = Ant()
        val jobList = listOf(
                Job(1, 1, 1, 0),
                Job(1, 1, 1, 1)
        )
        ant.jobQue = jobList.toMutableList()
        val pheromonMatrix = mutableListOf(
                mutableListOf(0.3, 0.3, 0.3, 0.1),
                mutableListOf(0.3, 0.3, 0.3, 0.1),
                mutableListOf(0.3, 0.3, 0.3, 0.1),
                mutableListOf(0.3, 0.3, 0.2, 0.2)
        )
        val jobsToSchedule = hashMapOf<Job, Int>(
                Pair(Job(1, 1, 1, 2), 2),
                Pair(Job(1, 1, 1, 3), 3)
        )
        val config = ACOConfig(0.03, 100, 0.4, heuristic = Heuristik.SAME_JOB_LENGTH, beta = 0.3)
        assertEquals(
                hashMapOf(Pair(1.0, Job(1, 1, 1, 3)),
                        Pair(0.5704838641764751, Job(1, 1, 1, 2))),
                ant.createHashmap(jobsToSchedule, pheromonMatrix, config)
        )
    }
}