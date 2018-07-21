package acoflowshop

import aco.Ant


private val c = 1.0
private val alpha = 1.0
private val beta = 5.0
private val evaporation = 0.5
private val Q = 500.0
private val antFactor = 0.8
private val randomFactor = 0.01
private val iterations = 1000
private val STORAGE_SIZE = 2

private val jobList: List<Job> = listOf(
        Job(1,1,1),
        Job(2,2,2),
        Job(4,1,1)
)
private val numberOfJobs = jobList.size
private val numberOfAnts = (numberOfJobs * antFactor).toInt()
private val ants = (0..numberOfAnts).map { i -> Ant() }
private val pheromone: MutableList<MutableList<Double>> = initEmptyPheromonMatrix(numberOfJobs, 0.0)

fun main(args: Array<String>) {

}


fun optimize(){
    var solutionNumber = 0

    while (solutionNumber < iterations) {

        for(i in 0..numberOfJobs-1) {
            ants.forEach {
                it.selectNextJob()
            }
        }
        ants.forEach { it.calculateDuration(STORAGE_SIZE) }

        val bestAnt = findBestAnt(ants)



        solutionNumber++
    }
}

fun findBestAnt(ants: List<Ant>): Ant? {
    return ants.sortedBy { it.duration }.firstOrNull()
}


fun initEmptyPheromonMatrix(size: Int, pheromonValue: Double): MutableList<MutableList<Double>> {
    return (0..size-1).map { (0..size-1).map { pheromonValue }.toMutableList() }.toMutableList()
}

fun updatePheromoneForAnt(ant: Ant, pheromonMatrix: MutableList<MutableList<Double>>) {
    
}