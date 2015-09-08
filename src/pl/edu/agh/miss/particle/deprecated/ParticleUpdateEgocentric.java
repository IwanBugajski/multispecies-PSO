package pl.edu.agh.miss.particle.deprecated;

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.ParticleUpdate;
import net.sourceforge.jswarm_pso.Swarm;
/**
 * 
 * @deprecated
 */
public class ParticleUpdateEgocentric extends ParticleUpdate {
	
	private double localIncrement;
	private double globalIncrement;

	public ParticleUpdateEgocentric(Particle particle) {
		super(particle);
		// TODO Auto-generated constructor stub
	}
	
	public void setLocalIncrement(double localIncrement) {
		this.localIncrement = localIncrement;
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

		// Update velocity and position
		for (int i = 0; i < position.length; i++) {
			// Update position
			position[i] = position[i] + velocity[i];

			// Update velocity
			velocity[i] = swarm.getInertia() * velocity[i] // Inertia
					+ Math.random() * localIncrement * (particleBestPosition[i] - position[i]) // Local best			
					+ Math.random() * globalIncrement * (globalBestPosition[i] - position[i]); // Global best
		}
	}

}
