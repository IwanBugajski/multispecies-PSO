package pl.edu.agh.mpso.fitness;

import net.sourceforge.jswarm_pso.FitnessFunction;

public class Parabola extends FitnessFunction {
	
	public Parabola(){
		super(false);
	}
	
	public Parabola(boolean maximize){
		super(maximize);
	}

	@Override
	public double evaluate(double[] position) {
		double result = 0.0;
		
		for(double x : position){
			result += x*x;
		}
		
		return result;
	}

}
