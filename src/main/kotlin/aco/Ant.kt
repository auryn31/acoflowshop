package aco


import acoflowshop.Job
import global.ACOConfig
import global.Heuristik
import java.util.*
import kotlin.collections.HashMap


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

    fun selectNextJobAndAddToJobQue(jobs: HashMap<Job, Int>, pheromonMatrix: List<List<Double>>, config: ACOConfig) {
        val nextJob = selectNextJob(jobs, pheromonMatrix, config)
        this.jobQue.add(nextJob)
    }

    fun selectNextJob(jobs: HashMap<Job, Int>, pheromonMatrix: List<List<Double>>, config: ACOConfig): Job {
        val jobMap = createHashmap(jobs, pheromonMatrix, config)
        val pheromonList = jobMap.keys.sorted().toList()
        val random = Random().nextDouble()
        val key = findKey(random, pheromonList)
        return jobMap.get(key) ?: throw NullPointerException("Could not find next job in select next job!")
    }

    //TODO: heuristische komponente hinzufügen
    //TODO: heraus bekommen wie sich der parameter beta verhält
    fun createHashmap(jobsList: HashMap<Job, Int>, pheromonMatrix: List<List<Double>>, config: ACOConfig): HashMap<Double, Job> {
        val jobs = HashMap(jobsList)
        val nexPos = jobQue.size
        val jobMap = hashMapOf<Double, Job>()
        var pheromonValue = 1.0

        jobQue.forEach { jobs.remove(it) }

        val pheromonSum =  when (config.heuristic){
            Heuristik.NONE -> jobs.map { pheromonMatrix[it.value][nexPos] }.reduce { acc, d ->  acc + d}
            Heuristik.SAME_JOB_LENGTH -> heuristicForSameJobLengthSum(jobs, pheromonMatrix, nexPos, config)
        }

        for (i in jobs) {
            jobMap[pheromonValue] = i.key
            pheromonValue -= when(config.heuristic) {
                Heuristik.NONE -> pheromonMatrix[i.value][nexPos] / pheromonSum
                Heuristik.SAME_JOB_LENGTH -> heuristicForSameJobLength(jobs, pheromonMatrix, nexPos, i, config,  pheromonSum)
            }
        }
        return jobMap
    }

    private fun heuristicForSameJobLengthSum(jobs: HashMap<Job, Int>, pheromonMatrix: List<List<Double>>, nexPos: Int, config: ACOConfig): Double {
        if(this.jobQue.isEmpty()) {
            return jobs.map { Math.pow(pheromonMatrix[it.value][nexPos], (1 - config.beta)) }.reduce { acc, d -> acc + d }
        }
        return jobs.map { Math.pow(pheromonMatrix[it.value][nexPos], (1 - config.beta)) * Math.pow(getFracSmallerZero(this.jobQue.last().durationMachineOne.toDouble(), it.key.durationMachineOne.toDouble()), config.beta) }.reduce { acc, d -> acc + d }
    }

    private fun heuristicForSameJobLength(jobs: HashMap<Job, Int>, pheromonMatrix: List<List<Double>>,nextPos: Int, currentJob: MutableMap.MutableEntry<Job, Int>, config: ACOConfig, pheromonSum: Double): Double {
        if(this.jobQue.isEmpty()) {
            return Math.pow(pheromonMatrix[currentJob.value][nextPos], (1-config.beta)) / pheromonSum
        }
        return Math.pow(pheromonMatrix[currentJob.value][nextPos], (1-config.beta)) *  Math.pow(getFracSmallerZero(this.jobQue.last().durationMachineOne.toDouble(), currentJob.key.durationMachineOne.toDouble()), config.beta) / pheromonSum
    }

    private fun getFracSmallerZero(val1: Double, val2: Double): Double {
        if(val1 < val2) {
            return Math.abs(val1/val2)
        } else {
            return Math.abs(val2/val1)
        }
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