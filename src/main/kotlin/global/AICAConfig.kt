package global

data class AICAConfig(
        val maxIterations: Int,
        val popSize: Int,
        val numberOfEmpires: Int,
        val P_as: Double,
        val P_r: Double,
        val P_ir: Double,
        val P_cr: Double,
        val I_gw: Int,
        val N_GW: Int,
        val xi: Double,
        val dbLogging: Boolean,
        val fileLogging: Boolean,
        val numberOfJobs: Int = 50): Config