package aco_flow_shop


class Job(val durationMachineOne: Int, val durationMachineTwo: Int, val storageSize: Int){


    override fun equals(other: Any?): Boolean {
        if(other is Job) {
            return this.durationMachineOne == other.durationMachineOne && this.durationMachineTwo == other.durationMachineTwo && this.storageSize == other.storageSize
        } else {
            return false
        }
    }
}