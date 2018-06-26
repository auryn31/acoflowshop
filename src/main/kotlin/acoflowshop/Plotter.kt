package acoflowshop

import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.math.log
import kotlin.math.log10

object Plotter {
    fun plotResults(results: Pair<List<Schedule>, List<Schedule>>, imageName: String) {

        val shiftRight = 35
        val height = 40

        val jobWidth = results.second.last().job.durationMachineTwo + results.second.last().start

        val factor = when (jobWidth) {
            in 0..10 -> 100
            in 10..20 -> 50
            in 20..30 -> 25
            else -> 12
        }


        try {
            val imageWidth = jobWidth*factor+shiftRight*2
            val imageHeight = 150;

            // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
            // into integer pixels
            val bi = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

            val ig2 = bi.createGraphics();

            val colors = arrayOf(Color.BLUE, Color.ORANGE, Color.CYAN, Color.GREEN, Color.GRAY, Color.MAGENTA, Color.YELLOW)

            val font = Font("TimesRoman", Font.BOLD, 20);
            ig2.setFont(font);

            ig2.setPaint(Color.black);
            ig2.drawString("M1", 5 , height);
            ig2.drawString("M2", 5, height+50);

            for (i in 0 until results.first.size) {
                ig2.color = colors[i%colors.size]
                ig2.drawRect(shiftRight + results.first[i].start*factor, 10, results.first[i].job.durationMachineOne*factor, height)
                ig2.fillRect(shiftRight + results.first[i].start*factor, 10, results.first[i].job.durationMachineOne*factor, height)
                ig2.drawRect(shiftRight + results.second[i].start*factor, 60, results.second[i].job.durationMachineTwo*factor, height)
                ig2.fillRect(shiftRight + results.second[i].start*factor, 60, results.second[i].job.durationMachineTwo*factor, height)
                val message = "${i+1}";
                ig2.setPaint(Color.black)
                ig2.drawString(message, shiftRight+results.first[i].start*factor + results.first[i].job.durationMachineOne*factor/2 , height)
                ig2.drawString(message, shiftRight+results.second[i].start*factor + results.second[i].job.durationMachineTwo*factor/2 , height+50)
            }

            ig2.drawLine(shiftRight, imageHeight-shiftRight, jobWidth*factor + shiftRight, imageHeight-shiftRight)

            val message = "$jobWidth";
            ig2.setPaint(Color.black)
            ig2.drawString(message, jobWidth*factor + shiftRight, imageHeight-shiftRight+8)

            ImageIO.write(bi, "PNG", File("$imageName.png"));

        } catch (ie: IOException) {
            ie.printStackTrace();
        }

    }
}