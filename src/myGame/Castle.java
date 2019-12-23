package myGame;

import java.util.ArrayList;
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
public class Castle {
	
	private String nickname;
	private Duke owner;
	private int money;
	private int level;
	private List<Troop> troops = new ArrayList<>();
	private Point location;
	private Pane layer;
	private List<Order> orders = new ArrayList<>();
	private List<Production> productions = new ArrayList<>();
	private Direction doorDirection;
	
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
		
	public int costToLevel() {
		int realLevel = this.level;
		for (Production production:productions) {
			if (production.isCastle()) realLevel++;
		}
		return 1000 * realLevel;
	}
	
	public int timeToLevel() {
		int realLevel = this.level;
		for (Production production:productions) {
			if (production.isCastle()) realLevel++;
		}
		return 100 + 50 * realLevel;
	}
	
	public void levelUp() {
		/*If it's too long, just used very small levels*/
		int cost = costToLevel();
		int time = timeToLevel();
		
		if (cost <= this.money) {
			this.money -= cost;
			productions.add(new Production(time));
		}
	
	}
	
	public void addTroop(Troop troop) {
		troops.add(troop);
	}
	
	public void addProduction(Troop troop) {
		if (troop.getCostProduction() <= this.money) {
			this.money -= troop.getCostProduction();
			productions.add(new Production(troop));
		}
	}
	
	public void tick() {
		
		// Add money each tick. If the duke is neutral, it's 10% of the normal income.
		if (this.owner.isNeutral()) {
			this.money += this.level;
		} else {
			this.money += this.level * 10;
		}
		
		Iterator<Production> i = productions.iterator();
		while (i.hasNext()) {
		   Production production = i.next();
		   production.tick();
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
		
		for (Order order:orders) {
			order.moveAll();
		}
		
	}

	public Point getLocation() {
		return location;
	}

	public Duke getOwner() {
		return owner;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}	
	
	public String getNickname() {
		return nickname;
	}

	public int getMoney() {
		return money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}


	public List<Troop> getTroops() {
		return troops;
	}
	
	public List<Troop> getTroops(Object c) {
		List<Troop> result = new ArrayList<>();
		for (Troop troop: troops) {
			if (troop.getClass() == c.getClass()) {
				result.add(troop);
			}
		}
		return result;
	}

	public List<Production> getProductions() {
		return productions;
	}
	
	public Production getProduction(int index) {
		if (index <= productions.size() - 1) {
			return productions.get(index);
		}
		return null;		
	}
	
	public Order getOrder(int index) {
		if (index <= orders.size() - 1) {
			return orders.get(index);
		}
		return null;		
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void addOrder(Castle target, List<Troop> troops) {
		
		for (Troop troop:troops) {
			troop.setLocation(this.location.add(this.doorDirection.toPoint().scalar(Settings.CASTLES_SIZE)));
			Rectangle shape = new Rectangle(Main.gridSize, Main.gridSize);
			shape.setX(-100);
			shape.setY(-100);
			shape.setFill(this.owner.getColor());
			this.layer.getChildren().add(shape);
			troop.setShape(shape);
		}
		
		for (Order order:orders) {
			if (order.getTarget() == target) {
				order.addTroops(troops);
				this.troops.removeAll(troops);
				return;
			}
		}
		
		this.orders.add(new Order(this, target, troops));
		this.troops.removeAll(troops);
	}

	public Direction getDoorDirection() {
		return doorDirection;
	}
	
	public void addToLayer() {
		Point pos = new Point(Main.gridStart.x + this.location.x * Main.gridSize, Main.gridStart.y + this.location.y * Main.gridSize);
    	Rectangle shape = new Rectangle(pos.x, pos.y, Main.gridSize * Settings.CASTLES_SIZE, Main.gridSize * Settings.CASTLES_SIZE);
    	shape.setFill(this.owner.getColor());
    	this.layer.getChildren().add(shape);
        
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
	
}
