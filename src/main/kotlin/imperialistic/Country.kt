package imperialistic

import aco.ACO
import acoflowshop.Job
import acoflowshop.calculateDurationForMCT

class Country {

    var jobList: List<Job>
    var duration: Double
//    var colonies: MutableList<Country>

    constructor(representation: List<Job>) {
        this.jobList = representation
//        this.colonies = mutableListOf()
        this.duration = this.calculateCost()
    }


    fun isColony(): Boolean {
        return false
    }

    fun isImerialist(): Boolean {
        return !isColony()
    }

    fun getRepresentation(): List<Job>{
        return jobList
    }

    fun getCost(): Double {
        return duration
    }

    fun getFitness(): Double? {
        return null
    }

    fun setRepresentation(jobList: List<Job>) {
        this.jobList = jobList
        this.duration = this.calculateCost()
    }

    private fun calculateCost(): Double {
        return calculateDurationForMCT(jobList.toMutableList(), 0.1)
    }

}