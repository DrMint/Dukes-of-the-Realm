package troop;

import javafx.scene.shape.Rectangle;
import myGame.Direction;
import myGame.Main;

import myGame.Point;
import myGame.Settings;

public abstract class Troop {
	private int costProduction;
	private int timeProduction;
	private int speed;
	private int health;
	private int maxHealth;
	private int damage;
	private Point location;
	private Rectangle shape = new Rectangle();
	private boolean canMove = true;
	private boolean hasArrived = false;
	private boolean turnClockwise = true;
	
	public Troop(int costProduction, int timeProduction, int speed, int health, int damage) {
		this.costProduction = costProduction;
		this.timeProduction = timeProduction;
		this.speed = speed;
		this.health = health;
		this.maxHealth = health;
		this.damage = damage;
		
	}
	
	
	/**
	 * Makes the troop move in direction of the target
	 * @param target 	the top left corner of the castle you wish the troop to go
	 */
	public void move(Point target) {
		if (!canMove) return;
		
		/* Put the target at the center of the castle */ 
		target.translate(Settings.CASTLES_SIZE / 2, Settings.CASTLES_SIZE / 2);
		for (int i = 0; i < speed; i++) {
			
			/* If the troop is next to the castle, stop moving */
			if (Math.abs(location.x - target.x) <= Settings.CASTLES_SIZE / 2 + 1 &&
				Math.abs(location.y - target.y) <= Settings.CASTLES_SIZE / 2 + 1)
			{
				this.canMove = false;
				this.hasArrived = true;
				break;
			}
			
			
			Direction nextMove = calculateDirectionForNextMove(target);
			
			/* If this next position is in a castle */
			if (Main.getCastleFromPoint(location.add(nextMove)) != null) {
				if (this.turnClockwise) {
					nextMove.turnClockwise();					
				} else {
					nextMove.turnCounterClockwise();	
				}
				
				/* If we apply this nextMove, will the next move's nextMove will be the same as my current position? 
				 * TL;DR: Will my poor troop be stuck in an endless loop? */
				if (location.add(nextMove).add(calculateDirectionForNextMove(location.add(nextMove), target)).equals(location)) {
					this.turnClockwise = !this.turnClockwise;
				}
			}
			
			location.translate(nextMove);
		}	
	}
	
	public Direction calculateDirectionForNextMove(Point location, Point target) {
		
		Direction result = new Direction();
		
		/* If the translation in the X axis is higher than the Y axis, do the X axis */
		if (Math.abs(location.x - target.x) > Math.abs(location.y - target.y)) {
			if (location.x < target.x) {
				result.setEast();
				return result;
			} else if (location.x > target.x) {
				result.setWest();
				return result;
			}
		} else {
			if (location.y < target.y) {
				result.setSouth();
				return result;
			} else if (location.y > target.y) {
				result.setNorth();
				return result;
			}
		}
		return null;
	}
	
	public Direction calculateDirectionForNextMove(Point target) {
		return calculateDirectionForNextMove(this.location, target);
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
	
	public void setHealth(int health) {
		this.health = health;
	}

	public int getDamage() {
		return damage;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location.copy();
	}

	public Rectangle getShape() {
		return shape;
	}

	public void setShape(Rectangle shape) {
		this.shape = shape;
	}
	
	public void refresh() {
		this.shape.setX(Main.gridStart.x + this.location.x * Main.gridSize);
		this.shape.setY(Main.gridStart.y + this.location.y * Main.gridSize);
    }

	public boolean canMove() {
		return canMove;
	}

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}

	public boolean hasArrived() {
		return hasArrived;
	}

	public void setHasArrived(boolean hasArrived) {
		this.hasArrived = hasArrived;
	}

	public int getMaxHealth() {
		return maxHealth;
	}
	
	
	
}
