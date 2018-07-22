package aco

import acoflowshop.Job
import acoflowshop.duration

class Ant {

    var jobQue: MutableList<Job> = mutableListOf()

    var duration: Int? = null

    fun visited(job: Job): Boolean {
        return jobQue.contains(job)
    }

    fun selectNextJob() {

    }

    fun calculateDuration(storageSize: Int){
        duration = acoflowshop.calculatefastestScheduleWithOrder(jobQue, storageSize)
    }

    fun calculateDurationWithNEH(storageSize: Int): Int {
        return acoflowshop.duration(jobQue, storageSize)
    }
}