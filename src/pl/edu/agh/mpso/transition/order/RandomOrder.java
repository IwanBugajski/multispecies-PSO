package pl.edu.agh.mpso.transition.order;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pl.edu.agh.mpso.species.SpeciesParticle;
import pl.edu.agh.mpso.species.SpeciesType;

public class RandomOrder extends OrderFunction{

	@Override
	public void calculate(SpeciesParticle[] particles) {
		List<SpeciesType> orderList = Arrays.asList(order);
		Collections.shuffle(orderList);
		orderList.toArray(order);
	}

}
