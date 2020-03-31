package arrayofpoints;

/**
 *
 * @author janvit
 */
public class Point {
    //data
    private double x;
    private double y;
    //vypoctena hodnota
    //x, y se nemeni, vypoctu jednou v konstruktoru a pak vracim pomoci gettru
    private double distance = 0; 
    
    //constructors
    public Point(){
        this.x = 0;
        this.y = 0;
        this.distance = this.distance();
    }
    
    //overloaded constructor
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.distance = this.distance();
    }
    //getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override //prekryta, overriden
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }
    
    //metody
    //vzdalenost od pocatku
    private double distance(){
        return Math.sqrt(x*x + y*y);
        //return distanceTo(new Point(0,0)); //prepouziti jine metody
    }

    public double getDistance() {
        return distance;
    }
    
    public double distanceTo(Point p){
        return Math.sqrt(Math.pow(this.x-p.x, 2) + Math.pow(this.y - p.y, 2));
    }
    
    //staticka, nevola se na objekt
    public static double distanceBetween(Point p1, Point p2){
        return Math.sqrt(Math.pow(p1.x-p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}
