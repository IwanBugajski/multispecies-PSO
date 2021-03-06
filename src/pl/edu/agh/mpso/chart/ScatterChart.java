package pl.edu.agh.mpso.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.Title;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

public class ScatterChart extends Chart<List<Point>>{
	private Map<String, List<Point>> data;
	private int xAxisLabelSize = 0; // 0 means default size
	private int yAxisLabelSize = 0; // 0 means default size
	private int xAxisValuesSizes = 0; // 0 means default size
	private int yAxisValuesSizes = 0; // 0 means default size

	public ScatterChart(){
		data = new HashMap<String, List<Point>>();
	}

	public ScatterChart(int xAxisLabelSize, int yAxisLabelSize, int xAxisValuesSizes, int yAxisValuesSizes) {
		this();
		this.xAxisLabelSize = xAxisLabelSize;
		this.yAxisLabelSize = yAxisLabelSize;
		this.xAxisValuesSizes = xAxisValuesSizes;
		this.yAxisValuesSizes = yAxisValuesSizes;
	}

	@Override
	public Chart<List<Point>> addSeries(String name, List<Point> values) {
		data.put(name, values);
		return this;
	}
	
	public Chart<List<Point>> addToSeries(String name, Point value){
		if(!data.containsKey(name)){
			data.put(name, new ArrayList<Point>());
		}
		data.get(name).add(value);
		return this;
	}

	@Override
	protected void save(File file) throws IOException {
		//create series
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for(String key : data.keySet()){
			XYSeries series = new XYSeries(key);
			XYSeries deviationSeriesUp = new XYSeries(key + "_deviation1");
			XYSeries deviationSeriesDown = new XYSeries(key + "_deviation2");			
			
			
			for(Point point : data.get(key)){
				series.add(point.x, point.y);
				deviationSeriesUp.add(point.x, point.y + point.deviation);
				deviationSeriesDown.add(point.x, point.y - point.deviation);
			}
			
			dataset.addSeries(series);
			if(standardDeviation) dataset.addSeries(deviationSeriesUp);
			if(standardDeviation) dataset.addSeries(deviationSeriesDown);
		}
		
		JFreeChart chart = ChartFactory.createScatterPlot(title, xTitle, yTitle, dataset);
		chart.getPlot().setBackgroundPaint(Color.WHITE);

		if(subtitles != null) {
			for(Title subtitle : subtitles)
			chart.addSubtitle(subtitle);
		}
		
		if(connectDots){
			XYPlot plot = (XYPlot) chart.getPlot();
	        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	        renderer.setSeriesLinesVisible(0, true);
	        Shape shape  = new Ellipse2D.Double(0,0,3,3);
	        renderer.setBaseShape(shape);
	        plot.setRenderer(renderer);
		}
		
		if(logScale){
			XYPlot plot = (XYPlot) chart.getPlot();
			LogAxis logAxis = new LogAxis(yTitle);
			plot.setRangeAxis(logAxis);
		}
		
		if(integerScale){
			XYPlot plot = (XYPlot) chart.getPlot();
			plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		}
		
		if(standardDeviation){
			Color [] colors = new Color[]{Color.RED, Color.BLUE, Color.GREEN, Color.PINK, Color.YELLOW};
			
			XYPlot plot = (XYPlot) chart.getPlot();
			XYItemRenderer renderer = plot.getRenderer();
			int cnt = data.size() * 3;

			for(int i = 0; i < cnt; i += 3){
				if(i / 3 < colors.length) {
					Paint paint = colors[i / 3];
					Paint semiTransparentPaint = new Color(colors[i / 3].getRed(), colors[i / 3].getGreen(), colors[i / 3].getBlue(), colors[i / 3].getAlpha() / 10);
					renderer.setSeriesPaint(i, paint);
					renderer.setSeriesPaint(i + 1, semiTransparentPaint);
					renderer.setSeriesPaint(i + 2, semiTransparentPaint);
				}
				renderer.setSeriesVisibleInLegend(i + 1, false);
				renderer.setSeriesVisibleInLegend(i + 2, false);
				
				Shape shape = ShapeUtilities.createDiamond(1);
				renderer.setSeriesShape(i + 1, shape);
				renderer.setSeriesShape(i + 2, shape);
			}


		}

		if (xAxisLabelSize > 0) {
			changeXAxisLabelSize(chart, xAxisLabelSize);
		}

		if (yAxisLabelSize > 0) {
			changeYAxisLabelSize(chart, yAxisLabelSize);
		}

		if (xAxisValuesSizes > 0) {
			changeXAxisValuesSize(chart, xAxisValuesSizes);
		}

		if (yAxisValuesSizes > 0) {
			changeYAxisValuesSize(chart, yAxisValuesSizes);
		}

		ChartSaveUtilities.saveChart(file, chart, size[0], size[1]);
	}

	private static void changeXAxisValuesSize(JFreeChart chart, int yAxisValuesSizes) {
		Font xLabelFont = chart.getXYPlot().getDomainAxis().getTickLabelFont();
		Font xIncreasedLabelFont = new Font(xLabelFont.getName(), xLabelFont.getStyle(), yAxisValuesSizes);
		chart.getXYPlot().getDomainAxis().setTickLabelFont(xIncreasedLabelFont);
	}

	private static void changeYAxisValuesSize(JFreeChart chart, int yAxisValuesSizes) {
		Font yLabelFont = chart.getXYPlot().getRangeAxis().getTickLabelFont();
		Font yIncreasedLabelFont = new Font(yLabelFont.getName(), yLabelFont.getStyle(), yAxisValuesSizes);
		chart.getXYPlot().getRangeAxis().setTickLabelFont(yIncreasedLabelFont);
	}

	private static void changeYAxisLabelSize(JFreeChart chart, int yAxisLabelSize) {
		Font yLabelFont = chart.getXYPlot().getRangeAxis().getLabelFont();
		Font yIncreasedLabelFont = new Font(yLabelFont.getName(), yLabelFont.getStyle(), yAxisLabelSize);
		chart.getXYPlot().getRangeAxis().setLabelFont(yIncreasedLabelFont);
	}

	private static void changeXAxisLabelSize(JFreeChart chart, int xAxisLabelSize) {
		Font xLabelFont = chart.getXYPlot().getDomainAxis().getLabelFont();
		Font xIncreasedLabelFont = new Font(xLabelFont.getName(), xLabelFont.getStyle(), xAxisLabelSize);
		chart.getXYPlot().getDomainAxis().setLabelFont(xIncreasedLabelFont);
	}
}
