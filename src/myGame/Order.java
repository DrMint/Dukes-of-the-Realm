package myGame;

import java.util.ArrayList;
import java.util.List;

import troop.Troop;

public class Order {
	
	private Castle origin;
	private Castle target;
	private List<Troop> troops;
	
	public Order(Castle origin, Castle target, List<Troop> troops) {
		this.origin = origin;
		this.target = target;
		this.troops = troops;
	}
	
	public void moveAll() {
		for (Troop troop: troops) {
			troop.move(target.getLocation());
		}
	}
	
	public void cancel() {
		this.target = this.origin;
	}

	public Castle getOrigin() {
		return origin;
	}

	public void setOrigin(Castle origin) {
		this.origin = origin;
	}

	public Castle getTarget() {
		return target;
	}

	public void setTarget(Castle target) {
		this.target = target;
	}

	public List<Troop> getTroops() {
		return troops;
	}
	
	public void addTroop(Troop c) {
		troops.add(c);
	}
	
	public void addTroops(List<Troop> list) {
		troops.addAll(list);
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

	public void setTroops(List<Troop> troops) {
		this.troops = troops;
	}
	
	
	
	
	
}
