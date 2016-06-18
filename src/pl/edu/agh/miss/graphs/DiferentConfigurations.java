package pl.edu.agh.miss.graphs;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.edu.agh.miss.chart.Chart;
import pl.edu.agh.miss.chart.Point;
import pl.edu.agh.miss.chart.ScatterChart;
import pl.edu.agh.miss.dao.SimulationResultDAO;
import pl.edu.agh.miss.output.SimulationResult;

public class DiferentConfigurations {
    private static final String FITNESS_FUNCTION = "Schwefel";
    private static final String PACKAGE = "pl.edu.agh.miss.fitness";
    private final static int DIMENSIONS = 100;
    private final static int ITERATIONS = 5000;
    private final static int TOTAL_PARTICLES = 25;

    private final static int[][] SPAWN_CONFIGURATIONS = {
    	{25,0,0,0,0,0,0,0},
//        {5, 10, 5, 0, 0, 0, 0, 5},
//        {5, 10, 5, 5, 0, 0, 0, 0},
//        {0, 0, 0, 0, 7, 7, 7, 4},
//        {6, 0, 0, 0, 5, 5, 5, 4},
//        {3, 6, 4, 2, 2, 2, 2, 4},
        
        {0,10,5,5,0,0,0,5},
        {4,7,7,7,0,0,0,0},
        {6,5,5,5,0,0,0,4},
        {0,7,7,7,0,0,0,4},
        {0,6,6,6,1,1,1,4}
        
    };
    private static final int COUNT_OF_PARTIALS = 100;

    public static void main(String[] args) throws IOException {
        generateChartForFitness(FITNESS_FUNCTION, DIMENSIONS, ITERATIONS, TOTAL_PARTICLES, SPAWN_CONFIGURATIONS);
    }

    private static void generateChartForFitness(String fitnessFunction, int dimensions, int iterations,
                                                int totalParticles, int[][] spawnConfigurations) throws IOException {
        System.out.println("Getting results");
        SimulationResultDAO dao = SimulationResultDAO.getInstance();
        List<SimulationResult> results = dao.getResults(PACKAGE + "." + fitnessFunction, dimensions, iterations, totalParticles);
        dao.close();
        System.out.println("Results loaded");

        List<SimulationResult> filteredResult = new LinkedList<SimulationResult>();
        for (SimulationResult result : results) {
            if (meetsCriteria(spawnConfigurations, result)) {
                filteredResult.add(result);
            }
        }

        Map<Integer, List<List<Double>>> dividedResultsByConfiguration =
                divideSimulationResultsAccordingToSpawnConfiguration(spawnConfigurations, filteredResult);

        System.out.println("Preparing chart data");
        
        String path = "thesis/configurations/" + fitnessFunction;
        String suffix = "" + dimensions + "_" + iterations;

        Chart<List<Point>> chart =
                new ScatterChart(22, 22, 16, 16)
                        //.setTitle("PSO " + fitnessFunction + " optimizing, ")
                        .setXAxisTitle("Iterations")
                        .setYAxisTitle("Quality")
                        .setLogScale()
                        .setFileFormat("pdf");
        

		

        for (int i=0; i < spawnConfigurations.length; i++) {
        	String label = i == 0 ? "Classic" : "Swarm " + Integer.toString(i+1);
            List<Point> points = new ArrayList<Point>();

            List<List<Double>> resultsForConfiguration =
                    dividedResultsByConfiguration.get(i);
            
            for (int j = 0; j < COUNT_OF_PARTIALS; j++) {
                double sum = 0;

                for (List<Double> resultPartialList : resultsForConfiguration) {
                    sum += resultPartialList.get(j);
                }

                double normalizeIterationValue = iterations * j / 100;
                double averageValueInPartials = sum / resultsForConfiguration.size();
                points.add(new Point(normalizeIterationValue, averageValueInPartials));
            }

            chart.addSeries(label, points);
        }

        chart.saveWithDateStamp(path + "/chart_" + suffix);
        
        
        
        File csvFile = new File("results/" + path + "/results_" + suffix + ".csv");
		PrintWriter writer = new PrintWriter(csvFile);
		writer.append("Swarm,Average Quality,Standard Deviation\n");
        
		for (int i=0; i < spawnConfigurations.length; i++) {
			String label = i == 0 ? "Classic" : "Swarm " + Integer.toString(i+1);
			List<Double> qualitiesForConfiguration = getQualitiesForConfiguration(spawnConfigurations[i], filteredResult);
			
			double avg = average(qualitiesForConfiguration);
			double stD = standardDeviation(qualitiesForConfiguration, avg);
			writer.append(label + "," + round(avg) + "," + round(stD) + "\n");
		}
		
        writer.close();
    }
    
    private static List<Double> getQualitiesForConfiguration(int [] configuration, List<SimulationResult> filteredResults){
    	List<Double> result = new LinkedList<Double>();
    	
    	for (SimulationResult simulationResult : filteredResults) {
    		final int[] speciesCount = getSpeciesConfiguration(simulationResult);
    		if (Arrays.equals(configuration, speciesCount)) {
    			result.add(simulationResult.bestFitness);
    		}
        }
    	
    	return result;
    }

    private static Map<Integer, List<List<Double>>> divideSimulationResultsAccordingToSpawnConfiguration(
            int[][] spawnConfigurations,
            List<SimulationResult> filteredResult) {

        Map<Integer, List<List<Double>>> dividedSimulationResults = new HashMap<Integer, List<List<Double>>>();

        for (int i = 0; i < spawnConfigurations.length; i++) {
        	List<List<Double>> resultForConfiguration = getResultsForConfiguration(spawnConfigurations[i], filteredResult);

            dividedSimulationResults.put(i, resultForConfiguration);
        }

        return dividedSimulationResults;
    }
    
    private static List<List<Double>> getResultsForConfiguration(int [] configuration, List<SimulationResult> filteredResult){
    	List<List<Double>> result = new LinkedList<List<Double>>();
    	
    	for (SimulationResult simulationResult : filteredResult) {
    		final int[] speciesCount = getSpeciesConfiguration(simulationResult);
    		if (Arrays.equals(configuration, speciesCount)) {
    			result.add(simulationResult.partial);
    		}
        }
    	
    	return result;
    }

    private static boolean meetsCriteria(int[][] spawnConfigurations, SimulationResult result) {
        final int[] speciesCount = getSpeciesConfiguration(result);
        for (int[] spawnConfiguration : spawnConfigurations) {
            if (Arrays.equals(spawnConfiguration, speciesCount)) {
                return true;
            }
        }
        return false;
    }

    private static int[] getSpeciesConfiguration(SimulationResult result) {
        return new int[]{ result.species1, result.species2, result.species3,
                                     result.species4, result.species5, result.species6,
                                     result.species7, result.species8};
    }
    
    private static double average(List<Double> values){
		double cnt = values.size();
		double sum = 0.0;
		
		for(double value : values){
			sum += value;
		}
		
		return sum / cnt;
	}
	
	private static double standardDeviation(List<Double> values, double average){
		double sum = 0.0;
		
		for(double value : values){
			sum += Math.pow(average - value, 2.0);
		}
		
		double variance = sum / values.size();
		
		return Math.sqrt(variance);
	}
	
	private static double round(double a){
		return  (double) Math.round(a * 100) / 100;
	}
}
