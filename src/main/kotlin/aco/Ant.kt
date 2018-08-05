package aco

import acoflowshop.Job
import acoflowshop.duration
import java.util.*
import kotlin.collections.HashMap

class Ant {

    var jobQue: MutableList<Job> = mutableListOf()

    var duration: Int? = null

    fun scheduled(job: Job): Boolean {
        return jobQue.contains(job)
    }

    fun selectNextJob(jobs: List<Job>, pheromonMatrix: List<List<Double>>): Job {
        val jobMap = createHashmap(jobs, pheromonMatrix)
        val pheromonList = jobMap.map { entry -> entry.key }.sorted()
        val random = Random().nextDouble()
        val key = findKey(random, pheromonList)
        return jobMap.getOrDefault(key, Job(1,1,1))
    }

    fun createHashmap(jobs: List<Job>, pheromonMatrix: List<List<Double>>):HashMap<Double, Job>{
        val nexPos = jobQue.size
        val jobMap = hashMapOf<Double, Job>()
        var pheromonValue = 1.0
        for(i in 0..jobs.size-1) {
            if(!scheduled(jobs[i])) {
                jobMap[pheromonValue] = jobs[i]
                pheromonValue -= pheromonMatrix[i][nexPos]
            }
        }
        return jobMap
    }

    fun findKey(pheromon: Double, pheromonList: List<Double>):Double {
        for(i in 0..pheromonList.size-1) {
            if(pheromon < pheromonList[i]) {
                return pheromonList[i]
            }
        }
        return pheromonList.first()
    }

    fun calculateDuration(storageSize: Int){
        duration = acoflowshop.calculatefastestScheduleWithOrder(jobQue, storageSize)
    }

    fun calculateDurationWithNEH(storageSize: Int): Int {
        return acoflowshop.duration(jobQue, storageSize)
    }
}