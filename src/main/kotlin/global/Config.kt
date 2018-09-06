package global

class Config(
        val evaporation: Double,
        val Q: Int,
        val antFactor: Double,
        val dbLogging: Boolean,
        val fileLogging: Boolean) {
    override fun toString(): String {
        return "Config($evaporation, $Q, $antFactor, $dbLogging, $fileLogging)"
    }
}