package global

import org.junit.Test
import java.io.File
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class ACOConfigTest {
    @Test
    fun testConfig() {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        try {
            val config = mapper.readValue(File("src/test/resources/TestConfig.json"), ACOConfig::class.java)
            val expected = ACOConfig(0.05, 1000,0.01, false, false, false, 0.0)
            assertEquals(expected.toString(), config.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            assertTrue(false)
        }
    }

    @Test
    fun testConfig2() {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        try {
            val config = mapper.readValue(File("src/test/resources/TestConfig.json"), ACOConfig::class.java)
            assertEquals(0.05, config.evaporation)
            assertEquals(1000, config.maxIterations)
            assertEquals(0.01, config.antFactor)
            assertEquals(0.0, config.beta)
            assertEquals(Heuristik.NONE, config.heuristic)
            assertFalse(config.dbLogging)
            assertFalse(config.fileLogging)
        } catch (e: Exception) {
            e.printStackTrace()
            assertTrue(false)
        }
    }

    @Test
    fun `if there is an beta in config json`() {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        try {
            val config = mapper.readValue(File("src/test/resources/TestConfig2.json"), ACOConfig::class.java)
            assertEquals(0.05, config.evaporation)
            assertEquals(1000, config.maxIterations)
            assertEquals(0.01, config.antFactor)
            assertEquals(0.3, config.beta)
            assertEquals(Heuristik.SAME_JOB_LENGTH, config.heuristic)
            assertFalse(config.dbLogging)
            assertFalse(config.fileLogging)
        } catch (e: Exception) {
            e.printStackTrace()
            assertTrue(false)
        }
    }
}
