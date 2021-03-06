package acoflowshop


data class Job(
        val durationMachineOne: Int,
        val durationMachineTwo: Int,
        val storageSize: Int = 0,
        val id: Int,
        val setupTimeMachineOne: Int = 0,
        val setupTimeMachineTwo: Int = 0,
        val reworktimeMachineOne: Int = 0,
        val reworktimeMachineTwo: Int = 0,
        val probabilityOfRework: Double = 0.0
) {
    private val hashCode = super.hashCode()

    override fun equals(other: Any?): Boolean {
        var returnValue = false
        if (other is Job) {
            returnValue = this.id == other.id
        }
        return returnValue
    }

    override fun toString(): String {
        return "Job(${durationMachineOne}, ${durationMachineTwo}, ${storageSize}, ${this.id})"
    }

    override fun hashCode(): Int {
        return this.hashCode
    }
}