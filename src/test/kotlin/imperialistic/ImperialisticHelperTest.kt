package imperialistic

import acoflowshop.Job
import org.junit.Test
import kotlin.test.assertEquals

class ImperialisticHelperTest {

    @Test
    fun testDistributeColonyWithRoulette(){
        val jobList = mutableListOf(
                Job(3,1,1,0),
                Job(1,3,1,1),
                Job(2,1,1,2))
        val empireList = mutableListOf(
                Job(1,3,1,1),
                Job(3,1,1,0),
                Job(2,1,1,2))
        val country = Country(empireList)
        val empire = Empire(country)
        val newCountry = Country(jobList)
//        empire.setColony(listOf(Country(jobList)))
        val empires = mutableListOf(
                empire
        )
        val newEmpires = ImperialisticHelper.distributeColonyWithRoulette(empires, newCountry)

        assertEquals(jobList, newEmpires[0].getColony(0).representation)
    }


    @Test
    fun testDistributeColonyWithRoulette2(){
        val jobList = mutableListOf(
                Job(3,1,1,0),
                Job(1,3,1,1),
                Job(2,1,1,2))
        val empireList = mutableListOf(
                Job(1,3,1,1),
                Job(3,1,1,0),
                Job(2,1,1,2))
        val country = Country(empireList)
        val empire1 = Empire(country)
        val empire2 = Empire(country)
        val empire3 = Empire(country)
        val newCountry = Country(jobList)
        val empires = mutableListOf(
                empire1,
                empire2,
                empire3
        )
        val newEmpires = ImperialisticHelper.distributeColonyWithRoulette(empires, newCountry)

        assertEquals(jobList, newEmpires.filter { it.getColonies().isNotEmpty() }[0].getColony(0).representation)
        assertEquals(1, newEmpires.filter { it.getColonies().isNotEmpty() }.size)
    }

    @Test
    fun testCreateHashMap(){
        val empireList = mutableListOf(
                Job(1,3,1,1),
                Job(3,1,1,0),
                Job(2,1,1,2))
        val country = Country(empireList)
        val empire1 = Empire(country)
        val empires = mutableListOf(
                empire1
        )
        assertEquals(hashMapOf(Pair(1.0, empire1)), ImperialisticHelper.createHashMap(empires))
    }

    @Test
    fun testCreateHashMap2(){
        val empireList = mutableListOf(
                Job(1,3,1,1),
                Job(3,1,1,0),
                Job(2,1,1,2))
        val country = Country(empireList)
        val empire1 = Empire(country)
        val empire2 = Empire(country)
        val empires = mutableListOf(
                empire1,
                empire2
        )
        assertEquals(hashMapOf(Pair(1.0, empire1), Pair(0.8125, empire2)), ImperialisticHelper.createHashMap(empires))
    }
}