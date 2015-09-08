package pl.edu.agh.miss.particle.species;

public enum SpeciesType {
	ALL(0),
	GLOBAL_AND_LOCAL(1),
	GLOBAL_AND_NEIGHBOUR(2),
	LOCAL_AND_NEIGHBOUR(3),
	GLOBAL_ONLY(4),
	LOCAL_ONLY(5),
	NEIGHBOUR_ONLY(6),
	RANDOM(7);
	
	public final int type;
	private final String [] typeNames = new String [] {
			"Normal", "Global and local", "Global and neighbour", "Local and neighbour", 
			"Global only", "Local only", "Neighbour only", "Random weights" 
	};
	
	private SpeciesType(int type){
		this.type = type;
	}
	
	@Override
	public String toString(){
		return typeNames[type];
	}
	
	public double [] getWeights(){
		double local, global, neighbour;
		
		switch(type){
			case 0:
				global = 1.0;
				local = 1.0;
				neighbour = 1.0;
				break;
			case 1:
				global = 1.0;
				local = 1.0;
				neighbour = 0.0;
				break;
			case 2:
				global = 1.0;
				local = 0.0;
				neighbour = 1.0;
				break;
			case 3:
				global = 0.0;
				local = 1.0;
				neighbour = 1.0;
				break;
			case 4:
				global = 1.0;
				local = 0.0;
				neighbour = 0.0;
				break;
			case 5:
				global = 0.0;
				local = 1.0;
				neighbour = 0.0;
				break;
			case 6:
				global = 0.0;
				local = 0.0;
				neighbour = 1.0;
				break;
			case 7:
				global = Math.random() * 3.0;
				local = Math.random() * (3.0 - global);
				neighbour = 3.0 - global - local;
				break;
			default:
				global = 1.0;
				local = 1.0;
				neighbour = 1.0;
		}
		
		return new double[]{global, local, neighbour};
	}
}
