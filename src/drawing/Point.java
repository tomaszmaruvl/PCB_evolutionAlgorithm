package drawing;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "x: " + this.x + " y: " + this.y;
    }

    public String usedString(){
        return "x"+this.x+"y"+this.y;
    }

}
