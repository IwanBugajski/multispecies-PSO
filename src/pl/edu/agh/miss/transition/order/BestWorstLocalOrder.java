package pl.edu.agh.miss.transition.order;

import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.miss.particle.species.SpeciesParticle;
import pl.edu.agh.miss.particle.species.SpeciesType;

public class BestWorstLocalOrder extends BestLocalOrder{
	private Map<SpeciesType, Double> bestLocal = new HashMap<SpeciesType, Double>();
	
	@Override
	public void calculate(SpeciesParticle[] particles) {
		for(SpeciesParticle particle : particles){
			SpeciesType type = particle.getType();
			double bestFitness = particle.getBestFitness();

			if(!bestLocal.containsKey(type) || bestLocal.get(type) < bestFitness){
				bestLocal.put(type, bestFitness);
			}
		}
		
		for(SpeciesType type : SpeciesType.values()){
			if(!bestLocal.containsKey(type)){
				bestLocal.put(type, Double.POSITIVE_INFINITY);
			}
		}
		
		order = sort(bestLocal);
	}
}
