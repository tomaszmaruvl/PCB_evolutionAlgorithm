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
    Loader loader;

    Population population;

    public EvolutionAlgorithm(int popQuant, int genQuant, double mutProp, double crossProp, boolean rou, Loader ld,
            int tsize) {
        this.populationQuantity = popQuant;
        this.generationQuantity = genQuant;
        this.mutationProp = mutProp;
        this.crossoverProp = crossProp;
        this.loader = ld;
        this.roulette = rou;
        this.tournamentSize = tsize;
        this.population = new Population(populationQuantity);
    }

    public void Calculate() {
        population.generatePopulation(loader.connections, loader.rows, loader.columns);
        population.calculateAllFitensses();
        population.calculateAverageSolution();
        population.calculateStandardDeviation();

        Logger log_pop = new Logger("Populations" + 3 + ".txt");
        Logger log_ind = new Logger("Individuals" + 3 + ".txt");
        log_pop.logPopulation(population, false);
        log_ind.logIndividuals(population, false);

        for (int i = 0; i < generationQuantity; i++) {
            population = newGeneration();
            mutatePopulation();
            population.calculateAllFitensses();
            population.calculateAverageSolution();
            population.calculateStandardDeviation();
            log_pop.logPopulation(population, true);
            log_ind.logIndividuals(population, true);
            if (i == (generationQuantity - 1)) {
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
                Solution[] result = first.doCrossover(second);
                first = result[0];
                second = result[1];
            }
            pop.add(first);
            pop.add(second);
        }
        return new Population(pop);
    }

    public void mutatePopulation() {
        Random gen = new Random();
        for (int i = 0; i < populationQuantity; i++) {
            if (gen.nextDouble() < mutationProp) {
                ArrayList<Track> tracks = population.population.get(i).tracks;
                for (int k = 0; k < tracks.size(); k++) {
                    tracks.get(k).mutateTrackOneSegment();
                }
            }
        }
    }

    // public void mutatePopulationSwap(){
    // Random gen = new Random();
    // for (int i = 0 ; i < populationQuantity ; i++){
    // if(gen.nextDouble() < mutationProp){
    // population.population.get(i).mutationSwap();
    // }
    // }
    // }
}