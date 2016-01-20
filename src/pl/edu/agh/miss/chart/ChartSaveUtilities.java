package pl.edu.agh.miss.chart;

import com.google.common.io.Files;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.IOException;

public class ChartSaveUtilities {
    public static void saveChart(File file, JFreeChart freeChart, int width, int height) throws IOException {
        String fileExtension = Files.getFileExtension(file.getAbsolutePath());
        if (fileExtension.equals("jpg")) {
            ChartUtilities.saveChartAsJPEG(file, freeChart, width, height);
        } else {
            throw new UnsupportedOperationException(
                    "Current version of ChartSaveUtilities.saveChart " +
                    "doesnt support " + fileExtension + " files.");
        }
    }
}
