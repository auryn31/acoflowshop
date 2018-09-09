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
        empire.colonies = mutableListOf(Country(jobList))
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
        empire.colonies = mutableListOf(Country(jobList))
        val empires = mutableListOf(
                empire
        )
        val newEmpires = AICA.exchangePositions(empires)
        assertEquals(jobList, newEmpires[0].emperor.jobList)
        assertNotEquals(empireList, newEmpires[0].emperor.jobList)
        assertEquals(empireList, newEmpires[0].colonies[0].jobList)
    }

    @Test
    fun exchangePositionsTestWothoutChange(){
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
        empire.colonies = mutableListOf(Country(jobList))
        val empires = mutableListOf(
                empire
        )
        val newEmpires = AICA.exchangePositions(empires)
        assertEquals(empireList, newEmpires[0].emperor.jobList)
        assertNotEquals(jobList, newEmpires[0].emperor.jobList)
        assertEquals(jobList, newEmpires[0].colonies[0].jobList)
    }
}