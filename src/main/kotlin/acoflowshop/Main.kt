package acoflowshop

import aco.Ant
import kotlin.math.sign

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
        Job(1,1,1, 0),
        Job(2,2,2, 1),
        Job(4,1,1,2)
)
private val numberOfJobs = jobList.size
private val numberOfAnts = (numberOfJobs * antFactor).toInt()
private val ants = (0..numberOfAnts).map { i -> Ant() }
private val pheromone: MutableList<MutableList<Double>> = initEmptyPheromonMatrix(numberOfJobs)

fun main(args: Array<String>) {

}


fun optimize(){
    var solutionNumber = 0

    while (solutionNumber < iterations) {

        for(i in 0..numberOfJobs-1) {
            ants.forEach {
//                it.selectNextJob()
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


fun initEmptyPheromonMatrix(size: Int): MutableList<MutableList<Double>> {
    val pheromonValue = 1.0/size.toDouble()
    return (0..size-1).map { (0..size-1).map { pheromonValue }.toMutableList() }.toMutableList()
}

fun updatePheromoneForAnt(ant: Ant, pheromonMatrix: MutableList<MutableList<Double>>, evaporation: Double): MutableList<MutableList<Double>> {
    val pheromonValue = 1.0 / pheromonMatrix.size * evaporation
    for (i in 0..pheromonMatrix.size-1) {
        for (j in 0..pheromonMatrix[i].size-1) {
            if(ant.jobQue[i].id==j) {
                pheromonMatrix[i][j] += pheromonValue * (pheromonMatrix.size-1)
            } else {
                pheromonMatrix[i][j] -= pheromonValue
            }
        }
    }
    return pheromonMatrix
}

fun followJobJJobI(ant: Ant, i: Int, j:Int):Boolean {
    for(k in 0..ant.jobQue.size-2){
        if(ant.jobQue[k].id == i && ant.jobQue[k+1].id == j) {
            return true
        }
    }
    return false
}