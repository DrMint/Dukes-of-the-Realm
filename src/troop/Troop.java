package troop;
import javafx.scene.shape.Rectangle;
import myGame.Point;
import myGame.Settings;

public abstract class Troop {
	private int costProduction;
	private int timeProduction;
	private int speed;
	private int health;
	private int damage;
	private Point location;
	private Rectangle shape = new Rectangle();
	
	public Troop(int costProduction, int timeProduction, int speed, int health, int damage) {
		this.costProduction = costProduction;
		this.timeProduction = timeProduction;
		this.speed = speed;
		this.health = health;
		this.damage = damage;
		
	}
	
	public void move(Point target) {
		
		int sizeCastle = Settings.CASTLES_SIZE;
		
		int diffX;
		int diffY;
		
		for (int i = 0; i < speed; i++) {
			diffX = target.x + 2 - this.location.x;
			diffY = target.y + 2 - this.location.y;
			
			if (diffX < -1 || diffX > sizeCastle) {
				if (diffY < -1 || diffY > sizeCastle) {
					if (Math.abs(diffX) >= Math.abs(diffY)) {
						if (diffX >= 0) {
							this.location.x++;
						} else {
							this.location.x--;
						}
					} else {
						if (diffY >= 0) {
							this.location.y++;
						} else {
							this.location.y--;
						}
					}
				} else {
					if (diffX >= 0) {
						this.location.x++;
					} else {
						this.location.x--;
					}
				}
			} else {
				if (diffY < -1 || diffY > sizeCastle) {
					if (diffY >= 0) {
						this.location.y++;
					} else {
						this.location.y--;
					}
				}
			}
		}
		

		
	}

	public int getCostProduction() {
		return costProduction;
	}

	public int getTimeProduction() {
		return timeProduction;
	}

	public int getSpeed() {
		return speed;
	}

	public int getHealth() {
		return health;
	}

	public int getDamage() {
		return damage;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Rectangle getShape() {
		return shape;
	}

	public void setShape(Rectangle shape) {
		this.shape = shape;
	}
	
	
	
}
