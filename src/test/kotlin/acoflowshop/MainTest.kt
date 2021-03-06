package acoflowshop

import aco.ACO
import aco.Ant
import global.fak
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MainTest {

    @Test
    fun initPheromonMatrixTest(){
        val matrix = ACO.initEmptyPheromonMatrix(2)
        assertEquals(mutableListOf(mutableListOf(0.5, 0.5), mutableListOf(0.5, 0.5)), matrix)
    }

    @Test
    fun findBestAntTest(){
        val ant1 = Ant()
        ant1.setDurationForMCT(5.0, 0.05)
        val ant2 = Ant()
        ant2.jobQue = mutableListOf(
                Job(1,2,1, 0),
                Job(2,1,1, 1)
        )
        ant2.calculateDurationWithMCT(1)
        val ants = listOf(ant1, ant2)
        val best = ACO.findBestAntForMCT(ants, 1)
        assertEquals(3.5, best?.getDuationForMTCWithourRecalculation())
    }

    @Test
    fun followJobJJobITestTrue() {
        val ant = Ant()
        ant.jobQue = mutableListOf(
                Job(1,2,1, 0),
                Job(2,1,1, 1)
        )
        assertTrue(ACO.followJobJJobI(ant, 0, 1))
    }

    @Test
    fun followJobJJobITestFalse() {
        val ant = Ant()
        ant.jobQue = mutableListOf(
                Job(1,2,1, 0),
                Job(2,1,1, 1),
                Job(2,1,1, 2)
        )
        assertFalse(ACO.followJobJJobI(ant, 2, 1))
    }

    @Test
    fun testFak6(){
        assertEquals(6, fak(3))
    }

    @Test
    fun testFak24(){
        assertEquals(24, fak(4))
    }

}