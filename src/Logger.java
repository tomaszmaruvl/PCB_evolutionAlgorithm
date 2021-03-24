import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    boolean append;
    String filename;
    
    public Logger(String filen){
        this.filename = filen;
    }

    public void logPopulation (Population pop, boolean append){
        try {
            FileWriter writer = new FileWriter(filename, append);
            PrintWriter printWriter = new PrintWriter(writer);
            printWriter.println(pop.bestSolution +"\t" + pop.bestSolution.fitness + "\t" + pop.worstSolution.fitness + "\t" + (int)pop.averageSolution + "\t" + (int)pop.stanDev);
        printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // printWriter.println("BEST \t WORST \t AVERAGE");
    }

    public void logIndividuals(Population pop, boolean append){
        try {
            FileWriter writer = new FileWriter(filename, append);
            PrintWriter printWriter = new PrintWriter(writer);
            printWriter.println(pop.getPopulationResultString());
        printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}