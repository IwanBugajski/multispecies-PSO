package pl.edu.agh.miss.swarm;

import net.sourceforge.jswarm_pso.FitnessFunction;

public class MultiSwarmParallel extends MultiSwarm {

    public MultiSwarmParallel(SwarmInformation[] swarmInfos, FitnessFunction fitnessFunction) {
        super(swarmInfos, fitnessFunction);
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
        for (int i = 0; i < particles.length; i++) {
            // Evaluate particle
            double fit = fitnessFunction.evaluate(particles[i]);

            numberOfEvaliations++; // Update counter

            // Update 'best global' position
            if (fitnessFunction.isBetterThan(bestFitness, fit)) {
                bestFitness = fit; // Copy best fitness, index, and position vector
                bestParticleIndex = i;
                if (bestPosition == null) bestPosition = new double[sampleParticle.getDimension()];
                particles[bestParticleIndex].copyPosition(bestPosition);
            }

            // Update 'best neighborhood'
            if (neighborhood != null) {
                neighborhood.update(this, particles[i]);
            }

        }
    }
}
