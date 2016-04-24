package pl.edu.agh.miss.transition.order;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import pl.edu.agh.miss.particle.species.SpeciesType;

public class MapComparator implements Comparator<Map.Entry<SpeciesType, Double>>{

	public int compare(Entry<SpeciesType, Double> arg0, Entry<SpeciesType, Double> arg1) {
		return arg0.getValue().compareTo(arg1.getValue());
	}


}
