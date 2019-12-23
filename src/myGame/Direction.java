package myGame;

public class Direction {
	/**
	 * 0 = North
	 * 1 = East
	 * 2 = South
	 * 3 = West
	 */
	int direction = 0;
	
	public Direction(int direction) {
		if (direction >= 0 && direction <= 3) {	
			this.direction = direction;		
		}
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
	
	public boolean isNorth(){return direction == 0;}
	public boolean isEast(){return direction == 1;}
	public boolean isSouth(){return direction == 2;}
	public boolean isWest(){return direction == 3;}
	
	
	
}
