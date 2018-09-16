package acoflowshop

import org.junit.Test
import kotlin.test.assertEquals

class DurationTest{
    @Test
    fun findBestOrderForJobOneTest(){
        val shortestList = findBestOrderForNextJob(emptyList(), Job(1,1,1,0))
        assertEquals(listOf(Job(1,1,1,0)), shortestList)
    }

    @Test
    fun findBestOrderForJobOneTestOrder(){
        val jobs = mutableListOf<Job>()
        jobs.add(Job(1,1,1,0))
        jobs.add(Job(3,2,1,1))
        val shortestList = findBestOrderForNextJob(jobs, Job(2,3,1,2))
        val targetList = mutableListOf<Job>()
        targetList.add(Job(1,1,1, 0))
        targetList.add(Job(2,3,1, 2))
        targetList.add(Job(3,2,1, 1))
        assertEquals(targetList, shortestList)
    }

    @Test
    fun calculateLengthUnordered(){
        val jobs = mutableListOf<Job>()
        jobs.add(Job(1,1,1,0))
        jobs.add(Job(3,2,1,1))
        jobs.add(Job(2,3,1,2))
        assertEquals(9, calculatefastestScheduleWithOrder(jobs, 1))
    }

    @Test
    fun calculateLengthUnorderedduration(){
        val jobs = mutableListOf<Job>()
        jobs.add(Job(1,1,1,0))
        jobs.add(Job(3,2,1,1))
        jobs.add(Job(2,3,1,2))
        assertEquals(8, duration(jobs, 1))
    }

    @Test
    fun calculateLengthOrdered(){
        val jobs = mutableListOf<Job>()
        jobs.add(Job(1,1,1,0))
        jobs.add(Job(3,2,1,1))
        val shortestList = findBestOrderForNextJob(jobs, Job(2,3,1,2))
        assertEquals(8, calculatefastestScheduleWithOrder(shortestList, 1))
    }


    @Test
    fun calculatefastestScheduleTestWithOneJob() {
        val shortestSchedule = calculatefastestScheduleWithOrder(listOf(Job(1,1,1,0)), 2)
        assertEquals(2, shortestSchedule)
    }

    @Test
    fun calculatefastestScheduleTestWithMoreJobs() {
        val jobs = mutableListOf<Job>(Job(1,1,1,0))
        jobs.add(Job(2,1,1,1))
        val shortestSchedule = calculatefastestScheduleWithOrder(jobs, 2)
        assertEquals(4, shortestSchedule)
    }

    @Test
    fun calculatefastestScheduleTestWith4Jobs() {
        val jobs = mutableListOf<Job>(
                Job(1,1,1,0),
                Job(2,3,1,1),
                Job(4,5,1,2),
                Job(1,1,1,3)
                )
        val shortestSchedule = calculatefastestScheduleWithOrder(jobs, 2)
        assertEquals(13, shortestSchedule)
    }

    @Test
    fun calculatefastestScheduleTestWith4JobsInDifferentOrder() {
        val jobs = mutableListOf<Job>()
        jobs.add(Job(2,3,1,0))
        jobs.add(Job(4,5,1,1))
        jobs.add(Job(1,1,1,2))
        jobs.add(Job(1,1,1,3))
        val shortestSchedule = calculatefastestScheduleWithOrder(jobs, 2)
        assertEquals(13, shortestSchedule)
    }

    @Test
    fun calculatefastestScheduleTestWith4DifferentJobs() {
        val jobs = mutableListOf<Job>()
        jobs.add(Job(2,3,1,0))
        jobs.add(Job(3,5,1,1))
        jobs.add(Job(1,1,1,2))
        jobs.add(Job(1,1,1,3))
        val shortestSchedule = calculatefastestScheduleWithOrder(jobs, 2)
        assertEquals(12, shortestSchedule)
    }

    @Test
    fun calculatefastestScheduleTestWith3Jobs() {
        val jobs = mutableListOf<Job>()
        jobs.add(Job(1,3,1,0))
        jobs.add(Job(1,3,1,1))
        jobs.add(Job(1,3,1,2))
        val shortestSchedule = calculatefastestScheduleWithOrder(jobs, 1)
        assertEquals(10, shortestSchedule)
    }

    @Test
    fun calculatefastestScheduleTestWithMemory() {
        val jobs = mutableListOf<Job>()
        jobs.add(Job(2,3,1,0))
        jobs.add(Job(1,2,1,1))
        jobs.add(Job(3,1,1,2))
        val shortestSchedule = calculatefastestScheduleWithOrder(jobs, 1)
        assertEquals(9, shortestSchedule)
    }

    @Test
    fun calculatefastestScheduleTestWithMemoryUse() {
        val jobs = mutableListOf<Job>(
                Job(1,3,1,0),
                Job(2,2,1,1),
                Job(1,2,1,2),
                Job(4,1,1,3)
        )
        val shortestSchedule = calculatefastestScheduleWithOrder(jobs, 2)
        assertEquals(9, shortestSchedule)
    }

    @Test
    fun calculateDurationForAICATest(){
        val jobs = mutableListOf<Job>(
                Job(1,3,1,0)
        )
        val shortestSchedule = calculateDurationForMCT(jobs, 0.0).first
        assertEquals(4.0, shortestSchedule)
    }

    @Test
    fun calculateDurationForAICATestWithTwoJobs(){
        val jobs = mutableListOf<Job>(
                Job(1,3,1,0),
                Job(2,2,1,1)
        )
        val shortestSchedule = calculateDurationForMCT(jobs, 0.0).first
        assertEquals(5.0, shortestSchedule)
    }

    @Test
    fun calculateDurationForAICATestWithTwoJobs2(){
        val jobs = mutableListOf<Job>(
                Job(1,1,1,0),
                Job(3,1,1,1)
        )
        val shortestSchedule = calculateDurationForMCT(jobs, 0.0).first
        assertEquals(3.5, shortestSchedule)
    }

    @Test
    fun calculateDurationForAICATestWithManyJobs(){
        val jobs = mutableListOf<Job>(
                Job(1,3,1,0),
                Job(2,2,1,1),
                Job(1,2,1,2),
                Job(4,1,1,3)
        )
        val shortestSchedule = calculateDurationForMCT(jobs, 0.0).first
        assertEquals(7.25, shortestSchedule)
    }

    @Test
    fun testSchedule(){
        val job = Job(1,3,1,0)
        val schedule = Schedule(job, 1)
        assertEquals("ID: ${job.id} START: 1 DURATION_ONE: ${job.durationMachineOne} DURATION_TWO: ${job.durationMachineTwo}", schedule.toString())
    }

}
