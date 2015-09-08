# Multispecies Particle Swarm Optimization
This repository contains an implementation of [PSO algorithm](https://en.wikipedia.org/wiki/Particle_swarm_optimization) which was modified by introducing several kinds of particles with different behaviours.

## The species
Different kinds of particles are called the *species*. Each species has its own way of calculating the velocity. In a standard PSO every particle determines the direction of movement by combining the following three components:
- Position of global best known solution
- Position of neighbourhood's best known solution
- Position of particle's best known solution

The species and their weights used for calculating velocity are described in a table below:

| Species name | Global | Neighbourhood | Particle | Commentary |
| ------------ | :----: | :-----------: | :------: | ---------- |
| All | 1.0 | 1.0 | 1.0 | This species is influenced by all three components, the same as in the standard PSO |
| GLobal & Local | 1.0 | 0.0 | 1.0 | This species ignores the influence of neighbourhood's best known position |
| GLobal & Neighbourhood | 1.0 | 1.0 | 0.0 | This species ignores the influence of particle's best known position |
| Local & Neighbourhood | 0.0 | 1.0 | 1.0 | This species ignores the influence of global best known position |
| Global Only | 1.0 | 0.0 | 0.0 | This particle is influenced only by global best known position |
| Local Only | 0.0 | 0.0 | 1.0 | This particle is influenced only by particle's best known position |
| Neighbourhood Only | 0.0 | 1.0 | 0.0 | This particle is influenced only by neighbourhood's best known position |
| Random | random | random | random | This particle multiplies all three influences by random weghts in every iteration of the algorithm. The sum of weights is always equal to 3 |

A swarm composed of several species is less likely to get stuck in local optima than a swarm of standard particles. Moreover, multispecies swarm is usually able to traverse a bigger part of search space. These regularities are caused by a fact that different particles may have different inspiration during selection of direction and can be scattered on various areas of search space.

## Third party libraries
The project uses the following libraries:
* JSwarm - open source implementation of standard PSO algorithm - [link](http://jswarm-pso.sourceforge.net/)
* JFreeChart - open source chart library - [link](http://www.jfree.org/jfreechart/)

## Execution

### Running an example
The repository contains [one class] (https://github.com/wojtekblack/miss2015/blob/master/src/pl/edu/agh/miss/Comparison.java) with a main method that runs a comparison between multispecies and standard PSO. To execute this code follow these steps:

1. Make sure you meet prerequisites:
  * [Java](https://www.java.com), version 7 or newer
  * [Eclipse](https://eclipse.org/), preferred version Kepler
2. Clone the repository
3. Import the project to eclipse
4. Add all jars located in *lib* directory to project's build path
5. Run **pl.edu.agh.miss.Comparison** as Java Application

The program will run an optimization of Rastrigin function with both standard and multispecies algorithms. Generated charts will be saved in *results* directory. The results are calculated as an average of results produced in a specified number of executions. 

### Parameters of example simulation
The optimization can be adjusted by modifying the following variables:

* In class **pl.edu.agh.miss.Comparison** :
 
  * The number of times simulation will be executed, the results are the average of results of all executions.
  ```java
  private static final int EXECUTIONS 
  ```
* In class **pl.edu.agh.miss.Simulation** :

  * Number of dimensions
  ```java
  public static int NUMBER_OF_DIMENTIONS
  ```
  * Number of steps in each execution
  ```java
  public static final int NUMBER_OF_ITERATIONS
  ```
  * Number of initial iterations that will not be included in result charts to increase charts' readability
  ```java
  public static final int NUMBER_OF_SKIPPED_ITERATIONS
  ```


### Creating a multispecies swarm
Firstly, you need to create an array of [SwarmInformation](src/pl/edu/agh/miss/swarm/SwarmInformation.java) instances. A SwarmInformation object contains two information: 

* Number of particles of specified type
* Species of those particles - an instance of enum [SpeciesType](src/pl/edu/agh/miss/particle/species/SpeciesType.java)

Example:
```java
SwarmInformation [] swarmInfos = new SwarmInformation[3];
swarmInfos[0] = new SwarmInformation(10, SpeciesType.ALL);
swarmInfos[1] = new SwarmInformation(5, SpeciesType.GLOBAL_AND_LOCAL);
swarmInfos[2] = new SwarmInformation(5, SpeciesType.RANDOM);
```
