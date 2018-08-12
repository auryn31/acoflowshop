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
            machineOne.add(Schedule(machineOneStack.remove(), counter))
        } else {

            // wenn der erste job der maschine 1 fertig ist
            if(machineOne.first().start + machineOne.first().job.durationMachineOne <= counter) {

                //zweite maschine ist leer
                if(machineTwo.size == 0) {
                    machineTwo.add(Schedule(machineTwoStack.remove(), counter))
                } else {
                    // maschine 2 ist fertig
                    if(machineTwo.last().job.durationMachineTwo+machineTwo.last().start <= counter) {
                        // ist nächster job auf maschine 1 fertig?
                        val nextJob = machineTwoStack.first
                        val nextJobOnMachineOne = machineOne.filter { it.job.id == nextJob.id }.firstOrNull()
                        if(nextJobOnMachineOne != null) {
                            if(nextJobOnMachineOne.start + nextJobOnMachineOne.job.durationMachineOne <= counter) {
                                machineTwo.add(Schedule(machineTwoStack.remove(), counter))

                                //neuen job aus dem memory nehmen
                                if(currentlyInStorage.filter { it.id == machineTwo.last().job.id }.isNotEmpty()) {
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
                if(machineOne.last().job.id != machineTwo.last().job.id && currentlyInStorage.filter { it.id == machineOne.last().job.id }.isEmpty()) {
                    currentlyUsedStorage += machineOne.last().job.storageSize
                    currentlyInStorage.add(machineOne.last().job)
                    memory.add(Memory(currentlyUsedStorage, counter, null))
                }

                // es gibt noch jobs für maschine 1
                if(machineOneStack.isNotEmpty() && currentlyUsedStorage < storageSize){
                    machineOne.add(Schedule(machineOneStack.remove(),counter))
                }


            }



        }
        counter++
    }



//    var allDone = false
//
//    while (!allDone) {
//        // wurde schon ein job durchgeführt
//        if (counter == 0) {
//            machineOne.add(Schedule(jobList.first(), 0))
//        } else {
//            // ist der erste job schon fertig
//            if (machineOne.first().start + machineOne.first().job.durationMachineOne <= counter) {
//
//                // wurde noch kein job auf maschine 1 durchgeführt
//                if (machineTwo.isEmpty()) {
//                    machineTwo.add(Schedule(jobList.first(), counter))
//                    if (jobList.size == 1) {
//                        allDone = true
//                    }
//                } else {
//                    // ist der nächste schon auf maschine 1 fertig?
//                    var nextJob: Schedule? = null
//                    for (i in 0 until jobList.size) {
//                        if (jobList[i].id == machineTwo.last().job.id && i < jobList.size - 1) {
//                            val filteredJobs = machineOne.filter { it.job.id == jobList[i + 1].id }
//                            if (!filteredJobs.isEmpty()) {
//                                nextJob = filteredJobs.first()
//                            }
//                            break
//                        }
//                        if (i == jobList.size - 1) {
//                            allDone = true
//                        }
//                    }
//                    if (nextJob != null) {
//                        val machineTwoIsFinished = machineTwo.last().job.durationMachineTwo + machineTwo.last().start <= counter
//                        val nextJobIsFinishedOnMachienOne = nextJob.job.durationMachineOne + nextJob.start <= counter
//                        if (machineTwoIsFinished && nextJobIsFinishedOnMachienOne) {
//                            machineTwo.add(Schedule(nextJob.job, counter))
//                        }
//                    }
//                }
//
////                if(machineOne.size >= 2 && machineTwo.size >= 2) {
////                    if (!memory.isEmpty() && !currentlyInMemory.isEmpty() &&  machineTwo[machineTwo.size - 2].job.durationMachineTwo + machineTwo[machineTwo.size - 2].start == counter && currentlyInMemory.filter { it.id == machineTwo.last().job.id }.size > 0) {
////                        currentlyUsedMemory -= machineOne[machineTwo.size - 2].job.storageSize
////                        memory.last().end = counter
////                        currentlyInMemory = currentlyInMemory.filter { it.id !=  machineTwo[machineTwo.size - 2].job.id }.toMutableList()
////                    }
////                }
//
//            }
//
//            // wenn der letzte job schon fertig ist
//            if (machineOne.last().start + machineOne.last().job.durationMachineOne <= counter) {
//                // hier muss nochmal genauer nachgeprüft werden, was da schief lief :-(
////                if (machineOne.size >= 2) {
////                    val counterEquals = machineOne.last().job.durationMachineOne + machineOne.last().start == counter
////                    val jobIDNotSame = machineTwo.last().job.id != machineOne.last().job.id
////                    if (counterEquals && jobIDNotSame) {
////                        currentlyUsedMemory += machineOne.last().job.storageSize
////                        memory.add(Memory(currentlyUsedMemory, counter, null))
////                        currentlyInMemory.add(machineOne.last().job)
////                    }
////                }
//
//
//                val lastJob = machineOne.last()
//                for (i in 0 until jobList.size - 1) {
//                    if (jobList[i].id == lastJob.job.id && currentlyUsedMemory < storageSize) {
//                        machineOne.add(Schedule(jobList[i + 1], counter))
//                        break
//                    }
//                }
//            }
//        }
//        counter++
//    }

    return Triple(machineOne, machineTwo, memory)
}

class Schedule(val job: Job, val start: Int) {

    override fun toString(): String {
        return "ID: ${job.id} START: ${start} DURATION_ONE: ${job.durationMachineOne} DURATION_TWO: ${job.durationMachineTwo}"
    }

}

class Memory(val inUse: Int, val start: Int, var end: Int?) {

}