package pl.edu.agh.miss;

import static pl.edu.agh.miss.Simulation.NUMBER_OF_DIMENTIONS;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_ITERATIONS;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_SKIPPED_ITERATIONS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.jswarm_pso.Neighborhood;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import pl.edu.agh.miss.chart.Chart;
import pl.edu.agh.miss.chart.ChartCombiner;
import pl.edu.agh.miss.chart.Point;
import pl.edu.agh.miss.chart.ScatterChart;
import pl.edu.agh.miss.chart.SpeciesPieChart;
import pl.edu.agh.miss.multidimensional.RastriginFunction;
import pl.edu.agh.miss.particle.species.SpeciesType;
import pl.edu.agh.miss.swarm.MultiSwarm;
import pl.edu.agh.miss.swarm.SwarmInformation;

@SuppressWarnings("rawtypes")
public class Comparison {
	private static final int EXECUTIONS = 1;
	private static Map<String, Chart> pieCharts = new TreeMap<String, Chart>();
	private static Map<String, Map<Integer, List<Double>>> results = new TreeMap<String, Map<Integer,List<Double>>>();
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		for(int i = 0; i < EXECUTIONS; i++){
			System.out.println("Execution " + (i+1) + " of " + EXECUTIONS);
			run("Standard", new int[] {24});
			//run("Swarm 2", new int[] {14, 5, 5});
			run("Multispecies", new int[] {4, 4, 0, 4, 2, 2, 2, 6});
			//run("Swarm 3", new int[] {6, 4, 4, 4, 0, 0, 0, 4});
			//run("Swarm 3", new int[] {2, 4, 0, 4, 2, 2, 2, 8});
			//run("Swarm 3", new int[] {5, 5, 0, 4, 2, 2, 2, 4});
		}
		
		Chart chart = new ScatterChart().setTitle("PSO Ristrigin optimizing, " + NUMBER_OF_DIMENTIONS + " dimensions, " + NUMBER_OF_ITERATIONS + " iterations").
				setXAxisTitle("Iterations").setYAxisTitle("Fitness").addSubTitle("" + EXECUTIONS + " executions");
		
		for(String swarmName : results.keySet()){
			List<Point> points = new ArrayList<Point>();
			for(int key : results.get(swarmName).keySet()){
				List<Double> values = results.get(swarmName).get(key);
				double sum = 0.0;
				
				for(double value : values){
					sum += value;
				}
				
				double average = sum / values.size();
				double standardDeviation = standardDeviation(values, average);
				
				points.add(new Point(key, average, standardDeviation));
			}
			chart.addSeries(swarmName, points);
		}
		
		chart.addStandardDeviation().saveWithDateStamp("raw/chart");
		
		try{
			ChartCombiner.combine(chart, pieCharts);
		} catch (Exception e) {
			
		}
	}
	
	private static double standardDeviation(List<Double> values, double average){
		double sum = 0.0;
		
		for(double value : values){
			sum += Math.pow(average - value, 2.0);
		}
		
		double variance = sum / values.size();
		
		return Math.sqrt(variance);
	}
	
	private static void run(String name, int [] particles){
		//create pie chart
		if(!pieCharts.containsKey(name)){
			Chart<Integer> pieChart = new SpeciesPieChart().addSpeciesData(name, particles);
			pieCharts.put(name, pieChart);
		}
		
		//create particles
		int cnt = 0;
		List<SwarmInformation> swarmInformations = new ArrayList<SwarmInformation>();
		
		for(int i = 0; i < particles.length; i++){
			if(particles[i] != 0){
				cnt += particles[i];
				
				SpeciesType type = SpeciesType.values()[i];
				SwarmInformation swarmInformation = new SwarmInformation(particles[i], type);
				
				swarmInformations.add(swarmInformation);
			}
		}
		
		SwarmInformation [] swarmInformationsArray = new SwarmInformation [swarmInformations.size()]; 
		MultiSwarm multiSwarm = new MultiSwarm(swarmInformations.toArray(swarmInformationsArray), new RastriginFunction());
		
		Neighborhood neighbourhood = new Neighborhood1D(cnt / 5, true);
		multiSwarm.setNeighborhood(neighbourhood);
		
		
		multiSwarm.setNeighborhoodIncrement(0.9);
		multiSwarm.setInertia(0.95);
		multiSwarm.setParticleIncrement(0.9);
		multiSwarm.setGlobalIncrement(0.9);
		
		multiSwarm.setMaxPosition(100);
		multiSwarm.setMinPosition(-100);
		
		for(int i = 0; i < NUMBER_OF_ITERATIONS; ++i) {
			// Evolve swarm
			multiSwarm.evolve();
			if(i % 1000 == 0 && i > NUMBER_OF_SKIPPED_ITERATIONS) {
				if(!results.containsKey(name)) results.put(name, new HashMap<Integer, List<Double>>());
				if(!results.get(name).containsKey(i)) results.get(name).put(i, new ArrayList<Double>());
				results.get(name).get(i).add(multiSwarm.getBestFitness());
			}
		}
	}

}
