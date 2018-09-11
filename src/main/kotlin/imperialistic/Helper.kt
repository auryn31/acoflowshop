package imperialistic

import java.util.*

object Helper {

    fun distributeColonyWithRoulette(empires: List<Empire>, colony: Country): List<Empire> {
        val empiresMap = Helper.createHashMap(empires)
        val strengthList = empiresMap.keys.sorted().toList()
        val random = Random().nextDouble()
        val key = Helper.findKey(random, strengthList)
        val empireWithNewColony = empiresMap.getOrDefault(key, Empire(Country(mutableListOf())))
        empireWithNewColony.addColony(colony)
        return empires
    }


    private fun createHashMap(empires: List<Empire>): HashMap<Double, Empire> {
        val empiresMap = hashMapOf<Double, Empire>()
        var restStrength = 1.0
        val zeta = 0.1
        val strengthSum = empires.map { it.costs }.reduce { acc, d -> acc + d }

        for (empire in empires) {
            empiresMap[restStrength] = empire
            restStrength -= empire.costs / strengthSum
        }
        return empiresMap
    }

    private fun findKey(random: Double, strengthList: List<Double>): Double {
        for (i in 0 until strengthList.size) {
            if (random < strengthList[i]) {
                return strengthList[i]
            }
        }
        return strengthList.first()
    }

}