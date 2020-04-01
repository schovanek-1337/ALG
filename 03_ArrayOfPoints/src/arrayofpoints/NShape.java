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
	public NShape() {

	}

	public NShape(Point[] points) {
		for (Point p : points) {
			this.points.add(p);
		}
	}

	public NShape(ArrayList<Point> points) {
		this.points.addAll(points);
	}

	public void add(Point p) {
		points.add(p);
	}

	public void add(double x, double y) {
		points.add(new Point(x, y));
	}

	public double perim() {
		double result = 0;
		for (int i = 0; i < points.size() - 1; i++) {
			result += points.get(i).distanceTo(points.get(i + 1));
		}
		result += points.get(0).distanceTo(points.get(points.size()));
		return result;
	}

	public Point getPointAt(int index) {
		return points.get(index);
	}

	public Point getNearest() {
		double min = Double.MAX_VALUE;
		Point nearest = null; //inicializace objektu
		double distance;
		for (Point point : points) { //prechadzanie ArrayListu pomocou foreach
			distance = point.getDistance();
			if (distance < min) {
				min = distance;
				nearest = point;
			}
		}
		return nearest;
	}

	public Point getFurthest() {
		Point winner = points.get(0);
		for (Point p : points) {
			if (winner.getDistance() < p.getDistance()) {
				winner = p;
			}
		}
		return winner;
	}

	public double minDistanceBetween() {
		int i = 0;
		int j = 1;
		double distance = 0;
		double currDistance; //syntax sugar
		while (i < points.size()) {
			currDistance = points.get(i).distanceTo(points.get(j));
			if (distance > currDistance) {
				distance = currDistance;
			}
			if (j < points.size() - 1) {
				j++;
			} else {
				i++;
				j = i;
			}
		}
		return distance;
	}

	public double maxDistanceBetween() {
		int i = 0;
		int j = 1;
		double distance = 0;
		double currDistance; //syntax sugar
		while (i < points.size()) {
			currDistance = points.get(i).distanceTo(points.get(j));
			if (distance < currDistance) {
				distance = currDistance;
			}
			if (j < points.size() - 1) {
				j++;
			} else {
				i++;
				j = i;
			}
		}
		return distance;
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
		myShape.add(6, 8);
		myShape.add(11, 34);
		myShape.add(129, 22);
		System.out.println(myShape.minDistanceBetween());
	}
}
