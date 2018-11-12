package global

data class ACOConfig(val evaporation: Double,
                     val maxIterations: Int,
                     val ants: Int,
                     val dbLogging: Boolean = false,
                     val fileLogging: Boolean = true,
                     val initMatrixWithNEH: Boolean = false,
                     val withEliteSolution: Double = 0.0,
                     val beta: Double = 0.0,
                     val heuristic: Heuristik = Heuristik.NONE): Config


/*
    SAME_JOB_LENGTH -> die selbe länge von Job n und Job n-1 auf maschine 1
    SAME_LENGTH_ON_DIFFERENT_MACHINES -> die selbe länge von job n-1 auf maschine 2 und job n auf maschine 1
 */
enum class Heuristik {NONE, SAME_JOB_LENGTH, SAME_LENGTH_ON_DIFFERENT_MACHINES, SAME_LENGTH_ON_DIFFERENT_MACHINES_WITH_REWORK_AND_SETUP}

