package arrayofpoints;

/**
 *
 * @author janvit
 */
public class MainStaticArray {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Point[] a = new Point[4];
        a[0] = new Point(3, 4);
        a[1] = new Point(6, 4);
        a[2] = new Point(6, 0);
        a[3] = new Point(3, 0);
        
        System.out.println(perimeter(a));
    }

    public static double perimeter(Point[] points) {
        // a[0] a[1] a[2] a[3] a[0]
        double perim = 0;
        for (int i = 0; i < points.length - 1; i++) {
            perim = perim + points[i].distanceTo(points[i + 1]); //points[i] ukazuje na nejaky bod  
        }
        perim = perim + points[points.length - 1].distanceTo(points[0]);
        return perim;
    }

}
