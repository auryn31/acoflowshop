package acoflowshop

import java.util.*
import kotlin.math.max

fun duration(jobsList: List<Job>, storageSize: Int): Int {
    val jobs = jobsList.sortedByDescending { it.durationMachineOne + it.durationMachineTwo }
    var jobOrder = mutableListOf<Job>()
    for (job in jobs) {
        jobOrder = findBestOrderForNextJob(jobOrder, job, storageSize).toMutableList()
    }
    return calculatefastestScheduleWithOrder(jobOrder, storageSize)
}

fun durationNEHASC(jobsList: List<Job>, storageSize: Int): Int {
    val jobs = jobsList.sortedBy { it.durationMachineOne + it.durationMachineTwo }
    var jobOrder = mutableListOf<Job>()
    for (job in jobs) {
        jobOrder = findBestOrderForNextJob(jobOrder, job, storageSize).toMutableList()
    }
    return calculatefastestScheduleWithOrder(jobOrder, storageSize)
}

fun findBestOrderForNextJob(machineList: List<Job>, jobToAdd: Job, storageSize: Int): List<Job> {
    if (machineList.isEmpty()) {
        return listOf(jobToAdd)
    }
    var shortestList = machineList.toMutableList()
    shortestList.add(jobToAdd)
    var shortestSum = calculatefastestScheduleWithOrder(shortestList, storageSize)

    for (i in 0..machineList.size - 1) {
        val left = machineList.subList(0, i)
        val right = machineList.subList(i, machineList.size)
        val currentList = left.toMutableList()
        currentList.add(jobToAdd)
        currentList.addAll(right)
        val currentLength = calculatefastestScheduleWithOrder(currentList, storageSize)
        if (currentLength < shortestSum) {
            shortestSum = currentLength
            shortestList = currentList
        }
    }
    return shortestList
}

fun calculatefastestScheduleWithOrder(jobList: List<Job>, storageSize: Int): Int {
    if (jobList.isEmpty()) {
        return 0
    }
    val schedule = getShortestSchedulePair(jobList, storageSize)
    return schedule.second.last().job.durationMachineTwo + schedule.second.last().start
}

fun getShortestSchedulePair(jobList: List<Job>, storageSize: Int): Triple<List<Schedule>, List<Schedule>, List<Memory>> {
    var currentlyUsedStorage = 0
    val machineOneStack = LinkedList<Job>()
    machineOneStack.addAll(jobList)
    val machineTwoStack = LinkedList<Job>()
    machineTwoStack.addAll(jobList)
    val machineOne = mutableListOf<Schedule>()
    val machineTwo = mutableListOf<Schedule>()
    val memory = mutableListOf<Memory>()
    var currentlyInStorage= mutableListOf<Job>()


    var counter = 0
    while(machineTwoStack.isNotEmpty()) {
        // erste maschine ist leer
        if(counter == 0) {
            addNextJobToMachineOne(machineOne, machineOneStack, counter)
        } else {

            // wenn der erste job der maschine 1 fertig ist
            if(machineOne.first().start + machineOne.first().job.durationMachineOne <= counter) {

                //zweite maschine ist leer
                if(machineTwo.size == 0) {
                    addNextJobToMachineTwo(machineTwo, machineTwoStack, counter)
                } else {
                    // maschine 2 ist fertig
                    if(machineTwo.last().job.durationMachineTwo+machineTwo.last().start <= counter) {
                        // ist nächster job auf maschine 1 fertig?
                        val nextJob = machineTwoStack.first
                        val nextJobOnMachineOne = machineOne.filter { it.job.id == nextJob.id }.firstOrNull()
                        if(nextJobOnMachineOne != null) {
                            if(nextJobOnMachineOne.start + nextJobOnMachineOne.job.durationMachineOne <= counter) {
                                addNextJobToMachineTwo(machineTwo, machineTwoStack, counter)

                                //neuen job aus dem memory nehmen
                                val newJobOnMachineTwoWasInStorage = currentlyInStorage.filter { it.id == machineTwo.last().job.id }.isNotEmpty()
                                if(newJobOnMachineTwoWasInStorage) {
                                    currentlyInStorage = currentlyInStorage.filter { it.id != machineTwo.last().job.id }.toMutableList()
                                    currentlyUsedStorage -= machineTwo.last().job.storageSize
                                    memory.last().end = counter
                                }
                            }

                        }

                    }
                }
            }

            // maschine 1 ist fertig
            if(machineOne.last().job.durationMachineOne + machineOne.last().start <= counter) {

                // der letzte job von maschine 1 ist noch nicht auf maschine 2 und noch nicht im speicher
                val nextJobIsNotInMemory = currentlyInStorage.filter { it.id == machineOne.last().job.id }.isEmpty()
                val nextJobIsNotOnMachineTwo = machineOne.last().job.id != machineTwo.last().job.id
                if(nextJobIsNotOnMachineTwo && nextJobIsNotInMemory) {
                    currentlyUsedStorage = addJobToStorage(currentlyUsedStorage, machineOne, currentlyInStorage, memory, counter)
                }

                // es gibt noch jobs für maschine 1
                if(machineOneStack.isNotEmpty() && currentlyUsedStorage < storageSize){
                    addNextJobToMachineOne(machineOne, machineOneStack, counter)
                }
            }
        }
        counter++
    }

    return Triple(machineOne, machineTwo, memory)
}

private fun addJobToStorage(currentlyUsedStorage: Int, machineOne: MutableList<Schedule>, currentlyInStorage: MutableList<Job>, memory: MutableList<Memory>, counter: Int): Int {
    var currentlyUsedStorage1 = currentlyUsedStorage
    currentlyUsedStorage1 += machineOne.last().job.storageSize
    currentlyInStorage.add(machineOne.last().job)
    memory.add(Memory(currentlyUsedStorage1, counter, null))
    return currentlyUsedStorage1
}

private fun addNextJobToMachineTwo(machineTwo: MutableList<Schedule>, machineTwoStack: LinkedList<Job>, counter: Int) {
    machineTwo.add(Schedule(machineTwoStack.remove(), counter))
}

private fun addNextJobToMachineOne(machineOne: MutableList<Schedule>, machineOneStack: LinkedList<Job>, counter: Int) {
    machineOne.add(Schedule(machineOneStack.remove(), counter))
}

class Schedule(val job: Job, val start: Int) {

    override fun toString(): String {
        return "ID: ${job.id} START: ${start} DURATION_ONE: ${job.durationMachineOne} DURATION_TWO: ${job.durationMachineTwo}"
    }

}

class Memory(val inUse: Int, val start: Int, var end: Int?) {

}