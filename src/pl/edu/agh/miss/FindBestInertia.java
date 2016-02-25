package pl.edu.agh.miss;

import static pl.edu.agh.miss.Simulation.NUMBER_OF_DIMENSIONS;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_ITERATIONS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Neighborhood;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import pl.edu.agh.miss.fitness.Rastrigin;
import pl.edu.agh.miss.particle.species.SpeciesType;
import pl.edu.agh.miss.swarm.MultiSwarm;
import pl.edu.agh.miss.swarm.SwarmInformation;

public class FindBestInertia {
	private static List<Thread> threads = new ArrayList<Thread>();
	private static Map<Double, Double> results = new HashMap<Double, Double>();
	private static int [] particles = new int[]{40,0,0,0,0,0,0,0};
	private static double maxVelocity = 1.0;
	private static FitnessFunction fitnessFunction = new Rastrigin();
	
	
	public static void main(String [] args) throws InterruptedException{
		NUMBER_OF_DIMENSIONS = 100;
		NUMBER_OF_ITERATIONS = 2000;
		int executions = 15;
		
		for(double i = 0.0; i <= 1.0; i+=0.05){
			runParallel(i, executions);
		}
		
		for(Thread thread : threads){
			thread.join();
		}
		
		System.out.println("\nResults:");
		
		for(double i = 0.0; i <= 1.0; i+=0.05){
			System.out.println("" + i + " : " + results.get(i));
		}
	}
	
	private static void runParallel(final double inertia, final int executions){
		Thread thread = new Thread(new Runnable() {
			List<Double> fitnessList = new ArrayList<Double>(executions);
			 
			public void run() {
				for(int i = 0; i < executions; i++){
					System.out.println("" + inertia + ": " + i + " of " + executions);
					double fitness = runSimulation(inertia);
					fitnessList.add(fitness);
				}
				
				double sum = 0.0;
				for(double f : fitnessList){
					sum += f;
				}
				double avg = sum / (double) executions;
				
				results.put(inertia, avg);
			}
		});
		
		threads.add(thread);
		thread.start();
	}
	
	private static double runSimulation(double inertia) {
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
		MultiSwarm multiSwarm = new MultiSwarm(swarmInformations.toArray(swarmInformationsArray), fitnessFunction);
		
		Neighborhood neighbourhood = new Neighborhood1D(cnt / 5, true);
		multiSwarm.setNeighborhood(neighbourhood);
		
		
		multiSwarm.setNeighborhoodIncrement(0.9);
		multiSwarm.setInertia(inertia);
		multiSwarm.setParticleIncrement(0.9);
		multiSwarm.setGlobalIncrement(0.9);
		
		multiSwarm.setMaxPosition(5.12);
		multiSwarm.setMinPosition(-5.12);
		
		multiSwarm.setAbsMaxVelocity(maxVelocity);
		multiSwarm.init();
		
		for(int i = 0; i < NUMBER_OF_ITERATIONS; ++i) {
			// Evolve swarm
			multiSwarm.evolve();
		}
		
		return multiSwarm.getBestFitness();
	}
}
