package imperialistic

import acoflowshop.Job
import java.util.*

class AICA {
    companion object {
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
                    val colonyRepresentation = colony.getRepresentation()
                    val assimilationRate = 3.0 / 7.0

                    val numberOfTasks = (colonyRepresentation.size.toDouble() * assimilationRate).toInt()
                    val candidatesArray = colonyRepresentation.map { 0 }.toMutableList()

                    var numberOfOnes = 0
                    while (numberOfTasks > numberOfOnes) {
                        val indexToSetOne = Random().nextInt(colonyRepresentation.size - 1)
                        if (candidatesArray[indexToSetOne] == 0) {
                            candidatesArray[indexToSetOne] = 1
                            numberOfOnes++
                        }
                    }

                    val assimilationList = colonyRepresentation.toMutableList()
                    // neues Land finden
                    for (i in 0 until candidatesArray.size) {
                        if (candidatesArray[i] == 1) {
                            val current = assimilationList[i]
                            for (j in 0 until assimilationList.size) {
                                if (assimilationList[j].id == empireRepresentation.getRepresentation()[i].id) {
                                    assimilationList[j] = current
                                }
                            }
                            assimilationList[i] = empireRepresentation.getRepresentation()[i]
                        }
                    }
                    empire.addColony(Country(assimilationList))
                }
            }
            return empires
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