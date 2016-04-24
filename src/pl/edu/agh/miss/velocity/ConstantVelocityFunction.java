package pl.edu.agh.miss.velocity;

public class ConstantVelocityFunction extends VelocityFunction{
	
	public ConstantVelocityFunction(){
		this.updatesInterval = -1;
	}
	
	public ConstantVelocityFunction(double v){
		this.initialVelocity = v;
	}
	
	@Override
	public double getNext() {
		return initialVelocity;
	}

}
