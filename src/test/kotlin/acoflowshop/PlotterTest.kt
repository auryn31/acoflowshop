package acoflowshop

import org.junit.Test
import java.io.File
import kotlin.test.assertTrue

class PlotterTest {

    @Test
    fun plotFirstJob(){
        val fileName = "realDataTestPlot"
        val pairs = getShortestSchedulePair(listOf<Job>(
                Job(1,2,1,0),
                Job(3,1,1,1),
                Job(3,1,1,2)
        ), 3)

        Plotter.plotResults(pairs, fileName)
        val file = File("$fileName.png")
        assertTrue(file.delete())
    }

    @Test
    fun plotSecondJob(){
        val fileName = "realDataTestPlot"
        val pairs = getShortestSchedulePair(listOf<Job>(
                Job(1,2,1,0),
                Job(7,1,1,1),
                Job(7,1,1,2),
                Job(3,1,1,3),
                Job(3,1,1,4),
                Job(62,5,1,5),
                Job(6,5,1,6),
                Job(6,5,1,7),
                Job(6,5,1,8),
                Job(3,1,1,9)
        ), 3)

        Plotter.plotResults(pairs, fileName)
        val file = File("$fileName.png")
        assertTrue(file.delete())
    }
}