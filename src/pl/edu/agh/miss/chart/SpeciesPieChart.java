package pl.edu.agh.miss.chart;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.Title;
import org.jfree.data.general.DefaultPieDataset;

import pl.edu.agh.miss.particle.species.SpeciesType;

public class SpeciesPieChart extends Chart<Integer>{
	private Map<String, Integer> data;
	
	
	public SpeciesPieChart() {
		data = new HashMap<String, Integer>();
	}
	
	public SpeciesPieChart addSpeciesData(String swarmName, int [] speciesArray){
		setTitle(swarmName);
		int sum = 0;
		
		
		for(int i = 0; i < speciesArray.length; i++){
			if(speciesArray[i] <= 0) continue;
			String speciesName = SpeciesType.values()[i].toString();
			data.put(speciesName, speciesArray[i]);
			
			sum += speciesArray[i];
		}
		
		addSubTitle("Number of particles: " + sum);
		
		return this;
	}
	
	@Override
	protected void save(File file) throws IOException {
		//create data set
		DefaultPieDataset dataset = new DefaultPieDataset();
		for(String key : data.keySet()){
			dataset.setValue(key, data.get(key));
		}
		
		JFreeChart chart = ChartFactory.createPieChart(title, dataset, true, true, false);
		
		if(subtitles != null) {
			for(Title subtitle : subtitles)
			chart.addSubtitle(subtitle);
		}
		
		ChartUtilities.saveChartAsJPEG(file, chart, size[0], size[1]);
	}

	@Override
	public Chart<Integer> addSeries(String name, Integer values) {
		data.put(name, values);
		return this;
	}

}
