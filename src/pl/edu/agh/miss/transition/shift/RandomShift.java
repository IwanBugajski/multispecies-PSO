package pl.edu.agh.miss.transition.shift;

import java.util.Random;

import net.sourceforge.jswarm_pso.Particle;
import pl.edu.agh.miss.swarm.SwarmInformation;

public class RandomShift extends ShiftFunction{
	private Random random = new Random();

	@Override
	protected Particle selectParticle(SwarmInformation swarmInfo) {
		return swarmInfo.getParticles().get(random.nextInt(swarmInfo.getNumberOfParticles()));
	}
}
