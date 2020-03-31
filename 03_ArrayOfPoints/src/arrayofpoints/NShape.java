package arrayofpoints;

import java.util.ArrayList;

/**
 *
 * @author janvit
 */
public class NShape {
    //data
    private ArrayList<Point> points = new ArrayList<>();

    //constructors
    //default prazdny konstruktor
    public NShape(){

    }

    //TODO
    public NShape (Point[] points){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //TODO
    public NShape (ArrayList<Point> points){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void add(Point p){
        points.add(p);
    }

    public void add(double x, double y){
        points.add(new Point(x, y));
    }

    //TODO vyuzit prochazeni ArrayListu po indexech
    public double perim(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Point getPointAt(int index){
        return points.get(index);
    }

    public Point getNearest(){
        double min = Double.MAX_VALUE;
        Point nearest = null; //inicializace objektu
        double distance;
        for (Point point : points) { //prechadzanie ArrayListu pomocou foreach
            distance = point.getDistance();
            if(distance < min){
                min = distance;
                nearest = point;
            }
        }
        return nearest;
    }

    public Point getFurthest(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //TODO vrati min vzdalenost mezi body
    public double minDistanceBetween(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //TODO vrati max vzdalenost mezi body
    public double maxDistanceBetween(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Point point : points) {
            sb.append(point.toString()).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        NShape myShape = new NShape();
        myShape.add(new Point(2, 3));
        myShape.add(2, 4);
        System.out.println(myShape.getNearest());
    }
}
