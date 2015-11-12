package pl.edu.agh.miss;

import static pl.edu.agh.miss.Simulation.NUMBER_OF_ITERATIONS;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_PARTICLES;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_DIMENSIONS;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jswarm_pso.Neighborhood;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import pl.edu.agh.miss.multidimensional.RastriginFunction;
import pl.edu.agh.miss.particle.species.SpeciesType;
import pl.edu.agh.miss.swarm.MultiSwarm;
import pl.edu.agh.miss.swarm.MultiSwarmParallel;
import pl.edu.agh.miss.swarm.SwarmInformation;

/**
 * 
 * @author iwanb
 * command line args:
 * - number of dimensions
 * - number of iterations
 * - proportional share of 1st species
 * - proportional share of 2nd species
 * - proportional share of 3rd species
 * - proportional share of 4th species
 * - proportional share of 5th species
 * - proportional share of 6th species
 * - proportional share of 7th species
 * - proportional share of 8th species
 */
public class Scalarm {
	public static void main(String[] args) {
		//get number of dimensions
		if(args.length >= 1){
			NUMBER_OF_DIMENSIONS = Integer.valueOf(args[0]);
		}
		//get number of iterations
		if(args.length >= 2){
			NUMBER_OF_ITERATIONS = Integer.valueOf(args[1]);
		}
		
		//create array of species share
		int numberOfSpecies = SpeciesType.values().length;
		int [] speciesArray = new int[numberOfSpecies];
		int argsSum = 0;
		
		for(int i = 2; i < Math.min(numberOfSpecies + 2, args.length); i++){
			argsSum += Integer.valueOf(args[i]);
		}
		
		if(argsSum == 0){
			speciesArray[0] = NUMBER_OF_PARTICLES;
		} else {
			for(int i = 0; i < Math.min(numberOfSpecies, args.length - 2); i++){
				float speciesShare = (float) Integer.valueOf(args[i + 2]) / (float) argsSum;
				speciesArray[i] = (int) (speciesShare * NUMBER_OF_PARTICLES);
			}
		}
		run(speciesArray);
	}

	private static void run(int [] particles){
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
		MultiSwarm multiSwarm = new MultiSwarmParallel(swarmInformations.toArray(swarmInformationsArray), new RastriginFunction());
		
		Neighborhood neighbourhood = new Neighborhood1D(cnt / 5, true);
		multiSwarm.setNeighborhood(neighbourhood);
		
		
		multiSwarm.setNeighborhoodIncrement(0.9);
		multiSwarm.setInertia(0.95);
		multiSwarm.setParticleIncrement(0.9);
		multiSwarm.setGlobalIncrement(0.9);
		
		multiSwarm.setMaxPosition(100);
		multiSwarm.setMinPosition(-100);

		multiSwarm.runSimulation(NUMBER_OF_ITERATIONS);

		System.out.println(multiSwarm.getBestFitness());
	}
}
