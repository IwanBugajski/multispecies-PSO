package pl.edu.agh.miss.transition.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.agh.miss.particle.species.SpeciesParticle;
import pl.edu.agh.miss.particle.species.SpeciesType;

public class BestAvgLocalOrder extends BestLocalOrder{
	private Map<SpeciesType, Double> bestLocalAvg = new HashMap<SpeciesType, Double>();
	private Map<SpeciesType, List<Double>> bestLocal = new HashMap<SpeciesType, List<Double>>();
	
	@Override
	public void calculate(SpeciesParticle[] particles) {
		for(SpeciesParticle particle : particles){
			SpeciesType type = particle.getType();
			double bestFitness = particle.getBestFitness();
			
			if(!bestLocal.containsKey(type)){
				bestLocal.put(type, new ArrayList<Double>());
			}

			bestLocal.get(type).add(bestFitness);
		}
		
		for(SpeciesParticle particle : particles){
			double sum = 0.0;
			SpeciesType type = particle.getType();
			List<Double> partialResults = bestLocal.get(type);
			
			for(double partialResult : partialResults){
				sum += partialResult;
			}
			
			bestLocalAvg.put(type, sum / partialResults.size());
		}
		
		for(SpeciesType type : SpeciesType.values()){
			if(!bestLocalAvg.containsKey(type)){
				bestLocalAvg.put(type, Double.POSITIVE_INFINITY);
			}
		}
		
		order = sort(bestLocalAvg);
	}
}
