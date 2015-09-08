package pl.edu.agh.miss.particle.species;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.sourceforge.jswarm_pso.Particle;

public class SpeciesParticle extends Particle {
	
	private SpeciesType type;
	
	public SpeciesParticle(SpeciesType type, int numberOfDimentions) {
		super(numberOfDimentions);
		this.type = type;
	}
	
	public SpeciesType getType() {
		return type;
	}
	
	public SpeciesParticle selfFactory(SpeciesType type) {
		Class cl = this.getClass();
		Constructor cons;

		try {
			return (SpeciesParticle) cl.asSubclass(SpeciesParticle.class).getConstructor(SpeciesType.class).newInstance(type);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
