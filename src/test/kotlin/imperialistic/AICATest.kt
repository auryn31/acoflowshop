package imperialistic

import acoflowshop.Job
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class AICATest {

    @Test
    fun assimilateTest() {
        val empireList = mutableListOf(
                Job(1, 1, 1, 0, 1, 1, 1, 1),
                Job(1, 1, 1, 1, 1, 1, 1, 1),
                Job(1, 1, 1, 2, 1, 1, 1, 1))
        val jobList = mutableListOf(
                Job(1, 1, 1, 1, 1, 1, 1, 1),
                Job(1, 1, 1, 2, 1, 1, 1, 1),
                Job(1, 1, 1, 0, 1, 1, 1, 1))
        val country = Country(empireList)
        val empire = Empire(country)
        empire.setColony(listOf(Country(jobList)))
        val empires = mutableListOf(
                empire
        )
        val newEmpires = AICA.assimilate(empires)
        assertEquals(2, newEmpires[0].getNumberOfColonies())
    }

    @Test
    fun assimilateTest2() {

        mockkObject(AICA)
        every { AICA.createNewCandidateArray(any(), any()) } returns listOf(0, 1, 1, 0, 0, 0)

        val empireList = mutableListOf(
                Job(1, 1, 1, 4),
                Job(1, 1, 1, 5),
                Job(1, 1, 1, 1),
                Job(1, 1, 1, 2),
                Job(1, 1, 1, 6),
                Job(1, 1, 1, 3))
        val jobList = mutableListOf(
                Job(1, 1, 1, 1),
                Job(1, 1, 1, 3),
                Job(1, 1, 1, 6),
                Job(1, 1, 1, 5),
                Job(1, 1, 1, 4),
                Job(1, 1, 1, 2))
        val resultList = mutableListOf(
                Job(1, 1, 1, 3),
                Job(1, 1, 1, 5),
                Job(1, 1, 1, 1),
                Job(1, 1, 1, 6),
                Job(1, 1, 1, 4),
                Job(1, 1, 1, 2))
        val country = Country(empireList)
        val empire = Empire(country)
        empire.setColony(listOf(Country(jobList)))
        val empires = mutableListOf(
                empire
        )
        val newEmpires = AICA.assimilate(empires)
        assertEquals(resultList, newEmpires[0].getColony(1).representation)
    }


    @Test
    fun exchangePositionsTestWithChange() {
        val empireList = mutableListOf(
                Job(3, 1, 1, 0),
                Job(1, 3, 1, 1),
                Job(2, 1, 1, 2))
        val jobList = mutableListOf(
                Job(1, 3, 1, 1),
                Job(3, 1, 1, 0),
                Job(2, 1, 1, 2))
        val country = Country(empireList)
        val empire = Empire(country)
        empire.setColony(listOf(Country(jobList)))
        val empires = mutableListOf(
                empire
        )
        val newEmpires = AICA.exchangePositions(empires)
        assertEquals(jobList, newEmpires[0].emperor.representation)
        assertNotEquals(empireList, newEmpires[0].emperor.representation)
        assertEquals(empireList, newEmpires[0].getColony(0).representation)
    }

    @Test
    fun exchangePositionsTestWithoutChange() {
        val jobList = mutableListOf(
                Job(3, 1, 1, 0),
                Job(1, 3, 1, 1),
                Job(2, 1, 1, 2))
        val empireList = mutableListOf(
                Job(1, 3, 1, 1),
                Job(3, 1, 1, 0),
                Job(2, 1, 1, 2))
        val country = Country(empireList)
        val empire = Empire(country)
        empire.setColony(listOf(Country(jobList)))
        val empires = mutableListOf(
                empire
        )
        val newEmpires = AICA.exchangePositions(empires)
        assertEquals(empireList, newEmpires[0].emperor.representation)
        assertNotEquals(jobList, newEmpires[0].emperor.representation)
        assertEquals(jobList, newEmpires[0].getColony(0).representation)
    }

    @Test
    fun createEmpiresTest() {
        val empireList = mutableListOf(
                Job(1, 3, 1, 1),
                Job(3, 1, 1, 0),
                Job(2, 1, 1, 2))
        val country1 = Country(empireList)
        val country2 = Country(empireList)
        val country3 = Country(empireList)

        val empires = AICA.createEmpires(listOf(country1, country2, country3))
        assertEquals(1, empires.size)
        assertEquals(1, empires.filter { it.getColonies().isNotEmpty() }.size)
    }

    @Test
    fun createCountries() {
        assertEquals(10, AICA.createCountries(10).size)
    }

    @Test
    fun createEmpiresWith10CountriesTest() {
        val empires = AICA.createEmpires(AICA.createCountries(10))
        assertEquals(2, empires.size)
        assertEquals(8, empires.map { it.getColonies().size }.reduce { acc, i -> acc + i })
    }

    @Test
    fun imperialisticCompetitionTest() {
        val jobList1 = mutableListOf(
                Job(1, 3, 1, 0),
                Job(3, 1, 1, 1),
                Job(2, 1, 1, 2))
        val jobList2 = mutableListOf(
                Job(3, 1, 1, 0),
                Job(1, 3, 1, 1),
                Job(2, 1, 1, 2))
        val empireList = mutableListOf(
                Job(1, 3, 1, 1),
                Job(3, 1, 1, 0),
                Job(2, 1, 1, 2))
        val country = Country(empireList)
        val empire1 = Empire(country)
        empire1.setColony(mutableListOf(Country(jobList2)))
        val empire2 = Empire(country)
        empire2.setColony(mutableListOf(Country(jobList1)))
        val empire3 = Empire(country)
        empire3.setColony(mutableListOf(Country(jobList1)))
        val empires = mutableListOf(
                empire1,
                empire2,
                empire3
        )
        val newEmpires = AICA.imperialisticCompetition(empires)

        assertEquals(0, newEmpires.filter { it == empire1 }[0].getColonies().size)
    }

    @Test
    fun eliminatingPowerlessEmpiresTest() {
        val jobList1 = mutableListOf(
                Job(1, 3, 1, 0),
                Job(3, 1, 1, 1),
                Job(2, 1, 1, 2))
        val jobList2 = mutableListOf(
                Job(3, 1, 1, 0),
                Job(1, 3, 1, 1),
                Job(2, 1, 1, 2))
        val empireList = mutableListOf(
                Job(1, 3, 1, 1),
                Job(3, 1, 1, 0),
                Job(2, 1, 1, 2))
        val country = Country(empireList)
        val empire1 = Empire(country)
        empire1.setColony(mutableListOf(Country(jobList1)))
        val empire2 = Empire(country)
        empire2.setColony(mutableListOf(Country(jobList2)))
        val empire3 = Empire(Country(jobList2))
        val empires = mutableListOf(
                empire1,
                empire2,
                empire3
        )
        val newEmpires = AICA.eliminatingPowerlessEmpires(empires)

        assertEquals(2, newEmpires.size)
        assertEquals(2, newEmpires.sortedByDescending { it.getNumberOfColonies() }[0].getNumberOfColonies())
        assertEquals(1, newEmpires.sortedByDescending { it.getNumberOfColonies() }[1].getNumberOfColonies())
    }

    @Test
    fun stoppingCriteriaIsReachedTest() {
        val empireList = mutableListOf(
                Job(1, 3, 1, 1),
                Job(3, 1, 1, 0),
                Job(2, 1, 1, 2))
        val country = Country(empireList)
        val empire = Empire(country)
        assertTrue(AICA.stoppingCriteriaIsReached(mutableListOf(empire), 1, 100))
    }

    @Test
    fun stoppingCriteriaIsReachedTest2() {
        val empireList = mutableListOf(
                Job(1, 3, 1, 1),
                Job(3, 1, 1, 0),
                Job(2, 1, 1, 2))
        val country = Country(empireList)
        val empire = Empire(country)
        val empire2 = Empire(country)
        assertTrue(AICA.stoppingCriteriaIsReached(mutableListOf(empire, empire2), 100, 100))
    }

    @Test
    fun stoppingCriteriaIsReachedTest3() {
        val empireList = mutableListOf(
                Job(1, 3, 1, 1),
                Job(3, 1, 1, 0),
                Job(2, 1, 1, 2))
        val country = Country(empireList)
        val empire = Empire(country)
        val empire2 = Empire(country)
        assertTrue(AICA.stoppingCriteriaIsReached(mutableListOf(empire, empire2), 101, 100))
    }

    @Test
    fun stoppingCriteriaIsReachedTest4() {
        val empireList = mutableListOf(
                Job(1, 3, 1, 1),
                Job(3, 1, 1, 0),
                Job(2, 1, 1, 2))
        val country = Country(empireList)
        val empire = Empire(country)
        val empire2 = Empire(country)
        assertFalse(AICA.stoppingCriteriaIsReached(mutableListOf(empire, empire2), 99, 100))
    }

    @Test
    fun empireRevolutionTest() {
        val jobListForEmpire = mutableListOf(
                Job(1, 3, 1, 1),
                Job(3, 1, 1, 0),
                Job(2, 1, 1, 2))
        val country = Country(jobListForEmpire)
        val empire = Empire(country)
        val empires = listOf(empire)
        AICA.empireRevolution(empires, 0.5, 0.5)
        assertEquals(empires, empires)
    }

    @Test
    fun empireRevolutionTest1() {
        val jobList1 = mutableListOf(
                Job(1, 3, 1, 0),
                Job(3, 1, 1, 1),
                Job(2, 1, 1, 2))
        val jobList2 = mutableListOf(
                Job(3, 1, 1, 0),
                Job(1, 3, 1, 1),
                Job(2, 1, 1, 2))
        val jobListForEmpire = mutableListOf(
                Job(1, 3, 1, 1),
                Job(3, 1, 1, 0),
                Job(2, 1, 1, 2))
        val country = Country(jobListForEmpire)
        val empire = Empire(country)
        val colonyList = listOf(Country(jobList1), Country(jobList2))
        empire.setColony(colonyList)
        val empires = listOf(empire)
        AICA.empireRevolution(empires, 0.5, 0.5)
        assertNotEquals(colonyList, empires[0].getColonies())
    }

    @Test
    fun colonyRevolutionTest(){
        val jobList1 = mutableListOf(
                Job(1, 3, 1, 0),
                Job(3, 1, 1, 1),
                Job(2, 1, 1, 2))
        val jobList2 = mutableListOf(
                Job(3, 1, 1, 0),
                Job(1, 3, 1, 1),
                Job(2, 1, 1, 2))
        val jobListForEmpire = mutableListOf(
                Job(1, 3, 1, 1),
                Job(3, 1, 1, 0),
                Job(2, 1, 1, 2))
        val country = Country(jobListForEmpire)
        val empire = Empire(country)
        val colonyList = listOf(Country(jobList1), Country(jobList2))
        empire.setColony(colonyList)
        val empires = listOf(empire)
        AICA.colonyRevolution(empires, 0.5)
        assertNotEquals(colonyList, empires[0].getColonies())
    }

    @Test
    fun resetTest(){
        val jobList1 = mutableListOf(
                Job(1, 3, 1, 0),
                Job(3, 1, 1, 1),
                Job(2, 1, 1, 2))
        val jobList2 = mutableListOf(
                Job(3, 1, 1, 0),
                Job(1, 3, 1, 1),
                Job(2, 1, 1, 2))
        val jobListForEmpire = mutableListOf(
                Job(1, 3, 1, 1),
                Job(3, 1, 1, 0),
                Job(2, 1, 1, 2))
        val country = Country(jobListForEmpire)
        val empire = Empire(country)
        val colonyList = listOf(Country(jobList1), Country(jobList2))
        empire.setColony(colonyList)
        val empires = listOf(empire)
        val newColonies = AICA.globalWar(empires, 3)
        assertEquals(3, newColonies.size)
    }

    @Test
    fun optimizeForMCTTest(){
        val jobList = createRandomJobList(50)
        AICA.optimizeForMCT(jobList, 1000, 100)
    }
}