package pl.edu.agh.miss.particle;

import pl.edu.agh.miss.Simulation;
import pl.edu.agh.miss.particle.species.SpeciesParticle;
import pl.edu.agh.miss.particle.species.SpeciesType;

public class MyParticle extends SpeciesParticle {
	

	public MyParticle(SpeciesType type) {
		super(type, Simulation.NUMBER_OF_DIMENTIONS);
	}

}
