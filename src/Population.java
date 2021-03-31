import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Population {

    ArrayList<Solution> population;
    int populationQuantity;
    Solution bestSolution;
    Solution worstSolution;
    double averageSolution = 0;
    double stanDev;
    double sumOfPrzystosowanie = 0;

    public Population(int populationQuantity) {
        this.populationQuantity = populationQuantity;
        this.population = new ArrayList<Solution>(populationQuantity);
    }

    public Population(ArrayList<Solution> sol) {
        this.population = sol;
        this.populationQuantity = sol.size();
    }

    public void generatePopulation(ArrayList<Track> tracks, int rows, int columns) {
        for (int i = 0; i < populationQuantity; i++) {
            ArrayList<Track> path = Helpers.cloneArrayList(tracks);
            Solution newSolution = new Solution(path, rows, columns);
            newSolution.generateRandomTracks();
            population.add(i, newSolution);
            System.out.println(i);
        }
    }

    public void printPopulation() {
        for (int i = 0; i < populationQuantity; i++) {
            System.out.println(population.get(i).toString());
        }
    }

    public String getPopulationResultString() {
        String result = "";
        for (int i = 0; i < populationQuantity; i++) {
            result += i + " \t" + population.get(i).fitness;
            if (i != populationQuantity - 1)
                result += "\n";
        }
        return result;
    }

    public Solution RunTournament(int tournamentSize) {
        Random gen = new Random();
        ArrayList<Solution> tournamentParticipants = new ArrayList<Solution>(tournamentSize);
        for (int i = 0; i < tournamentSize; i++) {
            int next = gen.nextInt(populationQuantity);
            tournamentParticipants.add(new Solution(population.get(next)));
        }
        Solution best = tournamentParticipants.get(0);
        for (Solution tourParticipant : tournamentParticipants)
            if (tourParticipant.fitness < best.fitness)
                best = tourParticipant;
        return new Solution(best);
    }

    public Solution RunRoulette(){
        Random rand = new Random();
        double valueOfChosen = rand.nextDouble() * sumOfPrzystosowanie;
        double localSumOfPrzystosowanie = 0;
        Solution result = null;
        for (int i = 0 ; i < populationQuantity ; i++){
            localSumOfPrzystosowanie += population.get(i).przystosowanie;
            if(localSumOfPrzystosowanie > valueOfChosen){
                result = new Solution(population.get(i));
                break;
            }
        }
        return result;
    }

    public void calculateAverageSolution() {
        int sum = 0;
        for (int i = 0; i < this.populationQuantity; i++) {
            sum += this.population.get(i).fitness;
        }
        this.averageSolution = sum / this.populationQuantity;
    }

    public void calculateStandardDeviation() {
        double average = this.averageSolution;
        double mianownik = 0;
        for (Solution sol : population) {
            mianownik += ((average - sol.fitness) * (average - sol.fitness));
        }
        stanDev = Math.sqrt(mianownik / populationQuantity);
    }

    public void calculateAllFitensses() {
        for (int i = 0; i < this.population.size(); i++) {
            this.population.get(i).calculateFitness();
        }
        getBestSolution();
        getWorstSolution();
        calculatePrzystosowanie();
    }

    public ArrayList<Track> getWorstSolution() {
        int worstFitness = 0;
        Solution result = null;
        for (int i = 0; i < this.populationQuantity; i++) {
            if (this.population.get(i).fitness > worstFitness) {
                worstFitness = this.population.get(i).fitness;
                result = this.population.get(i);
            }
        }
        this.worstSolution = result;
        if (result == null) {
            int k = 0;
        }
        return result.tracks;
    }

    public ArrayList<Track> getBestSolution() {
        int bestFitness = Integer.MAX_VALUE;
        Solution result = null;
        for (int i = 0; i < this.populationQuantity; i++) {
            if (this.population.get(i).fitness < bestFitness) {
                bestFitness = this.population.get(i).fitness;
                result = this.population.get(i);
            }
        }
        this.bestSolution = result;
        return result.tracks;
    }

    public void calculatePrzystosowanie() {
        int bestSolutionFitness = bestSolution.fitness;
        for (int i = 0; i < populationQuantity; i++) {
            population.get(i).calculatePrzystosowanie(bestSolutionFitness);
            sumOfPrzystosowanie += population.get(i).przystosowanie;
        }
    }
}
