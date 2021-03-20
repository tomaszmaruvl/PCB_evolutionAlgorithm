import java.util.ArrayList;
import java.util.Random;

import drawing.Point;

public class Track {

    public Direction[] directions = Direction.values();
    public ArrayList<Segment> track;
    public Point startingPoint;
    public Point endingPoint;

    public Track(Point begin, Point end) {
        this.startingPoint = begin;
        this.endingPoint = end;
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
        for (int i = 1; i < 30; i++) {
            mutateTrackOneSegment();
            mutateTrackSplitSegment();
        }
    }

    public void mutateTrackOneSegment() {
        Random generator = new Random();
        int trackSize = this.track.size();
        int elementOfTrack = generator.nextInt(trackSize);
        int elementBefore = elementOfTrack == 0 ? 0 : elementOfTrack - 1;
        int elementAfter = elementOfTrack == trackSize ? trackSize : elementOfTrack + 1;
        Segment chosenSegment = this.track.get(elementOfTrack);
        if (chosenSegment.isVertical()) {
            Direction moveDirection = getRandomDirection("horizontal", generator);
            this.track.add(elementBefore, new Segment(1, moveDirection));
            this.track.add(elementAfter + 1, new Segment(1, getOppositeDirection(moveDirection)));
        } else {
            Direction moveDirection = getRandomDirection("vertical", generator);
            this.track.add(elementBefore, new Segment(1, moveDirection));
            this.track.add(elementAfter + 1, new Segment(1, getOppositeDirection(moveDirection)));
        }
        normalizeTrack();
        while (hasZeros()) {
            removeZeroLengthSegments();
            normalizeTrack();
        }
    }

    public void mutateTrackSplitSegment() {
        Random generator = new Random();
        int trackSize = this.track.size();
        int elementOfTrack = generator.nextInt(trackSize);
        int elementBefore = elementOfTrack == 0 ? 0 : elementOfTrack - 1;
        int elementAfter = elementOfTrack == trackSize ? trackSize : elementOfTrack + 1;
        Segment chosenSegment = this.track.get(elementOfTrack);
        int initialLenght = chosenSegment.lenght;
        int placeOfSplit = generator.nextInt(chosenSegment.lenght);
        this.track.get(elementOfTrack).lenght = placeOfSplit;
        this.track.add(elementAfter, new Segment(initialLenght - placeOfSplit, chosenSegment.direction));
        chosenSegment = this.track.get(elementOfTrack);
        if (chosenSegment.isVertical()) {
            Direction moveDirection = getRandomDirection("horizontal", generator);
            this.track.add(elementBefore, new Segment(1, moveDirection));
            this.track.add(elementAfter + 1, new Segment(1, getOppositeDirection(moveDirection)));
        } else {
            Direction moveDirection = getRandomDirection("vertical", generator);
            this.track.add(elementBefore, new Segment(1, moveDirection));
            this.track.add(elementAfter + 1, new Segment(1, getOppositeDirection(moveDirection)));
        }
        normalizeTrack();
        while (hasZeros()) {
            removeZeroLengthSegments();
            normalizeTrack();
        }
    }

    public ArrayList<Point> getTrackCoordinated() {
        ArrayList<Point> result = new ArrayList<Point>();
        result.add(this.startingPoint);
        for (int i = 0; i < track.size(); i++) {
            Point prevoius = result.get(result.size() - 1);
            Segment actualSegment = track.get(i);
            switch (actualSegment.direction) {
            case DOWN:
                result.add(new Point(prevoius.x, prevoius.y - actualSegment.lenght));
                break;
            case UP:
                result.add(new Point(prevoius.x, prevoius.y + actualSegment.lenght));
                break;
            case LEFT:
                result.add(new Point(prevoius.x - actualSegment.lenght, prevoius.y));
                break;
            case RIGHT:
                result.add(new Point(prevoius.x + actualSegment.lenght, prevoius.y));
                break;
            }
        }
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
        ArrayList<Integer> toRemove = new ArrayList<Integer>();
        for (int i = 0; i < this.track.size() - 1; i++) {
            Segment actualSegment = this.track.get(i);
            Segment nextSegment = this.track.get(i + 1);
            if (nextSegment.direction == actualSegment.direction) {
                actualSegment.lenght += nextSegment.lenght;
                toRemove.add(i + 1);
            }
            if (nextSegment.direction == getOppositeDirection(actualSegment.direction)) {
                actualSegment.lenght = actualSegment.lenght - nextSegment.lenght;
                if (actualSegment.lenght < 0) {
                    actualSegment.lenght = Math.abs(actualSegment.lenght);
                    actualSegment.direction = getOppositeDirection(actualSegment.direction);
                }
                toRemove.add(i + 1);
            }
        }
        ArrayList<Segment> normalizedTrack = new ArrayList<Segment>();
        for (int i = 0; i < this.track.size(); i++) {
            if (!toRemove.contains(i))
                normalizedTrack.add(this.track.get(i));
        }
        this.track = normalizedTrack;
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
            if (i != this.track.size() - 1) {
                if (startX == this.endingPoint.x && startY == this.endingPoint.y)
                    return false;
            }
        }
        if (startX == this.endingPoint.x && startY == this.endingPoint.y)
            return true;
        return false;
    }
}
