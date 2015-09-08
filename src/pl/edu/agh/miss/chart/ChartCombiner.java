package pl.edu.agh.miss.chart;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class ChartCombiner {
	private static final int basicWidth = 800;
	private static final int basicHeight = 600;
	
	@SuppressWarnings("rawtypes")
	public static void combine(Chart mainChart, Map<String, Chart> secondaryCharts) throws Exception{
		//save temporary data
		File mainChartFile = new File("results/tmp/mainChart.jpg");
		mainChart.setHeight(basicHeight).setWidth(basicWidth).save(mainChartFile);
		BufferedImage mainChartImage = ImageIO.read(mainChartFile);
		
		List<BufferedImage> secondaryChartImages = new ArrayList<BufferedImage>();
		
		for(String name : secondaryCharts.keySet()){
			Chart secondaryChart = secondaryCharts.get(name);
			
			File secondaryFileChart = new File("results/tmp/secondary_" + name + ".jpg");
			secondaryChart.setHeight(basicHeight / 2).setWidth(basicWidth / secondaryCharts.size()).save(secondaryFileChart);
			BufferedImage secondartChartImage = ImageIO.read(secondaryFileChart);
			secondaryChartImages.add(secondartChartImage);
		}
		
		BufferedImage resultImage = new BufferedImage(basicWidth, basicHeight + basicHeight / 2, mainChartImage.getType());
		resultImage.createGraphics().drawImage(mainChartImage, 0, 0, null);
		
		for(int i = 0; i < secondaryChartImages.size(); i++){
			resultImage.createGraphics().drawImage(secondaryChartImages.get(i), i * basicWidth / secondaryChartImages.size(), basicHeight, null);
		}
		
		
		
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
		File resultFile = new File("results/combined/chart" + "_" + timeStamp + ".jpg");
		System.out.println("Combined chart saved at: " + resultFile.getAbsolutePath());
		ImageIO.write(resultImage, "jpeg", resultFile);
	}
}
