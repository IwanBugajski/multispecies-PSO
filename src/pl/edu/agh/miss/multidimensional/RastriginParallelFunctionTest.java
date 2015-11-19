package pl.edu.agh.miss.multidimensional;

import net.sourceforge.jswarm_pso.Particle;
import org.junit.Test;
import pl.edu.agh.miss.particle.MyParticle;

import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by lee on 2015-11-19.
 */
public class RastriginParallelFunctionTest {

    private double[] generateRandomArray(int size) {
        double[] array = new double[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextDouble();
        }
        return array;
    }

    @Test
    public void test() throws Exception {
        Random rand = new Random();
        RastriginParallelFunction parallelFunction = new RastriginParallelFunction();
        RastriginFunction function = new RastriginFunction();
        parallelFunction.initializeThreadPool(10);

        for (int i = 0; i < 50; i++) {
            double[] array = generateRandomArray(rand.nextInt(20000));
            Particle particle = mock(Particle.class);
            when(particle.getPosition()).thenReturn(array);

            long parallelStart = System.currentTimeMillis();
            double evaluate = parallelFunction.evaluate(particle);
            long parallelEnd = System.currentTimeMillis();

            long sequenceStart = System.currentTimeMillis();
            double evaluate2 = function.evaluate(particle);
            long sequenceEnd = System.currentTimeMillis();

            assertEquals(evaluate, evaluate2, 0.005);
            System.out.println("Count of dimensions: " + array.length + " | Sequence time: " + (sequenceEnd-sequenceStart) + " and Parallel time: " + (parallelEnd-parallelStart));
        }

        parallelFunction.disposeThreadPool();
    }
}