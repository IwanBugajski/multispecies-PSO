package pl.edu.agh.miss.particle.deprecated;

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.ParticleUpdate;
import net.sourceforge.jswarm_pso.Swarm;
/**
 * 
 * @deprecated
 */
public class ParticleUpdateAltercentric extends ParticleUpdate {
	
	private double globalIncrement;

	public ParticleUpdateAltercentric(Particle particle) {
		super(particle);
		// TODO Auto-generated constructor stub
	}
	
	public void setGlobalIncrement(double globalIncrement) {
		this.globalIncrement = globalIncrement;
	}

	@Override
	public void update(Swarm swarm, Particle particle) {
		double position[] = particle.getPosition();
		double velocity[] = particle.getVelocity();
		double globalBestPosition[] = swarm.getBestPosition();

		// Update velocity and position
		for (int i = 0; i < position.length; i++) {
			// Update position
			position[i] = position[i] + velocity[i];
			

			// Update velocity
			velocity[i] = swarm.getInertia() * velocity[i] // Inertia				
					+ Math.random() * globalIncrement * (globalBestPosition[i] - position[i]); // Global best
		}
	}

}
