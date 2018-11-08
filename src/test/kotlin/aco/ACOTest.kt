package aco

import acoflowshop.Job
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import global.ACOConfig
import global.Helper
import global.Heuristik
import javassist.NotFoundException
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ACOTest {

    @Test
    fun updatePheromoneForAntTestForTwo() {
        val ant = Ant()
        ant.jobQue = mutableListOf(
                Job(1, 2, 1, 0),
                Job(2, 1, 1, 1)
        )
        val newMatrix = ACO.updateJobPosPheromoneForAnt(ant, mutableListOf(mutableListOf(0.5, 0.5), mutableListOf(0.5, 0.5)), 0.05)
        assertEquals(mutableListOf(mutableListOf(0.525, 0.475), mutableListOf(0.475, 0.525)), newMatrix)
    }

    @Test
    fun testOptimize() {
        val jobList: List<Job> = Helper.readJobListFromFile("100Jobs").subList(0, 40)
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val acoConfig = mapper.readValue(File("src/main/resources/ACOConfig.json"), ACOConfig::class.java)!!
        ACO.optimize(jobList, acoConfig)
    }

    @Test
    fun `test optimization for heuristic`() {
        val jobList: List<Job> = Helper.readJobListFromFile("100Jobs").subList(0, 20)
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val acoConfig = mapper.readValue(File("src/test/resources/TestConfig2.json"), ACOConfig::class.java) ?: throw NotFoundException("Could not found config")
        ACO.optimize(jobList, acoConfig)
    }

    @Test
    fun updatePheromoneForAntTestForThree() {
        val ant = Ant()
        ant.jobQue = mutableListOf(
                Job(1, 2, 1, 0),
                Job(2, 1, 1, 2),
                Job(2, 1, 1, 1)
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
    fun updatePheromoneForJobJob() {
        val ant = Ant()
        ant.jobQue = mutableListOf(
                Job(1, 2, 1, 0),
                Job(2, 1, 1, 2),
                Job(2, 1, 1, 1)
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
    fun updatePheromoneForAntJobJobTestForTwo() {
        val ant = Ant()
        ant.jobQue = mutableListOf(
                Job(1, 2, 1, 0),
                Job(2, 1, 1, 1)
        )
        val newMatrix = ACO.updateJobJobPheromoneForAnt(ant, mutableListOf(mutableListOf(0.5, 0.5), mutableListOf(0.5, 0.5)), 0.05)
        assertEquals(mutableListOf(mutableListOf(0.475, 0.525), mutableListOf(0.525, 0.475)), newMatrix)
    }
}