package pl.edu.agh.mpso.fitness;

import net.sourceforge.jswarm_pso.FitnessFunction;

import static java.lang.Math.abs;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Schwefel function according to description at:
 * http://www.sfu.ca/~ssurjano/schwef.html
 */
public class Schwefel extends FitnessFunction{
	
	public Schwefel(){
		super(false);
	}

    @Override
    public double evaluate(double[] position) {
        double d = position.length;
        double sumOfXSinSqrtX = calculateSumOfXSinSqrtX(position);
        return 418.9829 * d - sumOfXSinSqrtX;
    }

    private double calculateSumOfXSinSqrtX(double[] position) {
        double result = 0;
        for (double pos : position) {
            result += pos * sin(sqrt(abs(pos)));
        }
        return result;
    }
}
