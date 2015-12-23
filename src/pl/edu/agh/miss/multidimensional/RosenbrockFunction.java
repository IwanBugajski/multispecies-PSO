package pl.edu.agh.miss.multidimensional;

import net.sourceforge.jswarm_pso.FitnessFunction;

public class RosenbrockFunction extends FitnessFunction{
    private static final int A = 1;
    private static final int B = 100;

    public RosenbrockFunction() {
        super(false);
    }

    public RosenbrockFunction(boolean maximize) {
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
