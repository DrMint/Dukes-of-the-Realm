package myGame;

/**
 * Give a level of abstraction when speaking about direction.
 * The developer can use functions such as isNorth() instead on relying
 * on arbitrary values.
 * @author Thomas Barillot and Maël Bouquinet
 * @version 1.0
 * @since   2019-12-23
 *
 */
public class Direction {
	/**
	 * 0 = North
	 * 1 = East
	 * 2 = South
	 * 3 = West
	 */
	private int direction = 0;
	
	public Direction() {}
	
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
	
	public Direction copy() {
		Direction tmp = new Direction();
		tmp.direction = this.direction;
		return tmp;
	}
	
	public boolean equals(Direction d) {
		return d.direction == this.direction;
	}
	
	public void turnClockwise() {
		this.direction += 1;
		if (this.direction > 3) this.direction -= 4;
	}
	
	public void turnCounterClockwise() {
		this.direction -= 1;
		if (this.direction < 0) this.direction += 4;
	}
	
	public Point toPoint() {
		switch (direction) {
			case 0: return new Point(0,-1);
			case 1: return new Point(1, 0);
			case 2: return new Point(0, 1); 
			case 3: return new Point(-1, 0);
			default: return new Point();
		}
	}
	
	public void randomize() {
		switch(Main.getRandomIntegerBetweenRange(0, 4)) {
			case 0: this.setNorth(); return;
			case 1: this.setEast(); return;
			case 2: this.setSouth(); return;
			case 3: this.setWest(); return;
		}
	}
	
	public boolean isNorth(){return direction == 0;}
	public boolean isEast(){return direction == 1;}
	public boolean isSouth(){return direction == 2;}
	public boolean isWest(){return direction == 3;}
	
	public void setNorth(){direction = 0;}
	public void setEast(){direction = 1;}
	public void setSouth(){direction = 2;}
	public void setWest(){direction = 3;}
	
	
	
}
