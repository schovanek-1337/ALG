package arrayofpoints;

/**
 *
 * @author janvit
 */
public class ArrayOfPoints {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Point p = new Point();
        System.out.println(p);
        Point p1 = new Point(4, 3);
        System.out.println(p1);
        System.out.println(p1.getDistance());
        Point p2 = new Point(6, 3);
        
        System.out.println(p1.distanceTo(p2));
        System.out.println(Point.distanceBetween(p1, p2));
        System.out.println(PointsLibrary.distanceBetween(p1, p2));
    }
    
}
