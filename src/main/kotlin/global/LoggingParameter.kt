package global

object LoggingParameter {

    var iteration = 0
    var bestDuration = 0.0
    var currentTime = 0L
    var evaluationIteration = 0


    fun reset(){
        this.iteration = 0
        this.bestDuration = 0.0
        this.currentTime = 0
    }
}