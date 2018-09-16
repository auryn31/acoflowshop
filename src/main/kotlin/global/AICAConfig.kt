package global

data class AICAConfig(
        val maxIterations: Int,
        val popSize: Int,
        val empirePercentage: Double,
        val assimilationRate: Double,
        val P_r: Double,
        val P_ir: Double,
        val P_cr: Double,
        val dbLogging: Boolean,
        val fileLogging: Boolean) {
}