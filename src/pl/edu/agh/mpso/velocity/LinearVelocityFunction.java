package pl.edu.agh.mpso.velocity;

public class LinearVelocityFunction extends VelocityFunction{
	private double finalVelocity = 1.0;
	private double currentVelocity;
	private double diff;
	
	public LinearVelocityFunction(double start, double end){
		this.initialVelocity = start;
		this.currentVelocity = start;
		this.finalVelocity = end;
		
		this.diff = (end - start) / (double) updatesCnt;
	}

	@Override
	public double getNext() {
		if((diff < 0.0 && currentVelocity > finalVelocity) || (diff > 0.0 && currentVelocity < finalVelocity)){
			currentVelocity += diff;
		}
			
		return currentVelocity;
	}
}
