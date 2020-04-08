package pkg05_shapes;

/**
 * Nemenny immutable
 *
 * @author vit.schovanek
 */
public class Rectangle implements Shape {
	//data

	private double a;
	private double b;
	private double area; //vypocitana
	private double circumference;

	public Rectangle(double a, double b) {
		this.a = a;
		this.b = b;
		this.area = area();
	}

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}

	private double area() {
		return a * b;
	}

	private double circumference() {
		return 2 * a + 2 * b;
	}

	//neni ted potreba
	public double getArea() {
		return area;
	}

	@Override
	public double computeArea() {
		return area;
	}

	@Override
	public String toString() {
		return "Rectangle{" + "a=" + a + ", b=" + b + '}';
	}

	@Override
	public double computeCircumference() {
		return circumference;
	}
}
