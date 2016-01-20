package pl.edu.agh.miss.chart;

import com.google.common.io.Files;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ChartSaveUtilities {
    public static void saveChart(File file, JFreeChart freeChart, int width, int height) throws IOException {
        String fileExtension = Files.getFileExtension(file.getAbsolutePath());
        if (fileExtension.equals("jpg")) {
            ChartUtilities.saveChartAsJPEG(file, freeChart, width, height);
        } else if (fileExtension.equals("pdf")) {
            writeChartAsPDF(file, freeChart, width, height);
        } else {
            throw new UnsupportedOperationException(
                    "Current version of ChartSaveUtilities.saveChart " +
                    "doesnt support " + fileExtension + " files.");
        }
    }

    public static void writeChartAsPDF(File file, JFreeChart chart, int width, int height) throws IOException {
        com.itextpdf.text.Rectangle pagesize = new com.itextpdf.text.Rectangle(width, height);
        Document document = new Document(pagesize, 50, 50, 50, 50);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate tp = cb.createTemplate(width, height);
            Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());
            Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);
            chart.draw(g2, r2D, null);
            g2.dispose();
            cb.addTemplate(tp, 0, 0);
        }
        catch(DocumentException de) {
            System.err.println(de.getMessage());
        }
        document.close();
    }
}
