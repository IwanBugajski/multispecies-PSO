package pl.edu.agh.mpso.chart;

public class Point {
	public final double x;
	public final double y;
	public double deviation = 0.0;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(double x, double y, double deviation) {
		this(x, y);
		this.deviation = deviation;
	}
}
