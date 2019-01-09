package imperialistic


class Empire(var emperor: Country, var xi: Double) {

    private var colonies = mutableListOf<Country>()
    private var costs = emperor.getCost()
//    private val zeta = 0.48

    private fun calculateCost() {
        this.costs = this.emperor.getCost() + xi * colonies.map { x -> x.getCost() }.reduce { acc, d -> acc + d }
    }

//    fun replaceColony(index: Int, colony: Country) {
//        this.colonies[index] = colony
//        this.calculateCost(this.zeta)
//    }
//
//    fun replaceEmperor(colony: Country) {
//        this.emperor = colony
//        this.calculateCost(0.1)
//    }

    fun getTotalCost(): Double {
        val a = this.emperor.getCost()
        val b = if (this.colonies.size > 0) this.colonies.map { it.getCost() }.reduce { acc, d -> acc + d }.toDouble() / this.colonies.size.toDouble() else 0.0
        return a + b
    }

    fun setColony(colonies: List<Country>) {
        this.colonies = colonies.toMutableList()
        this.calculateCost()
    }

    fun exchangeColonies(countyToRemove: Country, countyToAdd: Country) {
        this.colonies.remove(countyToRemove)
        this.colonies.add(countyToAdd)
        this.calculateCost()
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
