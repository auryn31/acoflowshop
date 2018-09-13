package imperialistic

import acoflowshop.Job
import acoflowshop.calculateDurationForMCT

data class Country(val representation: List<Job>) {

    private var duration = this.calculateCost()

    fun getCost(): Double {
        return duration
    }

    private fun calculateCost(): Double {
        return calculateDurationForMCT(representation.toMutableList(), 0.1)
    }

}