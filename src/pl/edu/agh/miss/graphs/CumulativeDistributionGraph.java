package pl.edu.agh.miss.graphs;

import pl.edu.agh.miss.chart.Chart;
import pl.edu.agh.miss.chart.Point;
import pl.edu.agh.miss.chart.ScatterChart;
import pl.edu.agh.miss.dao.SimulationResultDAO;
import pl.edu.agh.miss.output.SimulationResult;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class CumulativeDistributionGraph {
    private static final String fitnessFunction = "Ackley";
    private final static int dimensions = 100;
    private final static int iterations = 2000000;
    private final static int totalParticles = 25;


    public static void main(String[] args) throws IOException {
        drawGraph(fitnessFunction);
    }

    private static void drawGraph(String function) throws IOException {
        System.out.println("Getting results");
        SimulationResultDAO dao = SimulationResultDAO.getInstance();
        List<SimulationResult> results = dao.getResults(fitnessFunction, dimensions, iterations, totalParticles);
        dao.close();

        System.out.println("Preparing chart data");

        saveChart(function, createDistributionPoints(results));
    }

    private static List<Point> createDistributionPoints(List<SimulationResult> res) {
        List<SimulationResult> results = new LinkedList<>(res);
        Collections.sort(results, new Comparator<SimulationResult>() {
            @Override
            public int compare(SimulationResult o1, SimulationResult o2) {
                double delta= o1.bestFitness - o2.bestFitness;
                if(delta > 0) return 1;
                if(delta < 0) return -1;
                return 0;
            }
        });
        int countOfPoints = results.size();
        double minFitness = results.get(0).bestFitness;
        double maxFitness = results.get(countOfPoints - 1).bestFitness;

        List<Point> points = new LinkedList<>();
        for (int i = 0; i < countOfPoints; i++) {
            points.add(new Point(results.get(i).bestFitness, (double)(i + 1) / countOfPoints));
        }

        return points;
    }

    private static void saveChart(String function, List<Point> distributionPoints) {
        Chart<List<Point>> chart =
                new ScatterChart()
                        .setTitle("PSO " + function + " cumulative distribution, ")
                        .setXAxisTitle("Fitness")
                        .setYAxisTitle("Probability");

        chart.addSeries("fitness", distributionPoints);

        chart.saveWithDateStamp("partial/cumulativedistribution" + function + "/chart");
    }
}
