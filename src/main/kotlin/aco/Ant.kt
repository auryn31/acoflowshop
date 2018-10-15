package aco

import acoflowshop.Job
import java.util.*

class Ant {

    var jobQue: MutableList<Job> = mutableListOf()

    private var durationForMCT: Double? = null
    var reworkPercentage: Double? = null


    fun reset() {
        this.jobQue = mutableListOf()
    }

    internal fun scheduled(job: Job): Boolean {
        return jobQue.filter { it.id == job.id }.isNotEmpty()
    }

    fun selectNextJobAndAddToJobQue(jobs: List<Job>, pheromonMatrix: List<List<Double>>) {
        this.jobQue.add(selectNextJob(jobs, pheromonMatrix))
    }

    fun selectNextJob(jobs: List<Job>, pheromonMatrix: List<List<Double>>): Job {
        val jobMap = createHashmap(jobs, pheromonMatrix)
        val pheromonList = jobMap.keys.sorted().toList()
        val random = Random().nextDouble()
        val key = findKey(random, pheromonList)
        return jobMap.getOrDefault(key, Job(1, 1, 1, 0))
    }


    fun createHashmap(jobs: List<Job>, pheromonMatrix: List<List<Double>>): HashMap<Double, Job> {
        val nexPos = jobQue.size
        val jobMap = hashMapOf<Double, Job>()
        var pheromonValue = 1.0
        var pheromonSum = 0.0

        val jobPosMap = hashMapOf<Job, Int>()
        for (i in jobs) {
            jobPosMap.put(i, i.id)
        }

        jobQue.forEach { jobPosMap.remove(it) }

        pheromonSum = jobPosMap.map { pheromonMatrix[it.value][nexPos] }.reduce { acc, d ->  acc + d}

        for (i in jobPosMap) {
            jobMap[pheromonValue] = i.key
            pheromonValue -= pheromonMatrix[i.value][nexPos] / pheromonSum
        }
        return jobMap
    }

    fun findKey(pheromon: Double, pheromonList: List<Double>): Double {
        for (i in 0 until pheromonList.size) {
            if (pheromon < pheromonList[i]) {
                return pheromonList[i]
            }
        }
        return pheromonList.first()
    }

    fun getDurationForMCT(iteration: Int): Double? {
        if (this.durationForMCT == null && this.jobQue.isNotEmpty()) {
            this.calculateDurationWithMCT(iteration)
        }
        return this.durationForMCT
    }

    fun getDuationForMTCWithourRecalculation(): Double? {
        return this.durationForMCT
    }

    fun setDurationForMCT(duration: Double, reworkPercentage: Double) {
        this.durationForMCT = duration
        this.reworkPercentage = reworkPercentage
    }

    internal fun calculateDurationWithMCT(iteration: Int) {
        var costSum = 0.0
        var reworkPercentageSum = 0.0
        var calculationFrequency = iteration/50
        if(calculationFrequency <= 0) {
            calculationFrequency = 1
        }
        for (i in 0 until calculationFrequency) {
            val costPair = acoflowshop.calculateDurationForMCT(jobQue)
            costSum += costPair.first
            reworkPercentageSum += costPair.second
        }
        this.durationForMCT = costSum/ ( calculationFrequency.toDouble() )
        this.reworkPercentage = reworkPercentageSum / ( calculationFrequency.toDouble() )
    }
}