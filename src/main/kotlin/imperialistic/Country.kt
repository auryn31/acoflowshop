package imperialistic

import acoflowshop.Job
import acoflowshop.calculateDurationForMCT

data class Country(val representation: List<Job>) {

    private val costPair = this.calculateCost()
    private val duration = costPair.first
    private val reworkTime = costPair.second

    fun getCost(): Double {
        return this.duration
    }

    fun getReworkTime(): Double {
        return this.reworkTime
    }

    private fun calculateCost(): Pair<Double, Double> {
        return calculateDurationForMCT(representation.toMutableList())
    }

}