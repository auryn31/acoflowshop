package global

data class ACOConfig(val evaporation: Double,
                     val maxIterations: Int,
                     val antFactor: Double,
                     val dbLogging: Boolean,
                     val fileLogging: Boolean,
                     val initMatrixWithNEH: Boolean,
                     val withEliteSolution: Double): Config