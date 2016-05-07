package pl.edu.agh.miss.graphs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import pl.edu.agh.miss.chart.Chart;
import pl.edu.agh.miss.chart.Point;
import pl.edu.agh.miss.chart.ScatterChart;
import pl.edu.agh.miss.dao.SimulationResultDAO;
import pl.edu.agh.miss.output.SimulationResult;

public class Distribution {
    private static final String FITNESS_FUNCTION = "Rastrigin";
    private static final String PACKAGE = "pl.edu.agh.miss.fitness";
    private final static int DIMENSIONS = 100;
    private final static int ITERATIONS = 3000000;
    private final static int TOTAL_PARTICLES = 25;

    private final static int[][] SPAWN_CONFIGURATIONS = {
    	{25,0,0,0,0,0,0,0},
    	{3,6,4,2,2,2,2,4}
//    	,
//        {5, 10, 5, 0, 0, 0, 0, 5},
//        {5, 10, 5, 5, 0, 0, 0, 0},
//        {0, 0, 0, 0, 7, 7, 7, 4},
//        {6, 0, 0, 0, 5, 5, 5, 4},
//        {3, 6, 4, 2, 2, 2, 2, 4},
    };

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

        System.out.println("Preparing chart data");
        
        String path = "partial/distribution/" + fitnessFunction;
        String suffix = "" + dimensions + "_" + iterations;

        Chart<List<Point>> chart =
                new ScatterChart(22, 22, 16, 16)
                        //.setTitle("PSO " + fitnessFunction + " optimizing, ")
                        .setXAxisTitle("Quality")
                        .setYAxisTitle("Probability")
                        //.setLogScale()
                        .setFileFormat("pdf");
        

		

        for (int i=0; i < spawnConfigurations.length; i++) {
        	String label = i == 0 ? "Classic" : "Swarm " + Integer.toString(i+1);
            List<Point> points = new ArrayList<Point>();
            List<Double> qualities = getQualitiesForConfiguration(spawnConfigurations[i], filteredResult);
            Collections.sort(qualities);

            for(int j = 0; j < qualities.size(); j++){
            	points.add(new Point(qualities.get(j), ((double)j / (double)(qualities.size() - 1))));
            }

            chart.addSeries(label, points);
        }

        chart.saveWithDateStamp(path + "/chart_" + suffix);
        
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
}
