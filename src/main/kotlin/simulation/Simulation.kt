package simulation

import acoflowshop.Job

interface Simulation<T> {
    fun optimize(jobList: List<Job>, config: T): Pair<List<Job>, Double>
}