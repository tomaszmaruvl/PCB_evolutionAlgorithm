
import java.awt.EventQueue;
import java.util.ArrayList;

import drawing.MyFrame;
import drawing.Point;

public class Draw {

	public static void main(String[] args) {
		Loader loader = new Loader("/Users/tomaszmarulewski/Documents/_studia/PCB_evolutionAlgorithm/src/problems/zad1.txt");
		try {
			loader.load();
		} catch (Exception e) {
		}

		EvolutionAlgorithm ea = new EvolutionAlgorithm(100, 1000, 1, 1, false, loader, 10);
		ea.Calculate();
		
		ArrayList<ArrayList<Point>> toDraw = prepareTracksToDraw(ea.population.getWorstSolution(), true, loader.rows, loader.columns);
		ArrayList<ArrayList<Point>> toDraw2 = prepareTracksToDraw(ea.population.getBestSolution(), true, loader.rows, loader.columns);

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MyFrame(loader.rows, loader.columns, 30, 100, toDraw);
				new MyFrame(loader.rows, loader.columns, 30, 100, toDraw2);
			}
		});
		System.out.println("log");
	}

	public static ArrayList<ArrayList<Point>> prepareTracksToDraw( ArrayList<Track> tracks, boolean tracksExist, int rows, int columns){
		ArrayList<ArrayList<Point>> result = new ArrayList<ArrayList<Point>>();
		for (int i = 0 ; i < tracks.size() ; i++){
			Track n = tracks.get(i);
			if (!tracksExist) n.setRandomTrack();
			
			System.out.println("____________________________________");
			System.out.println(n.startingPoint);
        	System.out.println(n.track);
        	System.out.println(n.endingPoint);
			System.out.println(n.verifyTrack());
        	System.out.println("____________________________________");
			result.add(n.getTrackCoordinated());
		}
		return result; 
	}
}