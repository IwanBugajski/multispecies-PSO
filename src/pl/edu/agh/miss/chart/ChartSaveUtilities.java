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
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.Range;
import org.jfree.ui.Size2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;


public class ChartSaveUtilities {
    public static void saveChart(File file, JFreeChart freeChart, int width, int height) throws IOException {
        String fileExtension = Files.getFileExtension(file.getAbsolutePath());
        if (fileExtension.equals("jpg")) {
            writeLegendAsJPG(file, freeChart);

            LegendTitle legend1 = freeChart.getLegend();
            freeChart.removeLegend();
            ChartUtilities.saveChartAsJPEG(file, freeChart, width, height);
            freeChart.addLegend(legend1);
        } else if (fileExtension.equals("pdf")) {
            writeLegendAsPDF(file, freeChart);

            LegendTitle legend1 = freeChart.getLegend();
            freeChart.removeLegend();
            writeChartAsPDF(file, freeChart, width, height);
            freeChart.addLegend(legend1);
        } else {
            throw new UnsupportedOperationException(
                    "Current version of ChartSaveUtilities.saveChart " +
                    "doesnt support " + fileExtension + " files.");
        }
    }

    private static void writeLegendAsPDF(File file, JFreeChart freeChart) throws IOException{
        LegendTitle legend = freeChart.getLegend();
        BufferedImage img = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        Size2D size = legend.arrange(g2, new RectangleConstraint(250.0,
                new Range(0.0, 10000.0)));
        g2.dispose();

        float w = (float) size.width;
        float h = (float) size.height;

        com.itextpdf.text.Rectangle pagesize = new com.itextpdf.text.Rectangle(w, h);
        Document document = new Document(pagesize, 50, 50, 50, 50);
        try {
            String nameWithoutExtension = Files.getNameWithoutExtension(file.getAbsolutePath());
            File fileLegend = new File(file.getParent() + File.separator + nameWithoutExtension + "-legend.pdf");
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileLegend));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate tp = cb.createTemplate(w, h);
            Graphics2D g22 = tp.createGraphics(w, h, new DefaultFontMapper());
            Rectangle2D r2D = new Rectangle2D.Double(0, 0, w, h);
            legend.draw(g22, r2D, null);
            g22.dispose();
            cb.addTemplate(tp, 0, 0);
        }
        catch(DocumentException de) {
            System.err.println(de.getMessage());
        }
        document.close();
    }

    private static void writeLegendAsJPG(File file, JFreeChart freeChart) throws IOException {
        LegendTitle legend = freeChart.getLegend();
        BufferedImage img = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        Size2D size = legend.arrange(g2, new RectangleConstraint(250.0,
                new Range(0.0, 10000.0)));
        g2.dispose();

        // now create an image of the required size for the legend
        int w = (int) Math.rint(size.width);
        int h = (int) Math.rint(size.height);
        BufferedImage img2 = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g22 = img2.createGraphics();
        legend.draw(g22, new Rectangle2D.Double(0, 0, w, h));
        g22.dispose();

        // ...and save it to a PNG image
        String nameWithoutExtension = Files.getNameWithoutExtension(file.getAbsolutePath());
        OutputStream out = new BufferedOutputStream(new FileOutputStream(
                new File(file.getParent() + File.separator + nameWithoutExtension + "-legend.jpg")));
        ChartUtilities.writeBufferedImageAsJPEG(out, img2);
        out.close();
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
