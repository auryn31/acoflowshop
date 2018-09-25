package imperialistic

import acoflowshop.Job
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import global.AICAConfig
import global.Helper
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.mockkObject
import org.junit.Before
import org.junit.Test
import java.io.File
import java.util.*
import kotlin.test.*

class AICATest {

    var config: AICAConfig? = null
    var aica: AICA? = null

    @Before
    fun initTests(){
        val mapper = ObjectMapper().registerModule(KotlinModule())
        this.config = mapper.readValue(File("src/test/resources/AICATestConfig.json"), AICAConfig::class.java)
        this.aica = AICA(config!!)
    }

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
        val newEmpires = aica!!.assimilate(empires)
        assertEquals(1, newEmpires[0].getNumberOfColonies())
    }

    @Test
    fun assimilateTest2() {
        val aica = AICA(config!!)
        mockkObject(aica)
        every { aica.createNewCandidateArray(any(), any()) } returns listOf(0, 1, 1, 0, 0, 0)

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
        val newEmpires = aica.assimilate(empires)
        assertEquals(resultList, newEmpires[0].getColony(0).representation)
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
        val newEmpires = aica!!.exchangeEmperorPositionIfThereIsAnBetterCountry(empires)
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
        val newEmpires = aica!!.exchangeEmperorPositionIfThereIsAnBetterCountry(empires)
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

        val empires = aica!!.createEmpires(listOf(country1, country2, country3))
        assertEquals(3, empires.size)
        assertEquals(0, empires.filter { it.getColonies().isNotEmpty() }.size)
    }

    @Test
    fun createCountries() {
        assertEquals(10, aica!!.createCountries(10).size)
    }

    @Test
    fun createEmpiresWith10CountriesTest() {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val thisConfig = mapper.readValue(File("src/test/resources/createEmpiresWith10CountriesTest.json"), AICAConfig::class.java)
        aica = AICA(thisConfig)
        val empires = aica!!.createEmpires(aica!!.createCountries(10))
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
        val newEmpires = aica!!.imperialisticCompetition(empires)
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
        val newEmpires = aica!!.eliminatingPowerlessEmpires(empires)

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
        assertTrue(aica!!.stoppingCriteriaIsReached(mutableListOf(empire), 1, 100))
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
        assertTrue(aica!!.stoppingCriteriaIsReached(mutableListOf(empire, empire2), 100, 100))
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
        assertTrue(aica!!.stoppingCriteriaIsReached(mutableListOf(empire, empire2), 101, 100))
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
        assertFalse(aica!!.stoppingCriteriaIsReached(mutableListOf(empire, empire2), 99, 100))
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
        aica!!.empireRevolution(empires)
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
        aica!!.empireRevolution(empires)
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
        aica!!.colonyRevolution(empires)
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
        val newColonies = aica!!.globalWar(empires, 3)
        assertEquals(3, newColonies.size)
    }

    @Test
    fun optimizeForMCTTest(){
        val jobList = Helper.createRandomJobList(50)
        aica!!.optimizeForMCT(jobList) // 1000, 100
    }
}