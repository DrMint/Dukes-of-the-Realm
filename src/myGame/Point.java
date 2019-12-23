package myGame;

import java.util.function.Predicate;

public class Point {
	
	public int x;
	public int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point() {
		this(0, 0);
	}
	
	public boolean equals(Point p) {
		return p.x == this.x && p.y == this.y;
	}

	public Point add(Point p) {
		return new Point(this.x + p.x, this.y + p.y);
	}
	
	public Point add(int x, int y) {
		return new Point(this.x + x, this.y + y);
	}
	
	public void translate(Point p) {
		this.x += p.x;
		this.y += p.y;
	}
	
	public void translate(int x, int y) {
		this.x += x;
		this.y += y;
	}
	
	public Point scalar(int i) {
		return new Point(this.x * i, this.y * i);
	}
	
	public Point scalar(float i) {
		return new Point((int) (i * this.x), (int) (i * this.y));
	}
	
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	
	public static Predicate<Point> PredicatIsEquals(Point p2) 
	{
	    return p -> p.equals(p2);
	}
	
}
