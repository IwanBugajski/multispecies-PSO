package pl.edu.agh.miss.fitness;

import net.sourceforge.jswarm_pso.FitnessFunction;

import static java.lang.Math.*;

/**
 * Griewank function according to description at:
 * http://www.sfu.ca/~ssurjano/griewank.html
 */
public class Griewank extends FitnessFunction{
	
	public Griewank(){
		super(false);
	}
	
    @Override
    public double evaluate(double[] position) {
        double sumOfSquaredPos = createSumOfSquaredPos(position);
        double productOfCos = createProductOfCos(position);
        return (1/4000) * sumOfSquaredPos - productOfCos + 1;
    }

    private double createProductOfCos(double[] position) {
        double result = 1;
        int i = 1;
        for (double pos : position) {
            result *= cos(pos / sqrt(i));
            i++;
        }
        return result;
    }

    private double createSumOfSquaredPos(double[] position) {
        double result = 0;
        for (double pos : position) {
            result += pow(pos, 2);
        }
        return result;
    }
}
