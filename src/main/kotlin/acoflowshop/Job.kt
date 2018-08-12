package acoflowshop


class Job(val durationMachineOne: Int, val durationMachineTwo: Int, val storageSize: Int, val id: Int){

    override fun equals(other: Any?): Boolean {
        if(other is Job) {
            return this.durationMachineOne == other.durationMachineOne && this.durationMachineTwo == other.durationMachineTwo && this.storageSize == other.storageSize
        } else {
            return false
        }
    }

    override fun toString(): String {
        return "Job with id: ${this.id}"
    }
}