package pl.edu.agh.miss.transition.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pl.edu.agh.miss.particle.species.SpeciesParticle;
import pl.edu.agh.miss.particle.species.SpeciesType;

public class BestLocalOrder extends OrderFunction {
	private Map<SpeciesType, Double> bestLocal = new HashMap<SpeciesType, Double>();
	
	@Override
	public void calculate(SpeciesParticle[] particles) {
		for(SpeciesParticle particle : particles){
			SpeciesType type = particle.getType();
			double bestFitness = particle.getBestFitness();

			if(!bestLocal.containsKey(type) || bestLocal.get(type) > bestFitness){
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

	protected SpeciesType [] sort(Map<SpeciesType, Double> map){
		SpeciesType [] results = new SpeciesType[map.size()];
		List<Map.Entry<SpeciesType, Double>> entryList = new ArrayList<Entry<SpeciesType, Double>>(map.entrySet());
		Collections.sort(entryList, new MapComparator());
		
		for(int i = 0; i < map.size(); i++){
			results[i] = entryList.get(i).getKey();
		}
		
		return results;
	}
}
