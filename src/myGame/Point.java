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
public class Point implements java.io.Serializable {
	
	private static final long serialVersionUID = 9198812747851892592L;
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

	public Point add(int x, int y) {
		return new Point(this.x + x, this.y + y);
	}
	
	public Point add(Point p) {
		return this.copy().add(p.x, p.y);
	}
	
	public Point add(Direction d) {
		return this.copy().add(d.toPoint());
	}
	
	public void translate(int x, int y) {
		this.x += x;
		this.y += y;
	}
	
	public void translate(Point p) {
		translate(p.x, p.y);
	}
	
	public void translate(Direction d) {
		translate(d.toPoint());
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
