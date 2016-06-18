package pl.edu.agh.mpso.fitness;

import net.sourceforge.jswarm_pso.FitnessFunction;

public class Rosenbrock extends FitnessFunction{
    private static final int A = 1;
    private static final int B = 100;

    public Rosenbrock() {
        super(false);
    }

    public Rosenbrock(boolean maximize) {
        super(maximize);
    }

    @Override
    public double evaluate(double[] position) {
        double result = 0;
        for (int i = 0; i < position.length - 1; i++) {
            result += Math.pow(A - position[i], 2)
                      + B * Math.pow(position[i+1] - Math.pow(position[i], 2), 2);
        }
        return result;
    }
}
