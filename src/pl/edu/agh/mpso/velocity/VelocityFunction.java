package pl.edu.agh.mpso.velocity;

public abstract class VelocityFunction {
	protected double initialVelocity = 1.0;
	protected int updatesInterval = 100;
	protected int updatesCnt = 200;
	
	public double getInitialVelocity() {
		return initialVelocity;
	}

	public VelocityFunction setInitialVelocity(double initialVelocity) {
		this.initialVelocity = initialVelocity;
		return this;
	}
	
	public abstract double getNext();

	public int getUpdatesInterval() {
		return updatesInterval;
	}

	public VelocityFunction setUpdatesInterval(int updatesInterval) {
		this.updatesInterval = updatesInterval;
		return this;
	}

	public int getUpdatesCnt() {
		return updatesCnt;
	}

	public VelocityFunction setUpdatesCnt(int updatesCnt) {
		this.updatesCnt = updatesCnt;
		return this;
	}
}
