package pl.edu.agh.mpso.transition.order;

import pl.edu.agh.mpso.species.SpeciesParticle;
import pl.edu.agh.mpso.species.SpeciesType;

public abstract class OrderFunction {
	protected SpeciesType [] order;
	protected int updatesInterval = 100;
	
	public OrderFunction(){
		order = new SpeciesType [SpeciesType.values().length];
		System.arraycopy(SpeciesType.values(), 0, order, 0, SpeciesType.values().length);
	}
	
	public abstract void calculate(SpeciesParticle [] particles);

	public SpeciesType [] getOrder() {
		return order;
	}

	public OrderFunction setUpdatesInterval(int updatesInterval) {
		this.updatesInterval = updatesInterval;
		return this;
	}
	
	public int getUpdatesInterval() {
		return updatesInterval;
	}
}
