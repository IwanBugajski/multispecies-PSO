package pl.edu.agh.miss.swarm;

import net.sourceforge.jswarm_pso.FitnessFunction;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class MultiSwarmParallel extends MultiSwarm {

    private final ExecutorService executorService;

    private final int THREAD_POOL_SIZE = 25;

    public MultiSwarmParallel(SwarmInformation[] swarmInfos, FitnessFunction fitnessFunction) {
        super(swarmInfos, fitnessFunction);
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    /**
     * Evaluate fitness function for every particle
     * Warning: particles[] must be initialized and fitnessFunction must be set
     */
    public void evaluate() {
        if (particles == null) throw new RuntimeException("No particles in this swarm! May be you need to call Swarm.init() method");
        if (fitnessFunction == null) throw new RuntimeException("No fitness function in this swarm! May be you need to call Swarm.setFitnessFunction() method");

        // Initialize
        if (Double.isNaN(bestFitness)) {
            bestFitness = (fitnessFunction.isMaximize() ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
            bestParticleIndex = -1;
        }

        //---
        // Evaluate each particle (and find the 'best' one)
        //---
        List<Callable<Object>> tasks = new LinkedList<>();
        for (int i = 0; i < particles.length; i++) {
            // Evaluate particle
            tasks.add(new EvaluationRunnable(i));
        }

        try {
            List<Future<Object>> futures = executorService.invokeAll(tasks);
            for (Future<Object> future : futures) {
                future.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        numberOfEvaliations += particles.length; // Update counter
    }

    private synchronized void updateBestPositions(int index, double fitness) {
        // Update 'best global' position
        if (fitnessFunction.isBetterThan(bestFitness, fitness)) {
            bestFitness = fitness; // Copy best fitness, index, and position vector
            bestParticleIndex = index;
            if (bestPosition == null) bestPosition = new double[sampleParticle.getDimension()];
            particles[bestParticleIndex].copyPosition(bestPosition);
        }

        // Update 'best neighborhood'
        if (neighborhood != null) {
            neighborhood.update(this, particles[index]);
        }
    }

    private class EvaluationRunnable<Object> implements Callable<Object> {
        private int index;

        public EvaluationRunnable(int index) {
            this.index = index;
        }

        @Override
        public Object call() throws Exception {
            double fit = fitnessFunction.evaluate(particles[index]);
            updateBestPositions(index, fit);
            return null;
        }
    }
}
