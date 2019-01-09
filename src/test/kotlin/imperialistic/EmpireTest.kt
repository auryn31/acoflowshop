package imperialistic

import acoflowshop.Job
import org.junit.Test
import kotlin.test.assertEquals

class EmpireTest {

    @Test
    fun getTotalCostTest() {
        val empireList = mutableListOf(
                Job(1, 1, 1, 0))
        val country = Country(empireList)
        val empire = Empire(country, 0.48)
        assertEquals(2.0, empire.getTotalCost())
    }

    @Test
    fun getTotalCostTestWithColonies() {
        val empireList = mutableListOf(
                Job(1, 1, 1, 0))
        val coloniesList = mutableListOf(Country(mutableListOf(Job(1, 1, 1, 0))))
        val country = Country(empireList)
        val empire = Empire(country, 0.48)
        empire.setColony(coloniesList)
        assertEquals(4.0, empire.getTotalCost())
    }

    @Test
    fun removeColonyTest() {
        val empireList = mutableListOf(
                Job(1, 1, 1, 0))
        val coloniesList = mutableListOf(
                Country(mutableListOf(Job(1, 1, 1, 0))),
                Country(mutableListOf(Job(1, 1, 1, 1))))
        val country = Country(empireList)
        val empire = Empire(country, 0.48)
        empire.setColony(coloniesList)
        val weakestCountry = empire.getColonies().sortedBy { it.getCost() }[0]

        empire.removeColony(weakestCountry)
        assertEquals(1, empire.getColonies().size)
    }

    @Test
    fun removeWeakerColonyTest() {
        val weakerColony = Country(mutableListOf(
                Job(3,1,1,0),
                Job(1,3,1,1),
                Job(2,1,1,2)))
        val strongerColony = Country(mutableListOf(
                Job(1,3,1,1),
                Job(3,1,1,0),
                Job(2,1,1,2)))

        val empireList = mutableListOf(
                Job(1, 1, 1, 0))
        val coloniesList = mutableListOf(
                weakerColony, strongerColony)
        val country = Country(empireList)
        val empire = Empire(country, 0.48)
        empire.setColony(coloniesList)
        val weakestCountry = empire.getColonies().sortedByDescending { it.getCost() }[0]

        empire.removeColony(weakestCountry)
        assertEquals(strongerColony, empire.getColonies()[0])
    }
}