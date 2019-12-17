package myGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import troop.*;

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
	
	/*0 = North
	 *1 = East
	 *2 = South
	 *3 = West*/
	private int doorDirection;
	
	public Castle(String nickname, Duke owner, int money, int level, List<Troop> troops, Point location, Pane layer, int doorDirection) {
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
		
		this.money += this.level * 10;
		
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
        
    public void removeFromLayer() {
        this.layer.getChildren().remove(layer);
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
		
		int sizeCastle = Settings.CASTLES_SIZE;
		
		/*0 = North
		 *1 = East
		 *2 = South
		 *3 = West*/
		for (Troop troop:troops) {
			switch (this.doorDirection) {
				case 0: ; troop.setLocation(this.location.add(sizeCastle / 2, -1)); break;
				case 1: ; troop.setLocation(this.location.add(sizeCastle + 1, sizeCastle / 2)); break;
				case 2: ; troop.setLocation(this.location.add(sizeCastle / 2, sizeCastle + 1)); break;
				case 3: ; troop.setLocation(this.location.add(-1, sizeCastle / 2)); break;
			}
			Rectangle shape = new Rectangle(20, 20);
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

	public int getDoorDirection() {
		return doorDirection;
	}
	
}
