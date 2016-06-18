package pl.edu.agh.mpso.swarm;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.ParticleUpdate;
import pl.edu.agh.mpso.Simulation;
import pl.edu.agh.mpso.species.SpeciesParticle;
import pl.edu.agh.mpso.species.SpeciesParticleUpdate;
import pl.edu.agh.mpso.species.SpeciesType;

public class SwarmInformation {
	
	private SpeciesType type;
	private List<Particle> particles = new ArrayList<Particle>();
	private SpeciesParticle sampleParticle;
	private ParticleUpdate particleUpdate;
	private int numberOfParticles = 0;
	
	
	public SwarmInformation(int numberOfParticles, SpeciesParticle sampleParticle) {
		this.numberOfParticles  = numberOfParticles;
		this.type = sampleParticle.getType();
		this.sampleParticle = sampleParticle;
		this.particleUpdate = new SpeciesParticleUpdate(sampleParticle);
	}
	
	public SwarmInformation(int numberOfParticles, SpeciesType type){
		this(numberOfParticles, new SpeciesParticle(type, Simulation.NUMBER_OF_DIMENSIONS));
	}
	
	public SwarmInformation(int numberOfParticles, SpeciesType type, int dimensions){
		this(numberOfParticles, new SpeciesParticle(type, dimensions));
	}
	
	/**
	 * @deprecated
	 */
	public SwarmInformation(int numberOfParticles, SpeciesParticle sampleParticle, ParticleUpdate particleUpdate) {
		this.type = sampleParticle.getType();
		this.sampleParticle = sampleParticle;
		this.particleUpdate = particleUpdate;
	}
	
	public int getNumberOfParticles() {
		return numberOfParticles;
	}
	
	public SpeciesType getType() {
		return type;
	}
	
	public void addParticle(Particle particle) {
		this.particles.add(particle);
		numberOfParticles++;
	}
	
	public void removeParticle(Particle particle){
		this.particles.remove(particle);
		numberOfParticles--;
	}
	
	public List<Particle> getParticles() {
		return particles;
	}
	
	public SpeciesParticle getSampleParticle() {
		return sampleParticle;
	}
	
	public ParticleUpdate getParticleUpdate() {
		return particleUpdate;
	}
}
