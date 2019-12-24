package myGame;

import java.util.function.Predicate;

/**
 * A class to use points. I decided to use integer for the coordinates.
 * By default, every points are initialize at (0, 0).
 * 
 * @author Thomas Barillot and Maël Bouquinet
 * @version 1.0
 * @since 2019-12-23
 */
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
	
	/**
	 * Tests if the two points are equals
	 * @param p	the point to compare with
	 * @return	true if they are equal, otherwise false 
	 */
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
	
	public Point copy() {
		return new Point(x, y);
	}
	
	public Point scalar(float i) {
		return new Point((int) (i * this.x), (int) (i * this.y));
	}
	
	/**
	 * A predicate used to test if two points are equal
	 * @param p2	the point to compare with
	 * @return		true if they are equal, otherwise false
	 */
	public static Predicate<Point> PredicatIsEquals(Point p2) {
	    return p -> p.equals(p2);
	}
	
}
