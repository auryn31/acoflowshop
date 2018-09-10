package imperialistic

import acoflowshop.Job
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class HelperTest {

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
        val newEmpires = Helper.distributeColonyWithRoulette(empires, newCountry)

        assertEquals(jobList, newEmpires[0].getColony(0).getRepresentation())
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
        val newEmpires = Helper.distributeColonyWithRoulette(empires, newCountry)

        assertEquals(jobList, newEmpires.filter { it.getColonies().isNotEmpty() }[0].getColony(0).getRepresentation())
        assertEquals(1, newEmpires.filter { it.getColonies().isNotEmpty() }.size)
    }
}