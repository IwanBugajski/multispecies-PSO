package pl.edu.agh.miss.fitness;

import net.sourceforge.jswarm_pso.FitnessFunction;

import static java.lang.Math.*;

/**
 * Ackley function according to description at:
 * http://www.sfu.ca/~ssurjano/ackley.html
 */
public class Ackley extends FitnessFunction {

    private final static double A = 20;

    private final static double B = 0.2;

    private final static double C = 2 * Math.PI;
    
    public Ackley(){
    	super(false);
    }

    @Override
    public double evaluate(double[] position) {
        double d = position.length;
        double sumOfSquaredPos = calculateSumOfSquared(position);
        double sumOfCosPos = calculateSumOfCos(position);

        return -A * exp(-B * sqrt((1/d) * sumOfSquaredPos))
               -exp((1/d) * sumOfCosPos)
               + A + exp(1);
    }

    private double calculateSumOfCos(double[] position) {
        double result = 0;
        for (double pos : position) {
            result += cos(C * pos);
        }
        return result;
    }

    private double calculateSumOfSquared(double[] position) {
        double result = 0;
        for (double pos : position) {
            result += pow(pos, 2);
        }
        return result;
    }
}
