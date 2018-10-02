package global

data class ACOConfig(
        val evaporation: Double,
        val maxIterations: Int,
        val antFactor: Double,
        val dbLogging: Boolean,
        val fileLogging: Boolean,
        val initMatrixWithNEH: Boolean,
        val withEliteSolution: Boolean) {
    override fun toString(): String {
        return "ACOConfig($evaporation, $maxIterations, $antFactor, $dbLogging, $fileLogging)"
    }
}