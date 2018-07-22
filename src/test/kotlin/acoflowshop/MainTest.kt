package acoflowshop

import aco.Ant
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MainTest {

    @Test
    fun initPheromonMatrixTest(){
        val matrix = initEmptyPheromonMatrix(2)
        assertEquals(mutableListOf(mutableListOf(0.5, 0.5), mutableListOf(0.5, 0.5)), matrix)
    }

    @Test
    fun findBestAntTest(){
        val ant1 = Ant()
        ant1.duration = 5
        val ant2 = Ant()
        ant2.jobQue = mutableListOf(
                Job(1,2,1),
                Job(2,1,1)
        )
        ant2.calculateDuration(1)
        val ants = listOf(ant1, ant2)
        val best = findBestAnt(ants)
        assertEquals(4, best!!.duration!!)
    }

    @Test
    fun followJobJJobITestTrue() {
        val ant = Ant()
        ant.jobQue = mutableListOf(
                Job(1,2,1, 0),
                Job(2,1,1, 1)
        )
        assertTrue(followJobJJobI(ant, 0, 1))
    }

    @Test
    fun followJobJJobITestFalse() {
        val ant = Ant()
        ant.jobQue = mutableListOf(
                Job(1,2,1, 0),
                Job(2,1,1, 1),
                Job(2,1,1, 2)
        )
        assertFalse(followJobJJobI(ant, 2, 1))
    }

    @Test
    fun updatePheromoneForAntTestForTwo(){
        val ant = Ant()
        ant.jobQue = mutableListOf(
                Job(1,2,1, 0),
                Job(2,1,1, 1)
        )
        val newMatrix = updatePheromoneForAnt(ant, mutableListOf(mutableListOf(0.5, 0.5), mutableListOf(0.5, 0.5)))
        assertEquals(mutableListOf(mutableListOf(0.25, 0.75), mutableListOf(0.25, 0.25)), newMatrix)
    }

    @Test
    fun updatePheromoneForAntTestForThree(){
        val ant = Ant()
        ant.jobQue = mutableListOf(
                Job(1,2,1, 0),
                Job(2,1,1, 2),
                Job(2,1,1, 1)
        )
        val matrix = initEmptyPheromonMatrix(3)
        val newMatrix = updatePheromoneForAnt(
                ant,
                matrix
        )
        val expected = mutableListOf(
                mutableListOf(0.16666666666666666, 0.16666666666666666, 0.6666666666666666),
                mutableListOf(0.16666666666666666, 0.16666666666666666, 0.16666666666666666),
                mutableListOf(0.16666666666666666, 0.6666666666666666, 0.16666666666666666)
        )
        assertEquals(expected, newMatrix)
    }
}