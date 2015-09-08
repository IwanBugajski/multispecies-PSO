package pl.edu.agh.miss.particle.deprecated;

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.ParticleUpdate;
import net.sourceforge.jswarm_pso.Swarm;
/**
 * 
 * @deprecated
 */
public class ParticleUpdateBC extends ParticleUpdate {
	
	private double localIncrement;
	private double neighbourIncrement;
	private double globalIncrement;

	public ParticleUpdateBC(Particle particle) {
		super(particle);
		// TODO Auto-generated constructor stub
	}
	
	public void setLocalIncrement(double localIncrement) {
		this.localIncrement = localIncrement;
	}
	
	public void setNeighbourIncrement(double neighbourIncrement) {
		this.neighbourIncrement = neighbourIncrement;
	}
	
	public void setGlobalIncrement(double globalIncrement) {
		this.globalIncrement = globalIncrement;
	}

	@Override
	public void update(Swarm swarm, Particle particle) {
		double position[] = particle.getPosition();
		double velocity[] = particle.getVelocity();
		double globalBestPosition[] = swarm.getBestPosition();
		double particleBestPosition[] = particle.getBestPosition();
		double neighBestPosition[] = swarm.getNeighborhoodBestPosition(particle);

		localIncrement = Math.random();
		neighbourIncrement = Math.random();
		globalIncrement = 2.0 - localIncrement - neighbourIncrement;
		
		// Update velocity and position
		for (int i = 0; i < position.length; i++) {
			// Update position
			position[i] = position[i] + velocity[i];

			// Update velocity
			velocity[i] = swarm.getInertia() * velocity[i] // Inertia
					+ Math.random() * localIncrement * (particleBestPosition[i] - position[i]) // Local best
					+ Math.random() * neighbourIncrement * (neighBestPosition[i] - position[i]) // Neighborhood best					
					+ Math.random() * globalIncrement * (globalBestPosition[i] - position[i]); // Global best
		}
	}

}
