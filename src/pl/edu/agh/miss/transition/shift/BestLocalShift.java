package pl.edu.agh.miss.transition.shift;

import java.util.List;

import net.sourceforge.jswarm_pso.Particle;
import pl.edu.agh.miss.swarm.SwarmInformation;

public class BestLocalShift extends ShiftFunction{

	@Override
	protected Particle selectParticle(SwarmInformation swarmInfo) {
		List<Particle> particles = swarmInfo.getParticles();
		Particle bestLocal = particles.get(0);
		
		for(int i = 1; i < particles.size(); i++){
			Particle particle = particles.get(i);
			if(particle.getBestFitness() < bestLocal.getBestFitness()){
				bestLocal = particle;
			}
		}
		
		return bestLocal;
	}

}
