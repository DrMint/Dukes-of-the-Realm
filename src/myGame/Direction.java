package myGame;

/**
 * Give a level of abstraction when speaking about direction.
 * Allows to use functions such as isNorth() instead on relying
 * on arbitrary values.
 * @author Thomas Barillot and Maël Bouquinet
 * @version 1.0
 * @since   2019-12-23
 *
 */
public class Direction implements java.io.Serializable {

	private static final long serialVersionUID = -4635993413361705298L;
	
	/**
	 * 0 = North
	 * 1 = East
	 * 2 = South
	 * 3 = West
	 */
	private int direction = 0;
	
	/**
	 * By default, the direction in north.
	 */
	public Direction() {}
	
	/**
	 * Generate a direction from a point.
	 * <p>
	 * For example, Point(0, 1) will create a south direction.
	 * It might be a better idea to create a direction and then set the direction
	 * with a function such as setSouth().
	 * @param p the point that will be used to generate the direction.
	 */
	public Direction(Point p) {
		if (p.y > 0) {
			this.direction = 0;
		} else if (p.y < 0) {
			this.direction = 2;
		} else if (p.x > 0) {
			this.direction = 1;
		} else {
			this.direction = 3;
		}
	}
	
	/**
	 * @return a copy of the original direction.
	 */
	public Direction copy() {
		Direction tmp = new Direction();
		tmp.direction = this.direction;
		return tmp;
	}
	
	/**
	 * @param d another direction
	 * @return	true if the direction are the same, otherwise false.
	 */
	public boolean equals(Direction d) {
		return d.direction == this.direction;
	}
	
	/**
	 * Turns the direction clockwise.
	 */
	public void turnClockwise() {
		this.direction += 1;
		if (this.direction > 3) this.direction -= 4;
	}
	
	/**
	 * Turns the direction counter-clockwise.
	 */
	public void turnCounterClockwise() {
		this.direction -= 1;
		if (this.direction < 0) this.direction += 4;
	}
	

	/**
	 * @return a normalized point (distance 1 from the origin) and with the appropriate direction. 
	 */
	public Point toPoint() {
		switch (direction) {
			case 0: return new Point(0,-1);
			case 1: return new Point(1, 0);
			case 2: return new Point(0, 1); 
			case 3: return new Point(-1, 0);
			default: return new Point();
		}
	}
	
	/**
	 * Randomize the direction.
	 */
	public void randomize() {
		switch(Main.getRandomIntegerBetweenRange(0, 4)) {
			case 0: this.setNorth(); return;
			case 1: this.setEast(); return;
			case 2: this.setSouth(); return;
			case 3: this.setWest(); return;
		}
	}
	
	
	/* GETTERS AND SETTERS */
	
	
	public boolean isNorth(){return direction == 0;}
	public boolean isEast(){return direction == 1;}
	public boolean isSouth(){return direction == 2;}
	public boolean isWest(){return direction == 3;}
	
	public void setNorth(){direction = 0;}
	public void setEast(){direction = 1;}
	public void setSouth(){direction = 2;}
	public void setWest(){direction = 3;}
	
	
	
}
