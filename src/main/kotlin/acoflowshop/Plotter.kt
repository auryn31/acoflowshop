package acoflowshop

import com.lowagie.text.Document
import com.lowagie.text.Rectangle
import com.lowagie.text.pdf.GrayColor
import com.lowagie.text.pdf.PdfWriter
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.StackedBarChart
import javafx.scene.chart.XYChart
import javafx.stage.Stage
import javax.xml.crypto.Data
import javafx.scene.Scene
import javafx.scene.layout.VBox
import org.jCharts.axisChart.AxisChart
import org.jCharts.chartData.AxisChartDataSet
import org.jCharts.chartData.DataSeries
import org.jCharts.encoders.PNGEncoder
import org.jCharts.properties.*
import org.jCharts.test.TestDataGenerator
import org.jCharts.types.ChartType
import java.io.FileOutputStream
import java.util.*
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil.close
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


object Plotter {
    fun plotResults(results: Pair<List<Schedule>, List<Schedule>>, imageName: String) {

//        val xAxisLabel = arrayOf("1", "2")
//        val xAxisTitle = "Time"
//        val yAxisTitle = "Machines"
//        val title = "Scheduling"
//        val dataSeries = DataSeries(xAxisLabel, xAxisTitle, yAxisTitle, title)
//
////        val machineOne = results.first.map { it.job.durationMachineOne }.map { it.toDouble() }.toDoubleArray()
//
//        val data1 = mutableListOf<DoubleArray>()
//        for(i in 0..results.first.size-1) {
//            val arr = doubleArrayOf(results.first[i].job.durationMachineOne.toDouble(),results.first[i].job.durationMachineOne.toDouble())
//            data1.add(arr)
//        }
//
//
//
////        val data: Array<DoubleArray> = arrayOf(machineOne, machineOne)
//        val legendLabels = arrayOf("a", "b", "3")
//        val paints = TestDataGenerator.getRandomPaints(3)
//        val paints1 = arrayOf(GradientPaint(1f, 2f, Color(1,2,3), 1f, 2f, Color(1,2,3)),
//                GradientPaint(1f, 2f, Color(1,2,3), 1f, 2f, Color(1,2,3)),
//                GradientPaint(1f, 2f, Color(55,180,180), 1f, 2f, Color(55,180,180)))
//        val stackedBarChartProperties = StackedBarChartProperties()
//        val axisChartDataSet = AxisChartDataSet(data1.toTypedArray(), legendLabels, paints1, ChartType.BAR_STACKED, stackedBarChartProperties)
//        dataSeries.addIAxisPlotDataSet(axisChartDataSet)
//
//        val chartProperties = ChartProperties()
//        val axisProperties = AxisProperties(true)
//        val legendProperties = LegendProperties()
//        val axisChart = AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, 800, 350)
//
//        val output = FileOutputStream("$imageName.png")
//
//        PNGEncoder.encode(axisChart, output)



        try {
            val width = 1000
            val height = 200;

            // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
            // into integer pixels
            val bi = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            val ig2 = bi.createGraphics();

            val colors = arrayOf(Color.BLUE, Color.CYAN, Color.GRAY, Color.MAGENTA, Color.ORANGE, Color.YELLOW, Color.GREEN)

            for (i in 0 until results.first.size) {
                ig2.color = colors[Random().nextInt(colors.size)]
                ig2.drawRect(results.first[i].start*100, 10, results.first[i].job.durationMachineOne*100, 40)
                ig2.fillRect(results.first[i].start*100, 10, results.first[i].job.durationMachineOne*100, 40)
                ig2.drawRect(results.second[i].start*100, 60, results.first[i].job.durationMachineTwo*100, 40)
                ig2.fillRect(results.second[i].start*100, 60, results.first[i].job.durationMachineTwo*100, 40)
            }


//            val font = Font("TimesRoman", Font.BOLD, 20);
//            ig2.setFont(font);
//            val message = "www.java2s.com!";
//            val fontMetrics = ig2.getFontMetrics();
//            val stringWidth = fontMetrics.stringWidth(message);
//            val stringHeight = fontMetrics.getAscent();
//            ig2.setPaint(Color.black);
//            ig2.drawString(message, (width - stringWidth) / 2, height / 2 + stringHeight / 4);
//            ig2.drawRect(10, 10, 20, 30)
//            ig2.color = Color.BLUE
//            ig2.fillRect(10, 10, 20, 30)
//            val shape = java.awt.Rectangle()
//            shape.height = 10
//            shape.x = 20
//            shape.y = 20
//            shape.width = 100
//            ig2.draw(shape)

            ImageIO.write(bi, "PNG", File("$imageName.png"));

        } catch (ie: IOException) {
            ie.printStackTrace();
        }

    }
}