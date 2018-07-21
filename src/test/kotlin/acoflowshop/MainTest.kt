package acoflowshop

import aco.Ant
import org.junit.Test
import kotlin.test.assertEquals

class MainTest {

    @Test
    fun initPheromonMatrixTest(){
        val matrix = initEmptyPheromonMatrix(2, 1.0)
        assertEquals(mutableListOf(mutableListOf(1.0, 1.0), mutableListOf(1.0, 1.0)), matrix)
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
}