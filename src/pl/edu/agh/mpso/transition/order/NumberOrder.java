package pl.edu.agh.mpso.transition.order;

import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mpso.species.SpeciesParticle;
import pl.edu.agh.mpso.species.SpeciesType;

public class NumberOrder extends BestLocalOrder{
	private Map<SpeciesType, Double> count = new HashMap<SpeciesType, Double>();
	
	@Override
	public void calculate(SpeciesParticle[] particles) {
		for(SpeciesType type : SpeciesType.values()){
			count.put(type, 0.0);
		}
		
		for(SpeciesParticle particle : particles){
			SpeciesType type = particle.getType();
			double cnt = count.get(type);
			count.put(type, cnt++);
		}
		
		order = sort(count, true);
	}

}
