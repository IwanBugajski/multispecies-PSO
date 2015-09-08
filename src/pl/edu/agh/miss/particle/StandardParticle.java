package pl.edu.agh.miss.particle;

import pl.edu.agh.miss.Simulation;
import net.sourceforge.jswarm_pso.Particle;

public class StandardParticle extends Particle{
	public StandardParticle(int dimension) {
		super(dimension);
	}
	
	public StandardParticle(){
		super(Simulation.NUMBER_OF_DIMENTIONS);
	}
}
