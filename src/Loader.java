import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import drawing.Point;

public class Loader {

    String fileLocation;
    ArrayList<Track> connections;
    int rows;
    int columns;

    public Loader(String fileLocation) {
        this.fileLocation = fileLocation;
        this.connections = new ArrayList<Track>();
    }

    public void load() throws FileNotFoundException {
        File file = new File(fileLocation);
        Scanner input = new Scanner(file);
        List<String> list = new ArrayList<String>();

        while (input.hasNextLine()) {// Lista stringow z tekstu
            list.add(input.nextLine());
        }
        input.close();

        String[] coords = list.get(0).trim().split(";");
        this.rows = toInt(coords[0]);
        this.columns = toInt(coords[1]);

        for (int i = 1; i < list.size(); i++) {
            String[] xy = list.get(i).trim().split(";");
            Track track = new Track(new Point(toInt(xy[0]), toInt(xy[1])), new Point(toInt(xy[2]), toInt(xy[3])));
            this.connections.add(track);
        }
    }


    private int toInt(String strNumber) {
        return Integer.parseInt(strNumber);
    }
}