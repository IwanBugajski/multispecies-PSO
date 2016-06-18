package pl.edu.agh.mpso.transition.shift;

import java.util.List;

import net.sourceforge.jswarm_pso.Particle;
import pl.edu.agh.mpso.swarm.SwarmInformation;

public class WorstLocalShift extends ShiftFunction{

	@Override
	protected Particle selectParticle(SwarmInformation swarmInfo) {
		List<Particle> particles = swarmInfo.getParticles();
		Particle worstLocal = particles.get(0);
		
		for(int i = 1; i < particles.size(); i++){
			Particle particle = particles.get(i);
			if(particle.getBestFitness() > worstLocal.getBestFitness()){
				worstLocal = particle;
			}
		}
		
		return worstLocal;
	}

}
