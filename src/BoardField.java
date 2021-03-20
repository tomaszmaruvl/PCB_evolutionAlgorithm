import drawing.Point;

public class BoardField {
    public Point point;
    public int usedQuant;

    public BoardField(Point newPoint){
        this.point = newPoint;
        this.usedQuant = 0;
    }

    public void resetField(){
        this.usedQuant = 0;
    }

    public void incrementQuant() {
        this.usedQuant += 1;
    }
}
