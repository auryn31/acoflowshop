package imperialistic

class Empire {

    var emperor: Country
    var colonies: MutableList<Country>
    var costs: Double

    constructor(emperor: Country) {
        this.emperor = emperor
        this.colonies = mutableListOf()
        this.costs = emperor.getCost()
    }

    private fun calculateCost(){
        this.costs = this.emperor.getCost() + colonies.map { x -> x.getCost() }.reduce { acc, d ->  acc + d}
    }

    fun replaceColony(index: Int, colony: Country) {
        this.colonies[index] = colony
        this.calculateCost()
    }

    fun replaceEmperor(colony: Country) {
        this.emperor = colony
        this.calculateCost()
    }

    fun getCost(): Double {
        return this.costs
    }

    fun addColony(county: Country) {
        this.colonies.add(county)
        this.calculateCost()
    }

    fun removeColony(index: Int) {
        this.colonies.removeAt(index)
        this.calculateCost()
    }

    fun getNumerOfColonies(): Int {
        return this.colonies.size
    }

    fun getColony(index: Int): Country {
        return this.colonies[index]
    }

}
