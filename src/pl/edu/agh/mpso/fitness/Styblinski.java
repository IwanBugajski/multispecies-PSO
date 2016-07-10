package pl.edu.agh.mpso.fitness;

import net.sourceforge.jswarm_pso.FitnessFunction;

public class Styblinski extends FitnessFunction {

	public Styblinski(){
		super(false);
	}
	
	@Override
	public double evaluate(double[] position) {
		double result = 0.0;
		
		for(double x : position){
			result += Math.pow(x, 4) - 16 * Math.pow(x, 2) + 5 * x;
		}
		
		return result;
	}

}
