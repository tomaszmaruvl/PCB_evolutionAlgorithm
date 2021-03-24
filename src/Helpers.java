import java.util.ArrayList;

public class Helpers {

    static ArrayList<Track> cloneArrayList(ArrayList<Track> listToClone) {
        ArrayList<Track> result = new ArrayList<Track>();
        for (int i = 0; i < listToClone.size(); i++) {
            Track trackToClone = listToClone.get(i);
            result.add(new Track(trackToClone));

        }
        return result;
    }

    static ArrayList<Segment> cloneArrayListSegment(ArrayList<Segment> listToClone) {
        ArrayList<Segment> result = new ArrayList<Segment>();
        for (int i = 0; i < listToClone.size(); i++) {
            Segment segmentToClone = listToClone.get(i);
            result.add(new Segment(segmentToClone));

        }
        return result;

    }
}
