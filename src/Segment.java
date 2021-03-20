public class Segment {
    public int lenght;
    public Direction direction;

    public Segment(int length, Direction direction){
        this.direction = direction;
        this.lenght = length;
    }

    public boolean isVertical(){
        switch (this.direction){
            case DOWN: return true;
            case UP: return true;
            case RIGHT: return false;
            case LEFT: return false;
        }
        return false;
    }

    public String toString() {
        return lenght + " " + direction;
    }
}
