package pl.edu.agh.miss.multidimensional;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Particle;


public class RastriginParallelFunction extends RastriginFunction{
	private final int A = 10;

	private ExecutorService executorService;

	public RastriginParallelFunction() {
		super(false);
	}

	public RastriginParallelFunction(boolean maximize){
		super(maximize);
	}

	public void initializeThreadPool(int num) {
		executorService = Executors.newFixedThreadPool(num);
	}

	public void disposeThreadPool() {
		executorService.shutdown();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS); // wait forever
		} catch (InterruptedException e) {
			// should not happen
			e.printStackTrace();
		}
	}

	/**
	 * Evaluates a particles
	 * @param particle : Particle to evaluate
	 * @return Fitness function for a particle
	 */
	public double evaluate(Particle particle) {
		double position[] = particle.getPosition();

		int numOfWorkers = calculateNumberOfWorkers(position.length);
		double[][] positionChunks = divideArrayIntoChunks(position, numOfWorkers);
		List<Callable<Double>> tasks = new LinkedList<>();
		for (int i = 0; i < numOfWorkers; i++) {
			tasks.add(new PositionEvaluationRunnable(positionChunks[i]));
		}

		double result = position.length * A;

		try {
			List<Future<Double>> futures = executorService.invokeAll(tasks);
			for (Future<Double> future : futures) {
				Double tempResult = future.get();
				result += tempResult;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		particle.setFitness(result, isMaximize());
		return result;
	}

	public static double[][] divideArrayIntoChunks(double[] array, int countOfChunks) {
		if (countOfChunks > array.length) {
			throw new UnsupportedOperationException("Count of chunks should be lessequal than size of array");
		}

		if (countOfChunks == 1) {
			return new double [][] { array };
		}

		int chunkSize = array.length / countOfChunks;
		double[][] chunkArray = new double[countOfChunks][];

		int currentStartOfChunk = 0;
		for (int i = 0; i < countOfChunks - 1; i++) {
			int startOfNewChunk = currentStartOfChunk + chunkSize;
			chunkArray[i] = Arrays.copyOfRange(array, currentStartOfChunk, startOfNewChunk);
			currentStartOfChunk = startOfNewChunk;
		}
		chunkArray[countOfChunks - 1] = Arrays.copyOfRange(array, currentStartOfChunk, array.length);

		return chunkArray;
	}

	@Override
	public double evaluate(double[] position) {
		double result = 0;
		for(double x : position){
			result += x * x - A * Math.cos(2 * Math.PI * x);
		}
		return result;
	}

	private int calculateNumberOfWorkers(int length) {
		return (int)Math.ceil(Math.log10(length));
	}

	private class PositionEvaluationRunnable implements Callable<Double> {
		private final double[] position;

		public PositionEvaluationRunnable(double[] position) {
			this.position = position;
		}

		@Override
		public Double call() throws Exception {
			return evaluate(position);
		}
	}
}
