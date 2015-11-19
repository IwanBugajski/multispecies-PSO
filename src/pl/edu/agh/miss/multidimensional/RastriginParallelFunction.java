package pl.edu.agh.miss.multidimensional;

import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Particle;

public class RastriginParallelFunction extends RastriginFunction{
	private final int A = 10;

	public RastriginParallelFunction() {
		super(false);
	}

	public RastriginParallelFunction(boolean maximize){
		super(maximize);
	}
	
}
