package pl.edu.agh.miss.transition.order;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pl.edu.agh.miss.particle.species.SpeciesParticle;
import pl.edu.agh.miss.particle.species.SpeciesType;

public class RandomOrder extends OrderFunction{

	@Override
	public void calculate(SpeciesParticle[] particles) {
		List<SpeciesType> orderList = Arrays.asList(order);
		Collections.shuffle(orderList);
		orderList.toArray(order);
	}

}
