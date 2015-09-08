package pl.edu.agh.miss.particle.species;

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.ParticleUpdate;
import net.sourceforge.jswarm_pso.Swarm;

public class SpeciesParticleUpdate extends ParticleUpdate{
	private SpeciesType type = SpeciesType.ALL;
	
	public SpeciesParticleUpdate(Particle particle) {
		super(particle);
		
		if(particle instanceof SpeciesParticle){
			type = ((SpeciesParticle) particle).getType();
		}
		
	}

	@Override
	public void update(Swarm swarm, Particle particle) {
		double [] position = particle.getPosition();
		double [] velocity = particle.getVelocity();
		
		double [] globalBest = swarm.getBestPosition();
		double [] localBest = particle.getBestPosition();
		double [] neighbourhoodBest = swarm.getNeighborhoodBestPosition(particle);
		
		double localIncrement = swarm.getParticleIncrement();
		double globalIncrement = swarm.getGlobalIncrement();
		double neighbourhoodInrement = swarm.getNeighborhoodIncrement();
		
		double [] weights = type.getWeights();
		
		for(int i = 0; i < position.length; i++){
			// Update position
			position[i] = position[i] + velocity[i];
			
			velocity[i] = swarm.getInertia() * velocity[i]
					+ globalIncrement * weights[0] * (globalBest[i] - position[i])
					+ localIncrement * weights[1] * (localBest[i] - position[i])
					+ neighbourhoodInrement * weights[2] * (neighbourhoodBest[i] - position[i]);
		}
	}

}
