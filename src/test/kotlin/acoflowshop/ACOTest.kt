package acoflowshop

import aco.ACO
import aco.Ant
import org.junit.Test
import kotlin.test.assertEquals

class ACOTest {

    @Test
    fun updatePheromoneForAntTestForTwo(){
        val ant = Ant()
        ant.jobQue = mutableListOf(
                Job(1,2,1, 0),
                Job(2,1,1, 1)
        )
        val newMatrix = ACO.updateJobPosPheromoneForAnt(ant, mutableListOf(mutableListOf(0.5, 0.5), mutableListOf(0.5, 0.5)), 0.05)
        assertEquals(mutableListOf(mutableListOf(0.525, 0.475), mutableListOf(0.475, 0.525)), newMatrix)
    }

    @Test
    fun updatePheromoneForAntTestForThree(){
        val ant = Ant()
        ant.jobQue = mutableListOf(
                Job(1,2,1, 0),
                Job(2,1,1, 2),
                Job(2,1,1, 1)
        )
        val matrix = ACO.initEmptyPheromonMatrix(3)
        val newMatrix = ACO.updateJobPosPheromoneForAnt(
                ant,
                matrix,
                0.5
        )
        val expected = mutableListOf(
                mutableListOf(0.6666666666666666, 0.16666666666666666, 0.16666666666666666),
                mutableListOf(0.16666666666666666, 0.16666666666666666, 0.6666666666666666),
                mutableListOf(0.16666666666666666, 0.6666666666666666, 0.16666666666666666)
        )
        assertEquals(expected, newMatrix)
    }

    @Test
    fun updatePheromoneForJobJob(){
        val ant = Ant()
        ant.jobQue = mutableListOf(
                Job(1,2,1, 0),
                Job(2,1,1, 2),
                Job(2,1,1, 1)
        )
        val matrix = ACO.initEmptyPheromonMatrix(3)
        val newMatrix = ACO.updateJobJobPheromoneForAnt(
                ant,
                matrix,
                0.5
        )
        val expected = mutableListOf(
                mutableListOf(0.16666666666666666, 0.16666666666666666, 0.6666666666666666),
                mutableListOf(0.6666666666666666, 0.16666666666666666, 0.16666666666666666),
                mutableListOf(0.16666666666666666, 0.6666666666666666, 0.16666666666666666)
        )
        assertEquals(expected, newMatrix)
    }

    @Test
    fun updatePheromoneForAntJobJobTestForTwo(){
        val ant = Ant()
        ant.jobQue = mutableListOf(
                Job(1,2,1, 0),
                Job(2,1,1, 1)
        )
        val newMatrix = ACO.updateJobJobPheromoneForAnt(ant, mutableListOf(mutableListOf(0.5, 0.5), mutableListOf(0.5, 0.5)), 0.05)
        assertEquals(mutableListOf(mutableListOf(0.475, 0.525), mutableListOf(0.525, 0.475)), newMatrix)
    }
}