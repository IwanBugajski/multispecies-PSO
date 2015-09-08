package pl.edu.agh.miss.swarm;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.ParticleUpdate;
import pl.edu.agh.miss.particle.MyParticle;
import pl.edu.agh.miss.particle.species.SpeciesParticle;
import pl.edu.agh.miss.particle.species.SpeciesParticleUpdate;
import pl.edu.agh.miss.particle.species.SpeciesType;

public class SwarmInformation {
	
	private int numberOfParticles;
	private SpeciesType type;
	private List<Particle> particles = new ArrayList<Particle>();
	private SpeciesParticle sampleParticle;
	private ParticleUpdate particleUpdate;
	
	
	public SwarmInformation(int numberOfParticles, SpeciesParticle sampleParticle) {
		this.numberOfParticles = numberOfParticles;
		this.type = sampleParticle.getType();
		this.sampleParticle = sampleParticle;
		this.particleUpdate = new SpeciesParticleUpdate(sampleParticle);
	}
	
	public SwarmInformation(int numberOfParticles, SpeciesType type){
		this(numberOfParticles, new MyParticle(type));
	}
	
	/**
	 * @deprecated
	 */
	public SwarmInformation(int numberOfParticles, SpeciesParticle sampleParticle, ParticleUpdate particleUpdate) {
		this.numberOfParticles = numberOfParticles;
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
