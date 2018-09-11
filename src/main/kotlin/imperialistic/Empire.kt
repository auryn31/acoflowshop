package imperialistic

import kotlin.math.cos

class Empire {

    var emperor: Country
    private var colonies: MutableList<Country>
    var costs: Double

    constructor(emperor: Country) {
        this.emperor = emperor
        this.colonies = mutableListOf()
        this.costs = emperor.getCost()
    }

    private fun calculateCost() {
        this.costs = this.emperor.getCost() + colonies.map { x -> x.getCost() }.reduce { acc, d -> acc + d }
    }

    fun replaceColony(index: Int, colony: Country) {
        this.colonies[index] = colony
        this.calculateCost()
    }

    fun replaceEmperor(colony: Country) {
        this.emperor = colony
        this.calculateCost()
    }

    fun getTotalCost(zeta: Double): Double {
        val a = cos(this.emperor.getCost())
        val b = if (this.colonies.size > 0) zeta * this.colonies.map { cos(it.getCost()) }.reduce { acc, d -> acc + d }.toDouble() / this.colonies.size.toDouble() else 0.0
        return a + b
    }

    fun setColony(colonies: List<Country>) {
        this.colonies = colonies.toMutableList()
    }

    fun addColony(county: Country) {
        this.colonies.add(county)
        this.calculateCost()
    }

    fun removeColony(index: Int) {
        this.colonies.removeAt(index)
        this.calculateCost()
    }

    fun removeColony(colony: Country) {
        this.colonies.remove(colony)
        if (this.colonies.isEmpty()) {
            this.costs = 0.0
        } else {
            this.calculateCost()
        }
    }

    fun getNumerOfColonies(): Int {
        return this.colonies.size
    }

    fun getColony(index: Int): Country {
        return this.colonies[index]
    }

    fun getColonies(): List<Country> {
        return this.colonies.toList()
    }

}
