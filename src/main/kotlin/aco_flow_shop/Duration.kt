package aco_flow_shop

import kotlin.math.max

fun duration(storageSize: Number, jobsList: List<Job>) {
    var jobs = jobsList.sortedBy { it.durationMachineOne }
    var machineOne: List<Job> = emptyList()
    val machineTwo: List<Job> = emptyList()
    
}

fun findBestOrderForJobOne(machineList: List<Job>, jobToAdd: Job, storageSize: Int): List<Job>{
    if(machineList.isEmpty()) {
        return listOf(jobToAdd)
    }
    var shortestList = machineList.toMutableList()
    shortestList.add(jobToAdd)
    var shortestSum = calculatefastestSchedule(shortestList, storageSize)

    for(i in 0..machineList.size-1) {
        val left = machineList.subList(0, i)
        val right = machineList.subList(i, machineList.size)
        val currentList = left.toMutableList()
        currentList.add(jobToAdd)
        currentList.addAll(right)
        val currentLength = calculatefastestSchedule(currentList, storageSize)
        if(currentLength < shortestSum) {
            shortestSum = currentLength
            shortestList = currentList
        }
    }
    return shortestList
}

fun calculatefastestSchedule(jobList: List<Job>, storageSize: Int): Int {
    var currentlyUsedMemory = 0
    val machineOne: MutableList<Schedule> = mutableListOf()
    val machineTwo: MutableList<Schedule> = mutableListOf()
    for(i in 0..jobList.size-1) {
        var startIndexOne = if (i == 0) 0 else jobList.subList(0, i).map { it.durationMachineOne }.reduceRight { j, acc -> j + acc }
        if(currentlyUsedMemory + jobList[i].storageSize > storageSize) {
            startIndexOne = machineTwo[machineTwo.size-2].start + machineTwo[machineTwo.size-2].job.durationMachineTwo
            currentlyUsedMemory -= machineTwo.last().job.storageSize
        }
        machineOne.add(Schedule(jobList[i], startIndexOne))
        val startIndexTwoAfterOne = startIndexOne + jobList[i].durationMachineOne
        val startIndexTwoAfterTwo = if(machineTwo.isEmpty()) 0 else machineTwo.last().start + machineTwo.last().job.durationMachineTwo
        machineTwo.add(Schedule(jobList[i], max(startIndexTwoAfterOne, startIndexTwoAfterTwo)))
        if(machineTwo.last().start > machineOne.last().job.durationMachineOne + machineOne.last().start) {
            currentlyUsedMemory += jobList[i].storageSize
        }
    }
    return machineTwo.last().job.durationMachineTwo + machineTwo.last().start
}

class Schedule(val job: Job, val start: Int){

}