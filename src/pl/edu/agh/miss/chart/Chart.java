package pl.edu.agh.miss.chart;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;

public abstract class Chart<T> {
	protected String title, xTitle, yTitle;
	protected int [] size = new int [] {640, 480};
	protected List<Title> subtitles;
	protected boolean standardDeviation = false;
	protected boolean logScale;
	
	public Chart<T> addSubTitle(String subtitle){
		if(subtitles == null) subtitles = new ArrayList<Title>();
		subtitles.add(new TextTitle(subtitle));
		return this;
	}
	
	public Chart<T> setTitle(String title){
		this.title = title;
		return this;
	}
	
	public Chart<T> setXAxisTitle(String title){
		this.xTitle = title;
		return this;
	}
	
	public Chart<T> setYAxisTitle(String title){
		this.yTitle = title;
		return this;
	}
	
	public Chart<T> setWidth(int width){
		size[0] = width;
		return this;
	}
	
	public Chart<T> setHeight(int height){
		size[1] = height;
		return this;
	}
	
	
	public void save(String fileName){
		File file = new File(fileName);
		try {
			file.createNewFile();
			save(file);
			System.out.println("Chart saved at " + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveWithDateStamp(String prefix){
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
		String fileName = "results/" + prefix + "_" + timeStamp + ".jpg";
		save(fileName);
	}
	
	public void save(){
		saveWithDateStamp("chart");
	}
	
	public Chart<T> addStandardDeviation(){
		standardDeviation  = true;
		return this;
	}
	
	public Chart<T> setLogScale(){
		logScale = true;
		return this;
	}
	
	public abstract Chart<T> addSeries(String name, T values);
	
	protected abstract void save(File file) throws IOException;
}
