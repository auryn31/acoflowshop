package acoflowshop

import org.junit.Test
import kotlin.test.assertEquals

class DurationTest{
    @Test
    fun findBestOrderForJobOneTest(){
        val shortestList = findBestOrderForNextJob(emptyList(), Job(1,1,1), 1)
        assertEquals(listOf(Job(1,1,1)), shortestList)
    }

    @Test
    fun findBestOrderForJobOneTestOrder(){
        val jobs = mutableListOf<Job>()
        jobs.add(Job(1,1,1))
        jobs.add(Job(3,2,1))
        val shortestList = findBestOrderForNextJob(jobs, Job(2,3,1), 1)
        val targetList = mutableListOf<Job>()
        targetList.add(Job(1,1,1))
        targetList.add(Job(2,3,1))
        targetList.add(Job(3,2,1))
        assertEquals(targetList, shortestList)
    }

    @Test
    fun calculateLengthUnordered(){
        val jobs = mutableListOf<Job>()
        jobs.add(Job(1,1,1))
        jobs.add(Job(3,2,1))
        jobs.add(Job(2,3,1))
        assertEquals(9, calculatefastestScheduleWithOrder(jobs, 1))
    }

    @Test
    fun calculateLengthUnorderedduration(){
        val jobs = mutableListOf<Job>()
        jobs.add(Job(1,1,1))
        jobs.add(Job(3,2,1))
        jobs.add(Job(2,3,1))
        assertEquals(8, duration(jobs, 1))
    }

    @Test
    fun calculateLengthOrdered(){
        val jobs = mutableListOf<Job>()
        jobs.add(Job(1,1,1))
        jobs.add(Job(3,2,1))
        val shortestList = findBestOrderForNextJob(jobs, Job(2,3,1), 1)
        assertEquals(8, calculatefastestScheduleWithOrder(shortestList, 1))
    }


    @Test
    fun calculatefastestScheduleTestWithOneJob() {
        val shortestSchedule = calculatefastestScheduleWithOrder(listOf(Job(1,1,1)), 2)
        assertEquals(2, shortestSchedule)
    }

    @Test
    fun calculatefastestScheduleTestWithMoreJobs() {
        val jobs = mutableListOf<Job>(Job(1,1,1))
        jobs.add(Job(2,1,1))
        val shortestSchedule = calculatefastestScheduleWithOrder(jobs, 2)
        assertEquals(4, shortestSchedule)
    }

    @Test
    fun calculatefastestScheduleTestWith4Jobs() {
        val jobs = mutableListOf<Job>(Job(1,1,1))
        jobs.add(Job(2,3,1))
        jobs.add(Job(4,5,1))
        jobs.add(Job(1,1,1))
        val shortestSchedule = calculatefastestScheduleWithOrder(jobs, 2)
        assertEquals(13, shortestSchedule)
    }

    @Test
    fun calculatefastestScheduleTestWith4JobsInDifferentOrder() {
        val jobs = mutableListOf<Job>()
        jobs.add(Job(2,3,1))
        jobs.add(Job(4,5,1))
        jobs.add(Job(1,1,1))
        jobs.add(Job(1,1,1))
        val shortestSchedule = calculatefastestScheduleWithOrder(jobs, 2)
        assertEquals(13, shortestSchedule)
    }

    @Test
    fun calculatefastestScheduleTestWith4DifferentJobs() {
        val jobs = mutableListOf<Job>()
        jobs.add(Job(2,3,1))
        jobs.add(Job(3,5,1))
        jobs.add(Job(1,1,1))
        jobs.add(Job(1,1,1))
        val shortestSchedule = calculatefastestScheduleWithOrder(jobs, 2)
        assertEquals(12, shortestSchedule)
    }

    @Test
    fun calculatefastestScheduleTestWith3Jobs() {
        val jobs = mutableListOf<Job>()
        jobs.add(Job(1,3,1))
        jobs.add(Job(1,3,1))
        jobs.add(Job(1,3,1))
        val shortestSchedule = calculatefastestScheduleWithOrder(jobs, 1)
        assertEquals(10, shortestSchedule)
    }

    @Test
    fun calculatefastestScheduleTestWithMemory() {
        val jobs = mutableListOf<Job>()
        jobs.add(Job(2,3,1))
        jobs.add(Job(1,2,1))
        jobs.add(Job(3,1,1))
        val shortestSchedule = calculatefastestScheduleWithOrder(jobs, 1)
        assertEquals(9, shortestSchedule)
    }

}
