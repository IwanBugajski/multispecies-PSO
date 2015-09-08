package pl.edu.agh.miss.multidimensional;

import net.sourceforge.jswarm_pso.FitnessFunction;

public class RastriginFunction extends FitnessFunction{
	private final int A = 10;
	
	public RastriginFunction() {
		super(false);
	}
	
	public RastriginFunction(boolean maximize){
		super(maximize);
	}
	
	@Override
	public double evaluate(double[] position) {
		double result = position.length * A;
		
		for(double x : position){
			result += x * x - A * Math.cos(2 * Math.PI * x);
		}
		
		return result;
	}

}
