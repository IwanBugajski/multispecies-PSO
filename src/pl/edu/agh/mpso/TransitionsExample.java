package pl.edu.agh.mpso;

import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import pl.edu.agh.mpso.chart.Point;
import pl.edu.agh.mpso.chart.ScatterChart;
import pl.edu.agh.mpso.chart.SpeciesPieChart;
import pl.edu.agh.mpso.fitness.Rastrigin;
import pl.edu.agh.mpso.species.SpeciesType;
import pl.edu.agh.mpso.swarm.MultiSwarm;
import pl.edu.agh.mpso.swarm.SwarmInformation;
import pl.edu.agh.mpso.transition.order.BestLocalOrder;
import pl.edu.agh.mpso.transition.order.OrderFunction;
import pl.edu.agh.mpso.transition.shift.ShiftFunction;
import pl.edu.agh.mpso.transition.shift.WorstLocalShift;

public class TransitionsExample {
	private final static FitnessFunction FITNESS_FUNCTION = new Rastrigin();
	private final static OrderFunction ORDER_FUNCTION = new BestLocalOrder();
	private final static ShiftFunction SHIFT_FUNCTION = new WorstLocalShift();

	private final static int ITERATIONS = 800;
	private final static int INTERVAL = 100;

	private final static String PATH = "/thesis2/example/";
	
	public static void main(String[] args) {
		MultiSwarm changingSwarm = new MultiSwarm(getSwarmInfos(new int[]{4,3,3,3,3,3,3,3}), FITNESS_FUNCTION);
		changingSwarm.setOrderFunction(ORDER_FUNCTION);
		changingSwarm.setShiftFunction(SHIFT_FUNCTION);
		changingSwarm.setNeighborhood(new Neighborhood1D(5, true));
		changingSwarm.setMaxPosition(5);
		changingSwarm.setMinPosition(-5);
		changingSwarm.setInertia(0.95);
		changingSwarm.init();
		
		MultiSwarm staticSwarm = new MultiSwarm(getSwarmInfos(new int[]{4,3,3,3,3,3,3,3}), FITNESS_FUNCTION);
		staticSwarm.setNeighborhood(new Neighborhood1D(5, true));
		staticSwarm.setMaxPosition(5);
		staticSwarm.setMinPosition(-5);
		staticSwarm.setInertia(0.95);
		staticSwarm.init();
		
		ScatterChart fitnessChart = (ScatterChart) new ScatterChart().setXAxisTitle("Iterations").setYAxisTitle("Quality");
		
		for(int i = 0; i < ITERATIONS; i++){
			changingSwarm.evolve();
			staticSwarm.evolve();
			
			if(i % (INTERVAL / 10) == 0){
				fitnessChart.addToSeries("Changing", new Point(i, changingSwarm.getBestFitness()));
				fitnessChart.addToSeries("Static", new Point(i, staticSwarm.getBestFitness()));
			}
			
			if(i % INTERVAL == 0){
				
				int[] species = changingSwarm.getSpecies();
				SpeciesPieChart speciesPieChart = new SpeciesPieChart();
				for(int s = 0; s < species.length; s++){
					speciesPieChart.addSeries(SpeciesType.values()[s].name(), species[s]);
				}
				speciesPieChart.setFileFormat("pdf").saveWithDateStamp(PATH + "share_" + i);
			}
		}
		System.out.println(changingSwarm.getBestFitness());
		System.out.println(staticSwarm.getBestFitness());
		fitnessChart.setFileFormat("pdf").saveWithDateStamp(PATH + "fitness"); 
	}

	private static SwarmInformation [] getSwarmInfos(int [] particles){
		SwarmInformation [] infos = new SwarmInformation[particles.length];
		for(int i = 0; i < particles.length; i++){
			SpeciesType type = SpeciesType.values()[i];
			infos[i] = new SwarmInformation(particles[i], type);
		}
		return infos;
	}
}
