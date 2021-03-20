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

    public Population(int populationQuantity){
        this.populationQuantity = populationQuantity;
        this.population = new ArrayList<Solution>(populationQuantity);
    }

    public Population( ArrayList<Solution> sol){
        this.population = sol;
        this.populationQuantity = sol.size();
    }

    public void generatePopulation(ArrayList<Track> tracks){
        ArrayList<Track> initialTracks = (ArrayList<Track>)tracks.clone();
        for (int i = 0 ; i < populationQuantity ; i++ ){
            ArrayList<City> path = (ArrayList<City>) listOfCities.clone();
            population.add(i, new Solution(path));
        }
    }



    public void printPopulation(){
        for(int i = 0 ; i < populationQuantity ; i++){
            System.out.println(population.get(i).toString());
        }
    }

    public void printPopulationResults(){
        for(int i = 0 ; i < populationQuantity ; i++){
            System.out.println(population.get(i).toString() + " : " + population.get(i).getSolutionWithReturnString() + " : " + population.get(i).fitness);
        }
    }

    public String getPopulationResultString(){
        String result = "";
        for(int i = 0 ; i < populationQuantity ; i++){
            result += i + " \t"+ population.get(i).fitness;
            if(i != populationQuantity-1) result += "\n";
        }
        return result;
    }

    public Solution RunTournament(int tournamentSize){
        Random gen = new Random();
        ArrayList<Solution> tournamentParticipants = new ArrayList<Solution>(tournamentSize);
        for (int i = 0; i < tournamentSize; i++){
            int next = gen.nextInt(populationQuantity);
            tournamentParticipants.add(population.get(next));
        }
            Solution best = tournamentParticipants.get(0);
            for(Solution tourParticipant : tournamentParticipants)
                if (tourParticipant.fitness < best.fitness)
                    best = tourParticipant;
            return new Solution ((ArrayList<City>)best.solution.clone());
    }

    public Solution RunRoulette(int tournamentSize){
        Random gen = new Random();
        double[] rouletteParticipants = new double[populationQuantity];
        // double minFitness = Double.MAX_VALUE;
        for (int i = 0; i < populationQuantity; i++){
            // int next = gen.nextInt(populationQuantity);
            // Solution temp = (Solution) population.get(i);
            // temp.fitness = 1/temp.fitness;
            rouletteParticipants[i] = 1 / population.get(i).fitness;
            // rouletteParticipants.get(i).fitness = 1/population.get(i).fitness;
           
            
            // if (minFitness > tournamentParticipants.get(i).fitness){
            //     minFitness = tournamentParticipants.get(i).fitness;
            // }
        }
        // var list = tournamentParticipants.ToList();
        // list.Sort((x, y) => (int)(x.Fitness - y.Fitness));
        // Collections.sort(tournamentParticipants);
        double sum = 0;
        for(double part: rouletteParticipants){
            sum += part;
        }
        double chosenFitness = gen.nextDouble() * sum;
        double partial_sum = 0;
        for(int i = 0 ; i < rouletteParticipants.length ; i++){
            partial_sum += rouletteParticipants[i];
            if (partial_sum > chosenFitness){
                return population.get(i);
            }
        }
        return population.get(rouletteParticipants.length-1);
        // double[] maxValueOfThisParticipant = new double[tournamentSize];
        // for (int i = 0; i < tournamentSize; i++){
        //     if (i == 0)
        //         maxValueOfThisParticipant[i] = 1000;
        //     else
        //         maxValueOfThisParticipant[i] = 1000 / ((i+2) * (i + 2)) + maxValueOfThisParticipant[i - 1];

        // }
        // int chosenFitness = gen.nextInt((int) maxValueOfThisParticipant[tournamentSize-1]);
        // Solution chosen = null;
        // for (int i = 0; i < tournamentSize; i++)
        // {
        //     if (chosenFitness < maxValueOfThisParticipant[i])
        //         chosen = tournamentParticipants.get(i);
        // }
        // if(chosen==null)
        //     chosen = tournamentParticipants.get(tournamentSize - 1);
        // return chosen;
    }

    public void calculateStandardDeviation(){
        double average = this.averageSolution;
        double mianownik = 0;
        for (Solution sol : population){
            mianownik += ((average - sol.fitness) * (average - sol.fitness));
        }
        stanDev = Math.sqrt(mianownik/populationQuantity);
    }
}

