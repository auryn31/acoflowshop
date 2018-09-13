package imperialistic

import acoflowshop.Job
import java.util.*

object AICA {
    fun optimizeForMCT(jobList: List<Job>, iterations: Int) {

    }

    fun createCountries(k: Int): MutableList<Country> {
//            val k = 10 // number of countries
//            val r = 5 // number of processors
//            val n = 3 // number of tasks
        val jobList = createRandomJobList(k)

        val countries = mutableListOf<Country>()

        for (i in 0 until k) {
            val candidate = Country(generator(jobList))
            countries.add(candidate)
        }

        return countries
    }

    fun createEmpires(countries: List<Country>): List<Empire> {
        val numberOfEmpires = 2
        val newCountries = countries.sortedBy { it.getCost() }
        val candidateEmpires = newCountries.subList(0, numberOfEmpires)
        val candidateColonies = newCountries.subList(numberOfEmpires, newCountries.size)

        // erzeugen der Empires
        val empires = mutableListOf<Empire>()
        candidateEmpires.forEach { empires.add(Empire(it)) }

        // hinzufügen der Länder als Colonien
        candidateColonies.forEach {
            val randomNumber = Random().nextInt(empires.size)
            empires[randomNumber].addColony(it)
        }

        return empires.toList()
    }

    fun assimilate(empires: List<Empire>): List<Empire> {
        for (empire in empires) {
            val empireRepresentation = empire.emperor
            val colonies = empire.getColonies()
            for (colony in colonies) {
                val colonyRepresentation = colony.representation
                val assimilationRate = 3.0 / 7.0

                val numberOfTasks = (colonyRepresentation.size.toDouble() * assimilationRate).toInt()
                val candidatesArray = createNewCandidateArray(colonyRepresentation, numberOfTasks)

//                    val assimilationList = colonyRepresentation.toMutableList()
                val newColony: MutableList<Job?> = colonyRepresentation.map { null }.toMutableList()
                // neues Land finden
                for (i in 0 until candidatesArray.size) {
                    if (candidatesArray[i] == 1) {
                        newColony[i] = empireRepresentation.representation[i]
                    }
                }
                for (i in 0 until candidatesArray.size) {
                    if (candidatesArray[i] == 0 && !newColony.contains(colonyRepresentation[i])) {
                        newColony[i] = colonyRepresentation[i]
                    }
                }
                val notSetCountries = colonyRepresentation.filter { !newColony.contains(it) }.toMutableList()
                for (i in 0 until candidatesArray.size) {
                    if (newColony[i] == null) {
                        newColony[i] = notSetCountries.first()
                        notSetCountries.removeAt(0)
                    }
                }
                empire.addColony(Country(newColony.map { it!! }.toMutableList()))
            }
        }
        return empires
    }

    internal fun createNewCandidateArray(colonyRepresentation: List<Job>, numberOfTasks: Int): List<Int> {
        val candidatesArray = colonyRepresentation.map { 0 }.toMutableList()

        var numberOfOnes = 0
        while (numberOfTasks > numberOfOnes) {
            val indexToSetOne = Random().nextInt(colonyRepresentation.size - 1)
            if (candidatesArray[indexToSetOne] == 0) {
                candidatesArray[indexToSetOne] = 1
                numberOfOnes++
            }
        }
        return candidatesArray
    }

    fun exchangePositions(empires: List<Empire>): List<Empire> {
        for (empire in empires) {
            val colonies = empire.getColonies().sortedBy { it.getCost() }
            if (colonies[0].getCost() < empire.emperor.getCost()) {
                empire.addColony(empire.emperor)
                empire.emperor = empire.getColony(0)
                empire.removeColony(0)
            }
        }
        return empires
    }

    fun imperialisticCompetition(empires: List<Empire>): List<Empire> {
        val weakestEmpire = empires.sortedByDescending { it.costs }.first()
        val weakestColony = weakestEmpire.getColonies().sortedByDescending { it.getCost() }[0]
        weakestEmpire.removeColony(weakestColony)

        //fight um die colony
        return Helper.distributeColonyWithRoulette(empires, weakestColony)
    }

    fun eliminatingPowerlessEmpires(empires: List<Empire>): List<Empire> {
        var newEmpires = empires.filter { it.getColonies().isNotEmpty() }
        val powerlessEmpires = empires.filter { it.getColonies().isEmpty() }
        for (powerlessEmpire in powerlessEmpires) {
            newEmpires = Helper.distributeColonyWithRoulette(newEmpires, powerlessEmpire.emperor)
        }
        return newEmpires
    }

    fun stoppingCriteriaIsReached(empires: List<Empire>, iteration: Int, maxIterations: Int): Boolean {
        return empires.size == 1 || iteration >= maxIterations
    }


    fun revolution(empires: List<Empire>, P_ir: Double) {
        for(empire in empires) {
            if(empire.getNumberOfColonies() > 0) {
                val changesInEmpires = (empire.getNumberOfColonies().toDouble() * P_ir).toInt()
                var imperialistMod: Country? = null
                for(i in 0 until changesInEmpires) {
                    val pos1 = Random().nextInt(empire.emperor.representation.size)
                    val pos2 = Random().nextInt(empire.emperor.representation.size)
                    val newRepresentation = empire.emperor.representation.toMutableList()
                    val cacheJob = newRepresentation[pos1]
                    newRepresentation[pos1] = newRepresentation[pos2]
                    newRepresentation[pos2] = cacheJob
                    imperialistMod = Country(newRepresentation)
                }
                val badestColony = empire.getColonies().sortedBy { it.getCost() }.last()
                empire.removeColony(badestColony)
                empire.addColony(imperialistMod!!)
            }
        }
    }
}


fun generator(jobs: List<Job>): List<Job> {
    return jobs.shuffled()
}


fun createRandomJobList(length: Int): List<Job> {

    val jobList = mutableListOf<Job>()

    for (i in 0 until length) {
        val durationM1 = Random().nextInt(30)
        val durationM2 = Random().nextInt(30)
        val setupM1 = Random().nextInt(30)
        val setupM2 = Random().nextInt(30)
        val reworkM1 = ((0.3 * Random().nextDouble() + 0.3) * durationM1).toInt()
        val reworkM2 = ((0.3 * Random().nextDouble() + 0.3) * durationM2).toInt()
        jobList.add(
                Job(
                        id = i,
                        durationMachineOne = durationM1,
                        durationMachineTwo = durationM2,
                        setupTimeMachineOne = setupM1,
                        setupTimeMachineTwo = setupM2,
                        reworktimeMachineOne = reworkM1,
                        reworktimeMachineTwo = reworkM2
                )
        )
    }
    return jobList
}