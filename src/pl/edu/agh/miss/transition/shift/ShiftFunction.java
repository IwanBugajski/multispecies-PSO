package pl.edu.agh.miss.transition.shift;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sourceforge.jswarm_pso.Particle;
import pl.edu.agh.miss.particle.species.SpeciesParticle;
import pl.edu.agh.miss.particle.species.SpeciesType;
import pl.edu.agh.miss.swarm.SwarmInformation;

public abstract class ShiftFunction {
	protected int updatesInterval = 100;
	private int shiftCnt = SpeciesType.values().length / 2;
	private int shiftRange = 2;

	protected abstract Particle selectParticle(SwarmInformation swarmInfo);
	
	public void shift(SwarmInformation [] swarmInformations, SpeciesType [] order){
		//sort swarm information with a given order & find swarms with more than 0 particles
		List<SwarmInformation> sorted = new ArrayList<SwarmInformation>(swarmInformations.length);
		List<SwarmInformation> notEmpty = new ArrayList<SwarmInformation>(swarmInformations.length);
		Map<SpeciesType, SwarmInformation> sortingMap = new HashMap<SpeciesType, SwarmInformation>(swarmInformations.length);
		Random random = new Random();
		
		for(SwarmInformation swarmInfo : swarmInformations){
			sortingMap.put(swarmInfo.getType(), swarmInfo);
			if(swarmInfo.getNumberOfParticles() > 0){
				notEmpty.add(swarmInfo);
			}
		}
		
		for(SpeciesType type : order){
			sorted.add(sortingMap.get(type));
		}
		
		//remove first species from notEmpty - it cannot be shifted to a better one
		notEmpty.remove(sorted.get(0));
		
		for(int i = 0; i < shiftCnt && !notEmpty.isEmpty(); i++){
			//select random source swarm info
			int fromIndex = random.nextInt(notEmpty.size());
			SwarmInformation from = notEmpty.get(fromIndex);
			
			if(from.getParticles().size() == 0){
				System.out.println(from);
			}
			
			//select random destination swarm info - a better one than source
			int toIndex = Math.max(0, sorted.indexOf(from) - 1 - random.nextInt(shiftRange));
			SwarmInformation to = sorted.get(toIndex);
			
			//perform shift
			doShift(selectParticle(from), from, to);
			
			//update notEmpty
			if(from.getNumberOfParticles() == 0){
				notEmpty.remove(from);
			}
			if(toIndex != 0 && to.getNumberOfParticles() == 1){
				notEmpty.add(to);
			}
		}
	}
	
	
	public ShiftFunction setUpdatesInterval(int updatesInterval) {
		this.updatesInterval = updatesInterval;
		return this;
	}
	
	public int getUpdatesInterval() {
		return updatesInterval;
	}
	
	protected void doShift(Particle particle, SwarmInformation from, SwarmInformation to){
		System.out.println("\tShift: " + from.getType() + " -> " + to.getType());

		((SpeciesParticle) particle).setType(to.getType());
		from.removeParticle(particle);
		to.addParticle(particle);
		
	}

	public int getShiftCnt() {
		return shiftCnt;
	}

	public ShiftFunction setShiftCnt(int shiftCnt) {
		this.shiftCnt = shiftCnt;
		return this;
	}

	public int getShiftRange() {
		return shiftRange;
	}

	public ShiftFunction setShiftRange(int shiftRange) {
		this.shiftRange = shiftRange;
		return this;
	}
}
