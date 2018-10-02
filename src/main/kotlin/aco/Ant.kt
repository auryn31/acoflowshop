package aco

import acoflowshop.Job
import java.util.*

class Ant {

    var jobQue: MutableList<Job> = mutableListOf()

    var duration: Int? = null
    private var durationForMCT: Double? = null
    var reworkPercentage: Double? = null


    fun reset() {
        this.jobQue = mutableListOf()
        this.duration = null
    }

    fun scheduled(job: Job): Boolean {
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

        // Summe der noch übrigen Gesamtmenge an Pheromonen für die noch zu wählenden Jobs berechnen
        for (i in 0 until jobs.size) {
            if (!scheduled(jobs[i])) {
                pheromonSum += pheromonMatrix[i][nexPos]
            }
        }

        // hinzufügen der restlichen Jobs zur Hashmap mit Anteilen an ihren Pheromonen
        for (i in 0 until jobs.size) {
            if (!scheduled(jobs[i])) {
                jobMap[pheromonValue] = jobs[i]
                pheromonValue -= pheromonMatrix[i][nexPos] / pheromonSum
            }
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

    fun calculateDuration(storageSize: Int) {
        duration = acoflowshop.calculatefastestScheduleWithOrder(jobQue, storageSize)
    }

    fun calculateDurationWithNEH(storageSize: Int): Int {
        return acoflowshop.duration(jobQue, storageSize)
    }

    fun calculateDurationWithNEHASC(storageSize: Int): Int {
        return acoflowshop.durationNEHASC(jobQue, storageSize)
    }

    fun getDurationForMCT(iteration: Int): Double? {
        if (this.durationForMCT == null && this.jobQue.isNotEmpty()) {
            this.calculateDurationWithMCT(iteration)
        }
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