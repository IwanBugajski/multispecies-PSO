package pl.edu.agh.mpso;

import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import pl.edu.agh.mpso.chart.Point;
import pl.edu.agh.mpso.chart.ScatterChart;
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
	private final static int INTERVAL = 50;

	private final static String PATH = "/article/example/";
	
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
		
		MultiSwarm classicSwarm = new MultiSwarm(getSwarmInfos(new int[]{25,0,0,0,0,0,0,0}), FITNESS_FUNCTION);
		classicSwarm.setNeighborhood(new Neighborhood1D(5, true));
		classicSwarm.setMaxPosition(5);
		classicSwarm.setMinPosition(-5);
		classicSwarm.setInertia(0.95);
		classicSwarm.init();
		
		ScatterChart fitnessChart = (ScatterChart) new ScatterChart().connectTheDots().setXAxisTitle("Iterations").setYAxisTitle("Fitness");
		ScatterChart shareChart = (ScatterChart) new ScatterChart().setIntegerScale().setXAxisTitle("Iterations").setYAxisTitle("Number of individuals");
		
		for(int i = 0; i < ITERATIONS; i++){
			changingSwarm.evolve();
			staticSwarm.evolve();
			classicSwarm.evolve();
			
			if(i % (INTERVAL / 2) == 0){
				fitnessChart.addToSeries("Changing", new Point(i, changingSwarm.getBestFitness()));
				fitnessChart.addToSeries("Static", new Point(i, staticSwarm.getBestFitness()));
				fitnessChart.addToSeries("Classic", new Point(i, classicSwarm.getBestFitness()));
			}
			
			if(i % (INTERVAL / 2) == 0){
				int[] species = changingSwarm.getSpecies();
				for(int s = 0; s < species.length; s++){
					shareChart.addToSeries(SpeciesType.values()[s].name(), new Point(i, species[s]));
				}
			}
		}
		System.out.println(changingSwarm.getBestFitness());
		System.out.println(staticSwarm.getBestFitness());
		System.out.println(classicSwarm.getBestFitness());
		fitnessChart.setFileFormat("pdf").saveWithDateStamp(PATH + "fitness"); 
		shareChart.setFileFormat("pdf").saveWithDateStamp(PATH + "share");
		shareChart.connectTheDots().setFileFormat("pdf").saveWithDateStamp(PATH + "share_connected");
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
