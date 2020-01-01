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
	
	/**
	 * Its coordinate in the X-axis.
	 * I chose to make it public so that the code written is easier.
	 */
	public int x;
	
	/**
	 * Its coordinate in the Y-axis.
	 * I chose to make it public so that the code written is easier.
	 */
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

	/**
	 * Returns a new point with translated coordinates.
	 * @param x the translation on the X-axis.
	 * @param y the translation on the Y-axis.
	 * @return a new point with translated coordinates.
	 */
	public Point add(int x, int y) {
		return new Point(this.x + x, this.y + y);
	}
	
	/**
	 * Returns a new point with translated coordinates.
	 * Adds the values of both points.
	 * @param p the point used for translation.
	 * @return a new point with translated coordinates.
	 */
	public Point add(Point p) {
		return this.copy().add(p.x, p.y);
	}
	
	/**
	 * Return a new point with translated coordinates by 1 in given direction.
	 * @param d the direction to translation toward.
	 * @return a new point with translated coordinates.
	 */
	public Point add(Direction d) {
		return this.copy().add(d.toPoint());
	}
	
	/**
	 * Translate the point accordingly to its parameters.
	 * @param x the translation on the X-axis.
	 * @param y the translation on the Y-axis.
	 */
	public void translate(int x, int y) {
		this.x += x;
		this.y += y;
	}
	
	/**
	 * Translate the point by adding the values of the other point.
	 * @param p the point used for translation.
	 */
	public void translate(Point p) {
		translate(p.x, p.y);
	}
	
	/**
	 * Translate the point by 1 in given direction.
	 * @param d the direction to translation toward.
	 */
	public void translate(Direction d) {
		translate(d.toPoint());
	}
	
	/**
	 * Returns a new point with its coordinates multiplied by a number.
	 * @param i the number to multiple the coordinates with.
	 * @return a new point with its coordinates multiplied by a number.
	 */
	public Point scalar(int i) {
		return new Point(this.x * i, this.y * i);
	}
	
	/**
	 * Returns a new point with its coordinates multiplied by a number.
	 * @param f the number to multiple the coordinates with.
	 * @return a new point with its coordinates multiplied by a number.
	 */
	public Point scalar(float f) {
		return new Point((int) (f * this.x), (int) (f * this.y));
	}
	
	/**
	 * @return a copy of the original point.
	 */
	public Point copy() {
		return new Point(x, y);
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
