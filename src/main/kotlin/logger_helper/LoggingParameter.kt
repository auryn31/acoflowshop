package logger_helper

object LoggingParameter {

    var iteration = 0
    var bestDuration = 0.0
    var currentTime = 0L
    var evaluationIteration = 0
    var reworkTimeInPercentage = 0.0


    fun reset(){
        iteration = 0
        bestDuration = 0.0
        currentTime = 0L
        reworkTimeInPercentage = 0.0
        evaluationIteration = 0
    }
}