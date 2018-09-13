package imperialistic

import kotlin.math.cos

class Empire {

    var emperor: Country
    private var colonies: MutableList<Country>
    var costs: Double
    private val zeta = 0.1

    constructor(emperor: Country) {
        this.emperor = emperor
        this.colonies = mutableListOf()
        this.costs = emperor.getCost()
    }

    private fun calculateCost(zeta: Double) {
        this.costs = this.emperor.getCost() + zeta * colonies.map { x -> x.getCost() }.reduce { acc, d -> acc + d }
    }

    fun replaceColony(index: Int, colony: Country) {
        this.colonies[index] = colony
        this.calculateCost(this.zeta)
    }

    fun replaceEmperor(colony: Country) {
        this.emperor = colony
        this.calculateCost(0.1)
    }

    fun getTotalCost(zeta: Double): Double {
        val a = cos(this.emperor.getCost())
        val b = if (this.colonies.size > 0) zeta * this.colonies.map { cos(it.getCost()) }.reduce { acc, d -> acc + d }.toDouble() / this.colonies.size.toDouble() else 0.0
        return a + b
    }

    fun setColony(colonies: List<Country>) {
        this.colonies = colonies.toMutableList()
        this.calculateCost(this.zeta)
    }

    fun addColony(county: Country) {
        this.colonies.add(county)
        this.calculateCost(this.zeta)
    }

    fun removeColony(index: Int) {
        this.colonies.removeAt(index)
        this.calculateCost(this.zeta)
    }

    fun removeColony(colony: Country) {
        this.colonies.remove(colony)
        if (this.colonies.isEmpty()) {
            this.costs = 0.0
        } else {
            this.calculateCost(this.zeta)
        }
    }

    fun getNumberOfColonies(): Int {
        return this.colonies.size
    }

    fun getColony(index: Int): Country {
        return this.colonies[index]
    }

    fun getColonies(): List<Country> {
        return this.colonies.toList()
    }

}
