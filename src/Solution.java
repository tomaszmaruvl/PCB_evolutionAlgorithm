import java.util.ArrayList;
import java.util.Random;

import drawing.Point;

public class Solution {

    public ArrayList<Track> tracks;
    public int[][] plate;
    public int rows;
    public int columns;
    public int outOfBounds;
    public int tracksCrossing;
    public int totalPathLenght;
    public int totalSegmentsAmount;
    public int fitness;

    public Solution(ArrayList<Track> tracks, int rows, int columns) {
        this.outOfBounds = 0;
        this.tracksCrossing = 0;
        this.totalPathLenght = 0;
        this.totalSegmentsAmount = 0;
        this.tracks = tracks;
        this.columns = columns;
        this.rows = rows;
        this.plate = generateNewZerosPlate(rows, columns);
    }

    public Solution(Solution solution) {
        this.tracks = (ArrayList<Track>) Helpers.cloneArrayList(solution.tracks);
        this.outOfBounds = 0;
        this.tracksCrossing = 0;
        this.totalPathLenght = 0;
        this.totalSegmentsAmount = 0;
        this.columns = solution.columns;
        this.rows = solution.rows;
        this.plate = generateNewZerosPlate(rows, columns);
    }

    public void calculateFitness(){
        calculateFitnessInner();
        this.fitness =  this.outOfBounds * Constants.CROSSINGS_WEIGHT + 
                        this.tracksCrossing * Constants.CROSSINGS_WEIGHT +
                        this.totalPathLenght * Constants.PATH_LENGTH_WEIGHT+
                        this.totalSegmentsAmount * Constants.SEGMENTS_COUNT_WEUGHT; 
    }

    public void calculateFitnessInner() {
        for (int i = 0; i < this.tracks.size(); i++) {
            ArrayList<Point> trackCoordinated = this.tracks.get(i).getTrackCoordinated();
            this.totalSegmentsAmount += this.tracks.get(i).track.size();
            this.totalPathLenght += this.tracks.get(i).calculateTotalTrackLength();
            for (int j = 0; j < trackCoordinated.size(); j++) {
                if (!isPointInBounds(trackCoordinated.get(j))) {
                    this.outOfBounds += 1;
                }
                if (j != 0) {
                    Point start = trackCoordinated.get(j - 1);
                    Point end = trackCoordinated.get(j);
                    boolean vertical = end.x - start.x == 0 ? true : false;
                    if (vertical) {
                        for (int k = start.y; k < end.y; k++) {
                            if (k > 0 && k < columns && isPointInBounds(start)) {
                                this.plate[start.x][k] += 1;
                            }
                        }
                    } else {
                        for (int k = start.x; k < end.x; k++) {
                            if (k > 0 && k < rows && isPointInBounds(start)) {
                                this.plate[k][start.y] += 1;
                            }
                        }
                    }
                }
            }
        }

        calculateCrossings();
    }

    public int[][] generateNewZerosPlate(int rows, int columns) {
        int[][] plate = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                plate[i][j] = 0;
            }
        }
        return plate;
    }

    public boolean isPointInBounds(Point p) {
        if (p.x > 0 && p.x < rows && p.y > 0 && p.y < columns) {
            return true;
        }
        return false;
    }

    public void calculateCrossings() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (plate[i][j] > 1) {
                    this.tracksCrossing += 1;
                }
                ;
            }
        }
    }

    public void generateRandomTracks() {
        for (int i = 0; i < this.tracks.size(); i++) {
            Track p = this.tracks.get(i);
            p.setRandomTrack();
        }
    }

    public Solution[] doCrossover(Solution sol){
        Solution[] result = new Solution[2];
        Random r = new Random();
        int toChange = r.nextInt(this.tracks.size());
        Track fromSol = new Track(sol.tracks.get(toChange));
        Track fromThis = new Track(this.tracks.get(toChange));
        sol.tracks.set(toChange, fromThis);
        this.tracks.set(toChange,fromSol);
        result[0]= this;
        result[1]= sol;
        return result;
    }
}