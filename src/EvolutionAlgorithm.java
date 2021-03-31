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
            //while(population.bestSolution.tracksCrossing > 0){
            //System.out.println(population.bestSolution.tracksCrossing);
            if(i%100==0){System.out.println(i);}
            population = newGeneration();
            checkIfHasBad();
            mutatePopulation();
            population.calculateAllFitensses();
            population.calculateAverageSolution();
            population.calculateStandardDeviation();
            log_pop.logPopulation(population, true);
            log_ind.logIndividuals(population, true);
            // if (i == (generationQuantity - 1)) {
            //     System.out.println(population.bestSolution.fitness);
            // }

        }
    }

    public Population newGeneration() {
        Random gen = new Random();
        ArrayList<Solution> pop = new ArrayList<Solution>(populationQuantity);
        Solution first = null, second = null;
        pop.add(new Solution(population.bestSolution));
        pop.add(new Solution(population.population.get(gen.nextInt(populationQuantity))));
        for (int i = 2; i < populationQuantity; i += 2) {
            // tournament
            if (roulette) {
                first = population.RunRoulette();
                second = population.RunRoulette();
            } else {
                first = population.RunTournament(tournamentSize);
                second = population.RunTournament(tournamentSize);
            }
            if (gen.nextDouble() < crossoverProp) {
                int toCross = gen.nextInt(2)+1;
                Solution[] result = first.doCrossover(second, toCross);
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
                for (int k = 0; k < population.population.get(i).tracks.size(); k++) {
                    //if(gen.nextDouble()<0.5) {
                        if (gen.nextInt(10) < 3) {
                            population.population.get(i).tracks.get(k).mutateTrackOneSegment(gen.nextInt(11)+1);
                        } else {
                            population.population.get(i).tracks.get(k).mutateTrackSplitSegment(gen.nextInt(11)+1);
                        }
                    //}
                }
            }
        }
    }

    public void checkIfHasBad() {
        for (int i = 0; i < populationQuantity; i++) {
            for (int k = 0; k < population.population.get(i).tracks.size(); k++) {
                if (!population.population.get(i).tracks.get(k).verifyTrack()) {
                    int p = 0;
                }
            }
        }
    }

    public void runRandomSolution(int size){
        population.populationQuantity = size;
        population.generatePopulation(loader.connections, loader.rows, loader.columns);
        population.calculateAllFitensses();
        population.calculateAverageSolution();
        population.calculateStandardDeviation();

        Logger log_pop = new Logger("Populations" + 3 + ".txt");
        Logger log_ind = new Logger("Individuals" + 3 + ".txt");
        log_pop.logPopulation(population, false);
        log_ind.logIndividuals(population, false);
    } 
}