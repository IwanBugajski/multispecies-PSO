package pl.edu.agh.miss.transition.shift;

import net.sourceforge.jswarm_pso.Particle;
import pl.edu.agh.miss.particle.species.SpeciesType;
import pl.edu.agh.miss.swarm.SwarmInformation;

public class DefaultShiftFunction extends ShiftFunction{
	
	public DefaultShiftFunction(){
		this.updatesInterval = -1;
	}

	@Override
	public void shift(SwarmInformation [] swarmInformations, SpeciesType [] order){
		//do nothing
	}

	@Override
	protected Particle selectParticle(SwarmInformation swarmInfo) {
		return null;
	}

}
