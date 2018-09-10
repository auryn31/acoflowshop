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

    @Test
    fun imperialisticCompetitionTest(){
        val jobList1 = mutableListOf(
                Job(1,3,1,0),
                Job(3,1,1,1),
                Job(2,1,1,2))
        val jobList2 = mutableListOf(
                Job(3,1,1,0),
                Job(1,3,1,1),
                Job(2,1,1,2))
        val empireList = mutableListOf(
                Job(1,3,1,1),
                Job(3,1,1,0),
                Job(2,1,1,2))
        val country = Country(empireList)
        val empire1 = Empire(country)
        empire1.setColony(mutableListOf(Country(jobList1)))
        val empire2 = Empire(country)
        empire2.setColony(mutableListOf(Country(jobList2)))
        val empire3 = Empire(country)
        empire3.setColony(mutableListOf(Country(jobList2)))
        val empires = mutableListOf(
                empire1,
                empire2,
                empire3
        )
        val newEmpires = AICA.imperialisticCompetition(empires)

        assertEquals(0, newEmpires.filter { it == empire1 }[0].getColonies().size)
    }
}