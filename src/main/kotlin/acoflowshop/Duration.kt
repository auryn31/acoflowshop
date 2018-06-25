package acoflowshop

import kotlin.math.max

fun duration(jobsList: List<Job>, storageSize: Int): Int {
    val jobs = jobsList.sortedByDescending { it.durationMachineOne + it.durationMachineTwo }
    var jobOrder = mutableListOf<Job>()
    for(job in jobs) {
        jobOrder = findBestOrderForNextJob(jobOrder, job, storageSize).toMutableList()
    }
    return calculatefastestScheduleWithOrder(jobOrder, storageSize)
}

fun findBestOrderForNextJob(machineList: List<Job>, jobToAdd: Job, storageSize: Int): List<Job>{
    if(machineList.isEmpty()) {
        return listOf(jobToAdd)
    }
    var shortestList = machineList.toMutableList()
    shortestList.add(jobToAdd)
    var shortestSum = calculatefastestScheduleWithOrder(shortestList, storageSize)

    for(i in 0..machineList.size-1) {
        val left = machineList.subList(0, i)
        val right = machineList.subList(i, machineList.size)
        val currentList = left.toMutableList()
        currentList.add(jobToAdd)
        currentList.addAll(right)
        val currentLength = calculatefastestScheduleWithOrder(currentList, storageSize)
        if(currentLength < shortestSum) {
            shortestSum = currentLength
            shortestList = currentList
        }
    }
    return shortestList
}

fun calculatefastestScheduleWithOrder(jobList: List<Job>, storageSize: Int): Int {
    val schedule = getShortestSchedulePair(jobList, storageSize)
    return schedule.second.last().job.durationMachineTwo + schedule.second.last().start
}

fun getShortestSchedulePair(jobList: List<Job>, storageSize: Int): Pair<List<Schedule>, List<Schedule>> {
    var currentlyUsedMemory = 0
    val machineOne = mutableListOf<Schedule>()
    val machineTwo = mutableListOf<Schedule>()
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
    return Pair(machineOne, machineTwo)
}

class Schedule(val job: Job, val start: Int){

}