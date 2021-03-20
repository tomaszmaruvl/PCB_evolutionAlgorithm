
import java.awt.EventQueue;
import java.util.ArrayList;

import drawing.MyFrame;
import drawing.Point;

public class Draw {

	public static void main(String[] args) {
		Loader loader = new Loader("C:/Users/Tomasz Marulewski/Desktop/_nowe_SI/PCB_evolutionAlgorithm/problems/zad3.txt");
		try {
			loader.load();
		} catch (Exception e) {
			Object x = e;
		}

		ArrayList<ArrayList<Point>> toDraw = prepareTracksToDraw(loader.connections, false, loader.rows, loader.columns);

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MyFrame(loader.rows, loader.columns, 30, 20, toDraw);

			}
		});
	}

	public static ArrayList<ArrayList<Point>> prepareTracksToDraw( ArrayList<Track> tracks, boolean tracksExist, int rows, int columns){
		ArrayList<ArrayList<Point>> result = new ArrayList<ArrayList<Point>>();
		for (int i = 0 ; i < tracks.size() ; i++){
			Track n = tracks.get(i);
			if (!tracksExist) n.setRandomTrack();
			for(int j = 0 ; j<10 ;j++){
			// n.mutateTrackOneSegment();
			// n.mutateTrackSplitSegment();
			
		}
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