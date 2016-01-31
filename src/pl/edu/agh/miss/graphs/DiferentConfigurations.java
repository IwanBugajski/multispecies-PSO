package pl.edu.agh.miss.graphs;

import pl.edu.agh.miss.chart.Chart;
import pl.edu.agh.miss.chart.Point;
import pl.edu.agh.miss.chart.ScatterChart;
import pl.edu.agh.miss.dao.SimulationResultDAO;
import pl.edu.agh.miss.output.SimulationResult;

import java.io.IOException;
import java.util.*;

public class DiferentConfigurations {
    private static final String FITNESS_FUNCTION = "Rastrigin";
    private final static int DIMENSIONS = 100;
    private final static int ITERATIONS = 3000000;
    private final static int TOTAL_PARTICLES = 25;

    private final static int NUMBER_OF_SPECIES = 8;
    private final static int[][] SPAWN_CONFIGURATIONS = {
        {5, 10, 5, 0, 0, 0, 0, 5},
        {5, 10, 5, 5, 0, 0, 0, 0},
        {0, 0, 0, 0, 7, 7, 7, 4},
        {6, 0, 0, 0, 5, 5, 5, 4},
        {3, 6, 4, 2, 2, 2, 2, 4},
    };
    private static final int COUNT_OF_PARTIALS = 100;

    public static void main(String[] args) throws IOException {
        generateChartForFitness(FITNESS_FUNCTION, DIMENSIONS, ITERATIONS, TOTAL_PARTICLES, SPAWN_CONFIGURATIONS);
    }

    private static void generateChartForFitness(String fitnessFunction, int dimensions, int iterations,
                                                int totalParticles, int[][] spawnConfigurations) throws IOException {
        System.out.println("Getting results");
        SimulationResultDAO dao = SimulationResultDAO.getInstance();
        List<SimulationResult> results = dao.getResults(fitnessFunction, dimensions, iterations, totalParticles);
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

        Chart<List<Point>> chart =
                new ScatterChart(18, 18, 14, 14, 14)
                        //.setTitle("PSO " + fitnessFunction + " optimizing, ")
                        .setXAxisTitle("Iterations")
                        .setYAxisTitle("Quality")
                        .setLogScale()
                        .setFileFormat("pdf");

        for (int i=0; i < spawnConfigurations.length; i++) {
            String label = "Swarm " + Integer.toString(i);
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

        String path = "configurations/" + fitnessFunction;
        String suffix = "" + dimensions + "_" + iterations;

        chart.saveWithDateStamp(path + "/chart_" + suffix);
    }

    private static Map<Integer, List<List<Double>>> divideSimulationResultsAccordingToSpawnConfiguration(
            int[][] spawnConfigurations,
            List<SimulationResult> filteredResult) {

        Map<Integer, List<List<Double>>> dividedSimulationResults = new HashMap<>();

        for (int i = 0; i < spawnConfigurations.length; i++) {
            List<List<Double>> resultForConfiguration = new LinkedList<>();
            for (SimulationResult simulationResult : filteredResult) {
                resultForConfiguration.add(simulationResult.partial);
            }

            dividedSimulationResults.put(i, resultForConfiguration);
        }

        return dividedSimulationResults;
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
