package pl.edu.agh.mpso.transition.shift;

import java.util.Random;

import net.sourceforge.jswarm_pso.Particle;
import pl.edu.agh.mpso.swarm.SwarmInformation;

public class RandomShift extends ShiftFunction{
	private Random random = new Random();

	@Override
	protected Particle selectParticle(SwarmInformation swarmInfo) {
		return swarmInfo.getParticles().get(random.nextInt(swarmInfo.getNumberOfParticles()));
	}
}
