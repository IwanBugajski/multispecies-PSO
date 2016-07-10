package pl.edu.agh.mpso;

import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Neighborhood;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import pl.edu.agh.mpso.fitness.Rastrigin;
import pl.edu.agh.mpso.species.SpeciesType;
import pl.edu.agh.mpso.swarm.MultiSwarm;
import pl.edu.agh.mpso.swarm.SwarmInformation;
import pl.edu.agh.mpso.transition.order.BestLocalOrder;
import pl.edu.agh.mpso.transition.shift.RandomShift;

public class Example {

	public static void main(String[] args) {
		final int ITERATIONS = 1000;
		
		FitnessFunction fitnessFunction = new Rastrigin();
		
		SwarmInformation [] swarmInfos = new SwarmInformation[3];
		swarmInfos[0] = new SwarmInformation(10, SpeciesType.ALL);
		swarmInfos[1] = new SwarmInformation(5, SpeciesType.GLOBAL_AND_LOCAL);
		swarmInfos[2] = new SwarmInformation(5, SpeciesType.RANDOM);
		
		MultiSwarm swarm = new MultiSwarm(swarmInfos, fitnessFunction);
		
		swarm.setNeighborhood(new Neighborhood1D(3, true));
		
		swarm.setMinPosition(-5.12);
		swarm.setMaxPosition(5.12);
		
		swarm.setOrderFunction(new BestLocalOrder());
		swarm.setShiftFunction(new RandomShift());
		
		for(int i = 0; i < ITERATIONS; i++){
			swarm.evolve();
		}
		
		swarm.getBestFitness();
		swarm.getBestPosition();
	}

}
