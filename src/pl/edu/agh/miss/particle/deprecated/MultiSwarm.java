package pl.edu.agh.miss.particle.deprecated;

import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.ParticleUpdate;
import net.sourceforge.jswarm_pso.Swarm;
import net.sourceforge.jswarm_pso.VariablesUpdate;

/**
 * 
 * @deprecated
 */
public class MultiSwarm extends Swarm {
	
	private SwarmInformation swarmInfos[];
	
	public MultiSwarm(SwarmInformation swarmInfos[], FitnessFunction fitnessFunction) {
		if(swarmInfos.length <= 0) throw new RuntimeException("Number of swarm information must be greater than 0.");
		int numberOfParticles = 0;
		for(SwarmInformation swarmInfo : swarmInfos)
			numberOfParticles += swarmInfo.getNumberOfParticles();
		
		globalIncrement = DEFAULT_GLOBAL_INCREMENT;
		inertia = DEFAULT_INERTIA;
		particleIncrement = DEFAULT_PARTICLE_INCREMENT;
		numberOfEvaliations = 0;
		this.numberOfParticles = numberOfParticles;
		this.sampleParticle = swarmInfos[0].getSampleParticle();
		this.fitnessFunction = fitnessFunction;
		bestFitness = Double.NaN;
		bestParticleIndex = -1;

		// Set up variablesUpdate strategy (default: VariablesUpdate)
		variablesUpdate = new VariablesUpdate();

		neighborhood = null;
		neighborhoodIncrement = 0.0;
		particlesList = null;
		
		this.swarmInfos = swarmInfos;
	}
	
	@Override
	public void init() {
		// Init particles
		particles = new Particle[numberOfParticles];

		// Check constraints (they will be used to initialize particles)
		if (maxPosition == null) throw new RuntimeException("maxPosition array is null!");
		if (minPosition == null) throw new RuntimeException("maxPosition array is null!");
		if (maxVelocity == null) {
			// Default maxVelocity[]
			int dim = sampleParticle.getDimension();
			maxVelocity = new double[dim];
			for (int i = 0; i < dim; i++)
				maxVelocity[i] = (maxPosition[i] - minPosition[i]) / 2.0;
		}
		if (minVelocity == null) {
			// Default minVelocity[]
			int dim = sampleParticle.getDimension();
			minVelocity = new double[dim];
			for (int i = 0; i < dim; i++)
				minVelocity[i] = -maxVelocity[i];
		}

		// Init each particle
		int particleOffset = 0;
		for (SwarmInformation swarmInfo : swarmInfos) {
			for (int i = 0; i < swarmInfo.getNumberOfParticles(); ++i) {
				SpeciesParticle particle = new MyParticle(swarmInfo.getType());
				swarmInfo.addParticle(particle);
				particles[i+particleOffset] = particle;
				particles[i+particleOffset].init(maxPosition, minPosition, maxVelocity, minVelocity);
			}
			particleOffset += swarmInfo.getNumberOfParticles();
		}

		// Init neighborhood
		if (neighborhood != null) neighborhood.init(this);
	}
	
	@Override
	public void update() {
		// For each particle...
		for(SwarmInformation swarmInfo : swarmInfos) {
			for (Particle particle : swarmInfo.getParticles()) {
				ParticleUpdate particleUpdate = swarmInfo.getParticleUpdate();
				particleUpdate.begin(this);
				
				particleUpdate.update(this, particle);
	
				// Apply position and velocity constraints
				particle.applyConstraints(minPosition, maxPosition, minVelocity, maxVelocity);
				particleUpdate.end(this);
			}
		}
	}
	
	@Override
	public void evolve() {
		// Initialize (if not already done)
		if (particles == null)
			init();

		evaluate(); // Evaluate particles
		update(); // Update positions and velocities

		variablesUpdate.update(this);
	}

}
