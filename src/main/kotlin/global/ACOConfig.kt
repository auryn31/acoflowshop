package global

data class ACOConfig(val evaporation: Double,
                     val maxIterations: Int,
                     val antFactor: Double,
                     val dbLogging: Boolean = false,
                     val fileLogging: Boolean = true,
                     val initMatrixWithNEH: Boolean = false,
                     val withEliteSolution: Double = 0.0,
                     val beta: Double = 0.0,
                     val heuristic: Heuristik = Heuristik.NONE): Config


enum class Heuristik {NONE, SAME_JOB_LENGTH}