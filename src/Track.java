import java.util.ArrayList;
import java.util.Random;

import drawing.Point;

public class Track {

    public Direction[] directions = Direction.values();
    public ArrayList<Segment> track;
    public Point startingPoint;
    public Point endingPoint;
    public Random generator = new Random();

    public Track(Point begin, Point end) {
        this.startingPoint = begin;
        this.endingPoint = end;
    }

    public Track(Track trackToCopy) {
        this.track = trackToCopy.track != null ? new ArrayList<Segment>(trackToCopy.track) : null;
        this.startingPoint = trackToCopy.startingPoint;
        this.endingPoint = trackToCopy.endingPoint;
    }

    public void setSimpliestTrack() {
        this.track = new ArrayList<Segment>();
        int x_dif = this.endingPoint.x - this.startingPoint.x;
        int y_dif = this.endingPoint.y - this.startingPoint.y;
        Segment seg1 = new Segment(Math.abs(x_dif), (x_dif > 0 ? Direction.RIGHT : Direction.LEFT));
        Segment seg2 = new Segment(Math.abs(y_dif), (y_dif > 0 ? Direction.UP : Direction.DOWN));
        track.add(seg1);
        track.add(seg2);
        removeZeroLengthSegments();
    }

    public void setRandomTrack() {
        do {
            setSimpliestTrack();
            randomizeTrack();
        } while (!verifyTrack());
    }

    public void randomizeTrack() {
        for (int i = 1; i < 50; i++) {
            if (generator.nextInt(3) < 2) {
                mutateTrackOneSegment(generator.nextInt(3) + 1);
            } else {
                mutateTrackSplitSegment(generator.nextInt(3) + 1);
            }
        }
    }

    public void mutateTrackOneSegment(int length) {
        Random generator = new Random();
        int trackSize = this.track.size();
        int elementOfTrack = generator.nextInt(trackSize);
        int elementBefore = elementOfTrack == 0 ? 0 : elementOfTrack - 1;
        int elementAfter = elementOfTrack == trackSize ? trackSize : elementOfTrack + 1;
        Segment chosenSegment = this.track.get(elementOfTrack);
        if (chosenSegment.isVertical()) {
            Direction moveDirection = getRandomDirection("horizontal", generator);
            this.track.add(elementOfTrack, new Segment(length, moveDirection));
            this.track.add(elementAfter + 1, new Segment(length, getOppositeDirection(moveDirection)));
        } else {
            Direction moveDirection = getRandomDirection("vertical", generator);
            this.track.add(elementOfTrack, new Segment(length, moveDirection));
            this.track.add(elementAfter + 1, new Segment(length, getOppositeDirection(moveDirection)));
        }
        normalizeTrack();

    }

    public void mutateTrackSplitSegment(int length) {
        ArrayList<Segment> p = Helpers.cloneArrayListSegment(this.track);
        int trackSize = this.track.size();
        int trackToSplitIndex = generator.nextInt(trackSize);
        int trackToSplitSize = this.track.get(trackToSplitIndex).lenght;
        Direction trackToSplitDirection = this.track.get(trackToSplitIndex).direction;
        int beginOfNewSegment = generator.nextInt(trackToSplitSize);
        int lengthofNewSegment = generator.nextInt(trackToSplitSize - beginOfNewSegment);
        lengthofNewSegment = lengthofNewSegment == 0 ? 1 : lengthofNewSegment;
        int segmentToMutate = trackToSplitIndex;
        Segment chosenSegment = this.track.get(segmentToMutate);
        if (beginOfNewSegment != 0) {
            this.track.set(trackToSplitIndex, new Segment(beginOfNewSegment, trackToSplitDirection));
            this.track.add(trackToSplitIndex + 1, new Segment(lengthofNewSegment, trackToSplitDirection));
            this.track.add(trackToSplitIndex + 2,
                    new Segment(trackToSplitSize - beginOfNewSegment - lengthofNewSegment, trackToSplitDirection));
            segmentToMutate = trackToSplitIndex + 1;
            chosenSegment = this.track.get(segmentToMutate);
        } else {
            this.track.set(trackToSplitIndex, new Segment(lengthofNewSegment, trackToSplitDirection));
            this.track.add(trackToSplitIndex + 1,
                    new Segment(trackToSplitSize - beginOfNewSegment - lengthofNewSegment, trackToSplitDirection));
            segmentToMutate = generator.nextInt(2) == 0 ? trackToSplitIndex : trackToSplitIndex + 1;
            chosenSegment = this.track.get(segmentToMutate);
        }
        // int segmentToMutate = trackToSplitIndex;
        // Segment chosenSegment = this.track.get(segmentToMutate);
        // if (trackToSplitSize != 1) {
        // int trackToSplitPlaceOfSplit = generator.nextInt(trackToSplitSize);
        // trackToSplitPlaceOfSplit = trackToSplitPlaceOfSplit == 0 ? 1 :
        // trackToSplitPlaceOfSplit;
        // this.track.set(trackToSplitIndex, new Segment(trackToSplitPlaceOfSplit,
        // trackToSplitDirection));
        // this.track.add(trackToSplitIndex + 1,
        // new Segment(trackToSplitSize - trackToSplitPlaceOfSplit,
        // trackToSplitDirection));
        // segmentToMutate = generator.nextInt(2) == 0 ? trackToSplitIndex :
        // trackToSplitIndex + 1;
        // chosenSegment = this.track.get(segmentToMutate);
        // }
        if (chosenSegment.isVertical()) {
            Direction moveDirection = getRandomDirection("horizontal", generator);
            this.track.add(segmentToMutate, new Segment(length, moveDirection));
            this.track.add(segmentToMutate + 2, new Segment(length, getOppositeDirection(moveDirection)));
        } else {
            Direction moveDirection = getRandomDirection("vertical", generator);
            this.track.add(segmentToMutate, new Segment(length, moveDirection));
            this.track.add(segmentToMutate + 2, new Segment(length, getOppositeDirection(moveDirection)));
        }
        if(!verifyTrack()){
            int k = 0;
        }
        normalizeTrack();
    }

    public ArrayList<Point> getTrackCoordinated() {
        ArrayList<Point> result = new ArrayList<Point>();
        result.add(this.startingPoint);
        for (int i = 0; i < track.size(); i++) {
            Point prevoius = result.get(result.size() - 1);
            Segment actualSegment = track.get(i);
            switch (actualSegment.direction) {
            case DOWN:
                for (int j = 1; j <= actualSegment.lenght; j++) {
                    result.add(new Point(prevoius.x, prevoius.y - j));
                }
                break;
            case UP:
                for (int j = 1; j <= actualSegment.lenght; j++) {
                    result.add(new Point(prevoius.x, prevoius.y + j));
                }
                break;
            case LEFT:
                for (int j = 1; j <= actualSegment.lenght; j++) {
                    result.add(new Point(prevoius.x - j, prevoius.y));
                }
                break;
            case RIGHT:
                for (int j = 1; j <= actualSegment.lenght; j++) {
                    result.add(new Point(prevoius.x + j, prevoius.y));
                }
                break;
            }
        }
        //result.add(endingPoint);
        return result;
    }

    public Direction getRandomDirection(String axis, Random generator) {
        Direction[] horizontal = { Direction.LEFT, Direction.RIGHT };
        Direction[] vertical = { Direction.DOWN, Direction.UP };
        switch (axis) {
        case "horizontal":
            return horizontal[generator.nextInt(2)];
        case "vertical":
            return vertical[generator.nextInt(2)];
        }
        return Direction.UP;
    }

    public Direction getOppositeDirection(Direction dir) {
        switch (dir) {
        case DOWN:
            return Direction.UP;
        case UP:
            return Direction.DOWN;
        case LEFT:
            return Direction.RIGHT;
        case RIGHT:
            return Direction.LEFT;
        }
        return Direction.UP;
    }

    public void removeZeroLengthSegments() {
        ArrayList<Integer> zeros = new ArrayList<Integer>();
        for (int i = 0; i < this.track.size(); i++) {
            if (this.track.get(i).lenght == 0)
                zeros.add(i);
        }
        ArrayList<Segment> normalizedTrack = new ArrayList<Segment>();
        for (int i = 0; i < this.track.size(); i++) {
            if (!zeros.contains(i))
                normalizedTrack.add(this.track.get(i));
        }
        this.track = normalizedTrack;
    }

    public void normalizeTrack() {
        if (!verifyTrack()) {
            int k = 0;
        }
        ArrayList<Segment> copy = Helpers.cloneArrayListSegment(this.track);
        ArrayList<Point> coordinated = getTrackCoordinated();
        ArrayList<Point> fixed = new ArrayList<Point>();
        ArrayList<String> used = new ArrayList<String>();
        for (int i = 0; i < coordinated.size(); i++) {
            if (i == 0) {
                fixed.add(coordinated.get(i));
                used.add(coordinated.get(i).usedString());
            } else if (i == coordinated.size() - 1) {
                fixed.add(coordinated.get(coordinated.size() - 1));
                used.add(coordinated.get(i).usedString());
            } else {
                Point before = coordinated.get(i - 1);
                Point after = coordinated.get(i + 1);
                // if (!(before.x == after.x && before.y == after.y)) {
                String actualUsedString = coordinated.get(i).usedString();
                if (!used.contains(actualUsedString)) {
                    fixed.add(coordinated.get(i));
                    used.add(actualUsedString);
                } else {
                    int l = 0;
                    while (!fixed.remove(fixed.size() - 1).usedString().equals(actualUsedString)) {
                        used.remove(used.size() - 1);
                    }
                    fixed.add(coordinated.get(i));
                    // used.add(actualUsedString);
                }
            }
        }

        // }
        this.track = fromPointsToSegments(fixed);
        if (!verifyTrack()) {
            int klo = 2;
            this.track = copy;
            boolean k = verifyTrack();
            getTrackCoordinated();
        }
        if (hasZeros()) {
            int a = 0;
        }
    }

    public ArrayList<Segment> fromPointsToSegments(ArrayList<Point> in) {
        ArrayList<Segment> result = new ArrayList<Segment>();
        boolean treatAsFirst = true;
        for (int i = 0; i < in.size() - 1; i++) {
            if (treatAsFirst) {
                if (!(in.get(i).x == in.get(i + 1).x && in.get(i).y == in.get(i + 1).y)) {
                    result.add(new Segment(1, getDirectionFromPoints(in.get(i), in.get(i + 1))));
                    treatAsFirst = false;
                }
            } else {
                if (!(in.get(i).x == in.get(i + 1).x && in.get(i).y == in.get(i + 1).y)) {
                    if (result.get(result.size() - 1).direction == getDirectionFromPoints(in.get(i), in.get(i + 1))) {
                        result.get(result.size() - 1).lenght += 1;
                    } else {
                        result.add(new Segment(1, getDirectionFromPoints(in.get(i), in.get(i + 1))));
                    }

                }
            }
        }
        return result;
    }

    public boolean hasZeros() {
        for (int i = 0; i < this.track.size(); i++) {
            if (this.track.get(i).lenght == 0)
                return true;
        }
        return false;
    }

    public boolean verifyTrack() {
        int startX = this.startingPoint.x;
        int startY = this.startingPoint.y;
        for (int i = 0; i < this.track.size(); i++) {
            Segment actualSegment = this.track.get(i);
            if (actualSegment.direction == null) {
                return false;
            }
            switch (actualSegment.direction) {
            case UP:
                startY += actualSegment.lenght;
                break;
            case DOWN:
                startY -= actualSegment.lenght;
                break;
            case LEFT:
                startX -= actualSegment.lenght;
                break;
            case RIGHT:
                startX += actualSegment.lenght;
                break;
            }
            // if (i != this.track.size() - 1) {
            // if (startX == this.endingPoint.x && startY == this.endingPoint.y)
            // return false;
            // }
        }
        if (startX == this.endingPoint.x && startY == this.endingPoint.y)
            return true;
        return false;
    }

    public int calculateTotalTrackLength() {
        int result = 0;
        for (int i = 0; i < this.track.size(); i++) {
            if (i != 0) {
                if (this.track.get(i).direction == getOppositeDirection(this.track.get(i - 1).direction)) {
                    result -= this.track.get(i).lenght;
                } else {
                    result += this.track.get(i).lenght;
                }
            } else {
                result += this.track.get(i).lenght;
            }
        }
        return result;
    }

    public Direction getDirectionFromPoints(Point a, Point b) {
        if (b.x - a.x == 0) {
            if (b.y - a.y < 0) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        } else if (b.y - a.y == 0) {
            if (b.x - a.x > 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        }
        return null;
    }
}
