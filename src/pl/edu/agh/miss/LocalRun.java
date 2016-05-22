package pl.edu.agh.miss;

import static pl.edu.agh.miss.Simulation.NUMBER_OF_DIMENSIONS;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_ITERATIONS;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_PARTICLES;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Neighborhood;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import pl.edu.agh.miss.dao.SimulationResultDAO;
import pl.edu.agh.miss.fitness.Rastrigin;
import pl.edu.agh.miss.output.SimulationOutput;
import pl.edu.agh.miss.output.SimulationOutputError;
import pl.edu.agh.miss.output.SimulationOutputOk;
import pl.edu.agh.miss.output.SimulationResult;
import pl.edu.agh.miss.particle.species.SpeciesType;
import pl.edu.agh.miss.swarm.MultiSwarm;
import pl.edu.agh.miss.swarm.SwarmInformation;
import pl.edu.agh.miss.transition.order.RandomOrder;
import pl.edu.agh.miss.transition.shift.BestLocalShift;
import pl.edu.agh.miss.transition.shift.RandomShift;

/**
 * 
 * @author iwanb
 * command line args:
 * - function name - must be the same as class from pl.edu.agh.miss.fitness
 * - number of dimensions
 * - number of iterations
 * - species id
 * - proportional share of given species
 */
public class LocalRun {
	private static String className;
	private static List<Thread> threads;

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException, InterruptedException {
		FitnessFunction fitnessFunction = new Rastrigin();
		NUMBER_OF_DIMENSIONS = 100;
		NUMBER_OF_ITERATIONS = 5000;
		int executions = 30;
		
		className = fitnessFunction.getClass().getName();
		
		threads = new ArrayList<Thread>();
		
		runParallel(4, fitnessFunction, new int[]{4,3,3,3,3,3,3,3}, executions);
		
//		runParallel(0, fitnessFunction, new int[]{0,4,3,4,3,4,3,4}, executions);
//		runParallel(4, fitnessFunction, new int[]{4,3,3,3,3,3,3,3}, executions);
//		runParallel(11, fitnessFunction, new int[]{11,2,2,2,2,2,2,2}, executions);
//		runParallel(18, fitnessFunction, new int[]{18,1,1,1,1,1,1,1}, executions);
//		runParallel(25, fitnessFunction, new int[]{25,0,0,0,0,0,0,0}, executions);
		
//		runParallel(0, fitnessFunction, new int[]{4,3,3,3,3,3,3,3}, executions);
//		runParallel(1, fitnessFunction, new int[]{5,10,5,5,0,0,0,0}, executions);
//		runParallel(2, fitnessFunction, new int[]{0,0,0,0,7,7,7,4}, executions);
//		runParallel(3, fitnessFunction, new int[]{6,0,0,0,5,5,5,4}, executions);
//		runParallel(4, fitnessFunction, new int[]{3,6,4,2,2,2,2,4}, executions);
		
//		runParallel(0, fitnessFunction, new int[]{4,3,3,3,3,3,3,3}, executions);
//		runParallel(1, fitnessFunction, new int[]{5,10,5,5,0,0,0,0}, executions);
//		runParallel(2, fitnessFunction, new int[]{0,0,0,0,7,7,7,4}, executions);
//		runParallel(3, fitnessFunction, new int[]{6,0,0,0,5,5,5,4}, executions);
//		runParallel(4, fitnessFunction, new int[]{3,6,4,2,2,2,2,4}, executions);
		
		for(Thread thread : threads){
			thread.join();
		}
	}

	private static void runParallel(final int id, final FitnessFunction fitnessFunction, final int[] speciesArray, final int executions){
		Thread thread = new Thread(new Runnable() {
			
			public void run() {
				for(int i = 0; i < executions; i++){
					try {
						simulate(fitnessFunction, speciesArray, id, executions, i);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		threads.add(thread);
		thread.start();
	}

	private static void simulate(FitnessFunction fitnessFunction,
			int[] speciesArray, int id, int executions, int i) throws IOException {
		SimulationOutput output = null;
		try{
			long tic = System.currentTimeMillis();
			SimulationResult result = run(speciesArray, fitnessFunction);
			long toc = System.currentTimeMillis();
			long diff = toc - tic;
			long seconds = diff / 1000L;
			long minutes = seconds / 60L;
			long hours = minutes / 60L;
			System.out.println("" + id + ": Execution " + (i+1) + " of " + executions);
			System.out.println("\tDone in: " + hours + " hours " + (minutes%60) + " minutes " + (seconds % 60) + " seconds");
			
			output = new SimulationOutputOk();
			((SimulationOutputOk) output).results = result;
			SimulationResultDAO.getInstance().writeResult(result);
			SimulationResultDAO.getInstance().close();
			System.out.println(result.bestFitness);
		} catch (Throwable e){
			e.printStackTrace();
			output = new SimulationOutputError();
			((SimulationOutputError)output).reason = e.toString() + ": " + e.getMessage();
		} finally {
//			Writer writer = new FileWriter("output.json");
//			Gson gson = new Gson();
//			gson.toJson(output, writer);
//			writer.close();
		}
	}
	

	private static SimulationResult run(int [] particles, FitnessFunction fitnessFunction) {
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
		
		multiSwarm.setInertia(0.95);
		multiSwarm.setNeighborhoodIncrement(0.9);
		multiSwarm.setParticleIncrement(0.9);
		multiSwarm.setGlobalIncrement(0.9);
		
		multiSwarm.setMaxPosition(20);
		multiSwarm.setMinPosition(-20);
		
//		multiSwarm.setVelocityFunction(new LinearVelocityFunction(0.1, 2.5).setUpdatesCnt(100).setUpdatesInterval(20));
		multiSwarm.setOrderFunction(new RandomOrder().setUpdatesInterval(1000));
		multiSwarm.setShiftFunction(new RandomShift().setUpdatesInterval(1000));

		multiSwarm.setAbsMaxVelocity(2.0);
		multiSwarm.init();
		
		List<Double> partial = new ArrayList<Double>(NUMBER_OF_ITERATIONS / 100);
		
		for(int i = 0; i < NUMBER_OF_ITERATIONS; ++i) {
			// Evolve swarm
			multiSwarm.evolve();
			
			//display partial results
			if(NUMBER_OF_ITERATIONS > 100 && (i % (NUMBER_OF_ITERATIONS / 100) == 0)){
				partial.add(multiSwarm.getBestFitness());
			}
		}
		
		//create output.json
		SimulationResult output = new SimulationResult();
		output.fitnessFunction = className;
		output.iterations = NUMBER_OF_ITERATIONS;
		output.dimensions = NUMBER_OF_DIMENSIONS;
		output.partial = partial;
		output.bestFitness = multiSwarm.getBestFitness();
		output.totalParticles = NUMBER_OF_PARTICLES;
		
		output.species1 = particles[0];
		output.species2 = particles[1];
		output.species3 = particles[2];
		output.species4 = particles[3];
		output.species5 = particles[4];
		output.species6 = particles[5];
		output.species7 = particles[6];
		output.species8 = particles[7];
		
		output.orderFunction = multiSwarm.getOrderFunction().getClass().getSimpleName();
		output.shiftFunction = multiSwarm.getShiftFunction().getClass().getSimpleName();
		
		return output;
	}
}
