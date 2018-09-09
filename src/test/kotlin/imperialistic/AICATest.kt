package imperialistic

import acoflowshop.Job
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class AICATest {

    @Test
    fun assimilateTest(){
        val empireList = mutableListOf(
                Job(1,1,1,0,1,1,1,1),
                Job(1,1,1,1,1,1,1,1),
                Job(1,1,1,2,1,1,1,1))
        val jobList = mutableListOf(
                Job(1,1,1,1,1,1,1,1),
                Job(1,1,1,2,1,1,1,1),
                Job(1,1,1,0,1,1,1,1))
        val country = Country(empireList)
        val empire = Empire(country)
        empire.setColony(listOf(Country(jobList)))
        val empires = mutableListOf(
                empire
        )
        val newEmpires = AICA.assimilate(empires)
        assertEquals(2, newEmpires[0].getNumerOfColonies())
    }


    @Test
    fun exchangePositionsTestWithChange(){
        val empireList = mutableListOf(
                Job(3,1,1,0),
                Job(1,3,1,1),
                Job(2,1,1,2))
        val jobList = mutableListOf(
                Job(1,3,1,1),
                Job(3,1,1,0),
                Job(2,1,1,2))
        val country = Country(empireList)
        val empire = Empire(country)
        empire.setColony(listOf(Country(jobList)))
        val empires = mutableListOf(
                empire
        )
        val newEmpires = AICA.exchangePositions(empires)
        assertEquals(jobList, newEmpires[0].emperor.getRepresentation())
        assertNotEquals(empireList, newEmpires[0].emperor.getRepresentation())
        assertEquals(empireList, newEmpires[0].getColony(0).getRepresentation())
    }

    @Test
    fun exchangePositionsTestWithoutChange(){
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
        empire.setColony(listOf(Country(jobList)))
        val empires = mutableListOf(
                empire
        )
        val newEmpires = AICA.exchangePositions(empires)
        assertEquals(empireList, newEmpires[0].emperor.getRepresentation())
        assertNotEquals(jobList, newEmpires[0].emperor.getRepresentation())
        assertEquals(jobList, newEmpires[0].getColony(0).getRepresentation())
    }

    @Test
    fun createEmpiresTest(){
        val empireList = mutableListOf(
                Job(1,3,1,1),
                Job(3,1,1,0),
                Job(2,1,1,2))
        val country1 = Country(empireList)
        val country2 = Country(empireList)
        val country3 = Country(empireList)

        val empires = AICA.createEmpires(listOf(country1, country2, country3))
        assertEquals(2, empires.size)
        assertEquals(1, empires.filter { it.getColonies().isNotEmpty() }.size)
    }

    @Test
    fun createCountries() {
        assertEquals(10, AICA.createCountries(10).size)
    }

    @Test
    fun createEmpiresWith10CountriesTest(){
        val empires = AICA.createEmpires(AICA.createCountries(10))
        assertEquals(2, empires.size)
        assertEquals(8, empires.map { it.getColonies().size }.reduce { acc, i ->  acc + i})
    }
}