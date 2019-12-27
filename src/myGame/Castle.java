package myGame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import troop.Troop;

/**
 * A castle is an object in the game Duke of the Realm
 * It has a owner, some properties such as its treasury, level, position...
 * 
 * @author Thomas Barillot and Maël Bouquinet
 * @version 1.0
 * @since   2019-12-23
 */
public class Castle implements java.io.Serializable{

	private static final long serialVersionUID = -745739758419837966L;
	private transient Pane layer;
	private transient Rectangle shape;
	private String nickname;
	private Duke owner;
	private int money;
	private int level;
	private List<Troop> troops = new ArrayList<>();
	private List<Troop> troopsToDispatch = new ArrayList<>();
	private Point location;
	private List<Order> orders = new ArrayList<>();
	private List<Production> productions = new ArrayList<>();
	private Direction doorDirection;
	private int numProductionUnit = 1;
	private int numDispatchMax = 3;
	
	public Castle(String nickname, Duke owner, int money, int level, List<Troop> troops, Point location, Pane layer, Direction doorDirection) {
		this.nickname = nickname;
		this.owner = owner;
		this.money = money;
		this.level = level;
		this.troops.addAll(troops); // Make a copy of the list
		this.location = location;
		this.layer = layer;
		this.doorDirection = doorDirection;
	}
		
	/**
	 * @return the cost to level up the castle
	 */
	public int costToLevel() {
		int realLevel = this.level;
		for (Production production:productions) {
			if (production.isCastle()) realLevel++;
		}
		return 1000 * realLevel;
	}
	
	
	/**
	 * @return the number of ticks to level up
	 */
	public int timeToLevel() {
		int realLevel = this.level;
		for (Production production:productions) {
			if (production.isCastle()) realLevel++;
		}
		return 100 + 50 * realLevel;
	}
	
	
	/**
	 * 
	 */
	public void levelUp() {
		int cost = costToLevel();
		int time = timeToLevel();
		
		if (cost <= this.money) {
			this.money -= cost;
			productions.add(new Production(time, cost));
		}
	
	}
	
	/**
	 * Adds due income to the castle, propagate the tick to the production units,
	 * if a production if finished, remove it from the production line, dispatches units,
	 * remove empty orders.
	 */
	public void tick() {
		
		// Add money each tick. If the duke is neutral, it's 10% of the normal income.
		if (this.owner.isNeutral()) {
			this.money += this.level;
		} else {
			this.money += this.level * 10;
		}
		
		/* One production unit can only work on one production at the same time
		 * So only do numProductionUnit operation per tick */
		int numDone = 0;
		Iterator<Production> i = productions.iterator();
		while (i.hasNext() && numDone < this.numProductionUnit) {
			Production production = i.next();
			production.tick();
			numDone++;
		   
			if (production.isFinish()) {
				/* If the production was a castle update, levels up the castle.
				 * If not, add the troop in production to the castle's troops */
				if (production.isCastle()) {
					this.level++;
				} else {
					this.addTroop(production.getTroop());
				}
				i.remove();
			}
		}
		
		/* Dispatches up to numDispatchMax troop ready to be dispatch 
		 * Dispatches troops according to their speed (slowest troops first) */
		if (troopsToDispatch.size() != 0) {
			numDone = 0;
			troopsToDispatch.sort(Comparator.comparing(Troop::getSpeed));
			Iterator<Troop> i2 = troopsToDispatch.iterator();
			while (i2.hasNext() && numDone < this.numDispatchMax) {
				Troop troop = i2.next();
				troop.getShape().setVisible(true);
				troop.setCanMove(true);
				numDone++;
				i2.remove();
			}
		}
		
		/* For all orders, if empty deletes it, otherwise makes them move */
		Iterator<Order> i3 = orders.iterator();
		while (i3.hasNext()) {
			Order order = i3.next();
			if (order.getTroops().size() != 0) {
			} else {
				i3.remove();
			}
		}
	}

	
	/**
	 * Adds a troop to the castle's troops.
	 * When a troop is added, its health is replenished.
	 * @param troop	the troop that should be added
	 */
	public void addTroop(Troop troop) {
		troop.setHasArrived(false);
		troop.setHealth(troop.getMaxHealth());
		troops.add(troop);
	}
	
	/**
	 * Adds a troop to the production line.
	 * Remove its cost from the castle treasury.
	 * @param troop
	 */
	public void addProduction(Troop troop) {
		if (troop.getCostProduction() <= this.money) {
			this.money -= troop.getCostProduction();
			productions.add(new Production(troop));
		}
	}
	
	/**
	 * Add a new order to the castle.
	 * @param target	the castle the troops should move towards
	 * @param troops	the troops concerned by the order
	 */
	public void addOrder(Castle target, List<Troop> troops) {
		
		Point pos;
		Direction tmp;
		int positionModifier = 0;
		for (Troop troop:troops) {			
			pos = this.location.copy();
			// Place x and y at the center of the castle
			pos.translate(Settings.CASTLES_SIZE / 2,  Settings.CASTLES_SIZE / 2);
			// Move the unit toward the door
			pos.translate(this.doorDirection.toPoint().scalar(Settings.CASTLES_SIZE / 2 + Settings.SOLDIER_SIZE));
			
			// The following code allows the troops to be spawn at slightly different places
			// so that they doesn't superpose each others.
			tmp = this.doorDirection.copy();
			switch (positionModifier) {
				case 0: tmp.turnClockwise();pos.translate(tmp.toPoint().scalar(Settings.SOLDIER_SIZE)); break;
				case 1: tmp.turnCounterClockwise();pos.translate(tmp.toPoint().scalar(Settings.SOLDIER_SIZE)); break;
				case 2: pos.translate(tmp.toPoint().scalar(Settings.SOLDIER_SIZE)); break;
				default: break;
			}
			
			positionModifier++;
			if (positionModifier > 2) {positionModifier = 0;}
			
			troop.setLocation(pos);
			drawTroop(troop);
			troop.setCanMove(false);
			this.troopsToDispatch.add(troop);
		}
		
		/* If the order already exists, just append those new troops to it 
		for (Order order:orders) {
			if (order.getTarget() == target) {
				order.addTroops(troops);
				this.troops.removeAll(troops);
				return;
			}
		}*/
		
		/* Or else, create a new order and remove all those troops from the castle */
		this.orders.add(new Order(this, target, troops));
		this.troops.removeAll(troops);
	}
	
	/**
	 * Removes the first added element in production,
	 * meaning the oldest one.
	 */
	public void cancelProduction() {
		if (this.productions.size() > 0) {
			this.money += this.productions.get(0).getCost() * Settings.CANCEL_PRODUCTION_RETURN_RATIO;
			this.productions.remove(0);
		}
	}
	
	/**
	 * Removes all productions.
	 */
	public void cancelAllProduction() {
		for(Production production:productions) {
			this.money += production.getCost() * Settings.CANCEL_PRODUCTION_RETURN_RATIO;
		}
		this.productions.clear();
	}
	
	/**
	 * The castle's troops takes a certain amount of damage.
	 * If a troop's health gets to 0, he is removed from troops.
	 * @param damage	how much damage
	 */
	public void takesDamage(int damage) {
		Iterator<Troop> i = troops.iterator();
		while (i.hasNext()) {
			Troop troop = i.next();
			if (damage == 0) {break;}
			if (troop.getHealth() <= damage) {
				damage -= troop.getHealth();
				i.remove();
			} else {
				troop.setHealth(troop.getHealth() - damage);
				damage = 0;
			}
		}
	}
	
	
	/**
	 * Calculates how much damage the castle can deal.
	 * @return	how much damage the castle can deal
	 */
	public int dealDamage() {
		int result = 0;
		for(Troop troop:troops) {
			result += troop.getDamage();
		}
		return result;
	}
	
	
	
	
	
	/**
	 * Draw the castle and its door on the panel.
	 */
	public void drawSelf() {
		Point pos = new Point(Main.gridStart.x + this.location.x * Main.gridSize, Main.gridStart.y + this.location.y * Main.gridSize);
		this.shape = new Rectangle();
		this.shape.setX(pos.x);
		this.shape.setY(pos.y);
		this.shape.setWidth(Main.gridSize * Settings.CASTLES_SIZE);
		this.shape.setHeight(Main.gridSize * Settings.CASTLES_SIZE);
		this.shape.setFill(this.owner.getColor());
    	this.layer.getChildren().add(this.shape);
        
        int width;
        int height;
        
    	if (this.doorDirection.isNorth() || this.doorDirection.isSouth()) {
    		width = (int) (Main.gridSize * Settings.CASTLES_SIZE * 0.5);
        	height = (int) (Main.gridSize * Settings.CASTLES_SIZE * 0.25);
    	} else {
    		width = (int) (Main.gridSize * Settings.CASTLES_SIZE * 0.25);
        	height = (int) (Main.gridSize * Settings.CASTLES_SIZE * 0.5);
    	}
        
		// Place x and y at the center of the castle
    	pos.translate(Settings.CASTLES_SIZE * Main.gridSize / 2,  Settings.CASTLES_SIZE * Main.gridSize / 2);
    	// Move the door in its appropriate direction
    	pos.translate(this.doorDirection.toPoint().scalar(Main.gridSize * Settings.CASTLES_SIZE * 0.4f));
    	// Get the top left corner coordinates from the center point
    	pos.translate(- width / 2, - height / 2);   	

        Rectangle door = new Rectangle(pos.x, pos.y, width, height);
        door.setFill(Color.WHITE);
        this.layer.getChildren().add(door);
    }
	

	public void drawTroop(Troop troop) {
		Rectangle shape = new Rectangle(Main.gridSize * Settings.SOLDIER_SIZE, Main.gridSize * Settings.SOLDIER_SIZE);
		shape.setFill(this.owner.getColor());
		shape.setVisible(false);
		this.layer.getChildren().add(shape);
		troop.setShape(shape);
		
	}
	
	public void drawAllOrders() {
		for (Order order:orders) {
			for (Troop troop:order.getTroops()) {
				Rectangle shape = new Rectangle(Main.gridSize * Settings.SOLDIER_SIZE, Main.gridSize * Settings.SOLDIER_SIZE);
				shape.setFill(order.getSender().getColor());
				shape.setVisible(troop.canMove());
				troop.setShape(shape);
				this.layer.getChildren().add(shape);
				troop.drawSelf();
			}
		}
		
	}
	
	public void undrawTroop(Troop troop) {
		this.layer.getChildren().remove(troop.getShape());
	}
	
	
	
	/* GETTERS AND SETTERS */
	

	public Direction getDoorDirection() {return doorDirection;}
	public Point getLocation() {return location;}
	public Duke getOwner() {return owner;}
	
	public void setOwner(Duke owner) {
		this.owner = owner;
		this.shape.setFill(this.owner.getColor());
	}

	public int getLevel() {return level;}
	public void setLevel(int level) {this.level = level;}	
	public String getNickname() {return nickname;}
	public int getMoney() {	return money;}
	public void setMoney(int money) {this.money = money;}
	public List<Troop> getTroops() {return troops;}
	
	/**
	 * Returns troops but only those of a certain type.
	 * @param c	the type of troop you want to get
	 * @return 	troops but only those of a certain type.
	 */
	public List<Troop> getTroops(Class<?> c) {
		List<Troop> result = new ArrayList<>();
		for (Troop troop: troops) if (troop.getClass() == c) result.add(troop);
		return result;
	}

	public List<Production> getProductions() {return productions;}
	
	/** 
	 * Return only the index nth production.
	 * If out of bound, return null;
	 * 
	 * @param index	the index of the production
	 * @return		the index nth production
	 */
	public Production getProduction(int index) {
		if (index <= productions.size() - 1) return productions.get(index);
		return null;		
	}

	public List<Order> getOrders() {return orders;}
	
	/** Return only the index nth order
	 * 	If out of bound, return null;
	 * 
	 * @param index	the index of the order
	 * @return		the index nth order
	 */
	public Order getOrder(int index) {
		if (index <= orders.size() - 1) return orders.get(index);
		return null;		
	}

	public void setLayer(Pane layer) {
		this.layer = layer;
	}

	public Pane getLayer() {
		return layer;
	}

		
}
