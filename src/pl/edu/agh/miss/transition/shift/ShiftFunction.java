package pl.edu.agh.miss.transition.shift;

import pl.edu.agh.miss.particle.species.SpeciesType;
import pl.edu.agh.miss.swarm.SwarmInformation;

public abstract class ShiftFunction {
	protected int updatesInterval = 100;

	public abstract void shift(SwarmInformation [] swarmInformations, SpeciesType [] order);
	
	public ShiftFunction setUpdatesInterval(int updatesInterval) {
		this.updatesInterval = updatesInterval;
		return this;
	}
	
	public int getUpdatesInterval() {
		return updatesInterval;
	}
}
