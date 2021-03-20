import java.util.ArrayList;
import java.util.Random;

public class EvolutionAlgorithm {

    int populationQuantity;
    int generationQuantity;
    double mutationProp;
    double crossoverProp;
    double selectionProp;
    boolean roulette;
    int tournamentSize;

    Population population;

    public EvolutionAlgorithm(int popQuant, int genQuant, double mutProp, double crossProp, boolean rou, Problem pb, int tsize) {
        this.populationQuantity = popQuant;
        this.generationQuantity = genQuant;
        this.mutationProp = mutProp;
        this.crossoverProp = crossProp;
        this.problem = pb;
        this.roulette = rou;
        this.tournamentSize = tsize;
        this.population = new Population(populationQuantity);
    }

    public void Calculate(int attempt) {
        population.generatePopulation(problem.loader.citiesList);
        problem.calculateFitness(population);
        population.calculateStandardDeviation();

        Logger log_pop = new Logger("Populations" + attempt + ".txt");
        // Logger log_ind = new Logger("Individuals"+ attempt + ".txt");
        log_pop.logPopulation(population, false);
        // log_ind.logIndividuals(population, false);

        for (int i = 0; i < generationQuantity; i++) {
            population = newGeneration();
            mutatePopulationInverse();
            problem.calculateFitness(population);
            population.calculateStandardDeviation();
            log_pop.logPopulation(population, true);
            // log_ind.logIndividuals(population, true);
            if(i == (generationQuantity -1)){
            System.out.println(population.bestSolution.fitness);
            }
            
        }
    }

    public Population newGeneration() {
        Random gen = new Random();
        ArrayList<Solution> pop = new ArrayList<Solution>(populationQuantity);
        Solution first = null, second = null;
        for (int i = 0; i < populationQuantity; i += 2) {
            // tournament
            if (roulette) {
                first = population.RunRoulette(tournamentSize);
                second = population.RunRoulette(tournamentSize);
            } else {
                first = population.RunTournament(tournamentSize);
                second = population.RunTournament(tournamentSize);
            }
            if (gen.nextDouble() < crossoverProp) {
                var result = first.CrossPMX(second, problem.loader.citiesList);
                first = result.child1;
                second = result.child2;
            }
            pop.add(first);
            pop.add(second);
        }
        return new Population(pop);
    }

    public void mutatePopulationInverse(){
        Random gen = new Random();
        for (int i = 0 ; i < populationQuantity ; i++){
            if(gen.nextDouble() < mutationProp){
                population.population.get(i).mutationInverse();
            }
        }
    }

    public void mutatePopulationSwap(){
        Random gen = new Random();
        for (int i = 0 ; i < populationQuantity ; i++){
            if(gen.nextDouble() < mutationProp){
                population.population.get(i).mutationSwap();
            }
        }
    }
}