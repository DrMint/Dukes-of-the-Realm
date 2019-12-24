package myGame;

import java.util.ArrayList;
import java.util.Iterator;
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
			troop.move(target.getLocation().copy());
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
	
	public void tick() {
		
		int damageTaken = target.dealDamage();
		
		Iterator<Troop> i = troops.iterator();
		while (i.hasNext()) {
			Troop troop = i.next();
			if (troop.hasArrived()) {
				/* If the target castle is own by the sender of the order,
				 * adds the troops to the target castle */
				if (this.target.getOwner() == this.origin.getOwner()) {
					origin.undrawTroop(troop);
					target.addTroop(troop);
					i.remove();
					
				/* Otherwise, this is a enemy castle */
				} else {
					/* Deals this troop's damage to the castle */
					target.takesDamage(troop.getDamage());
					
					/* Takes damage */
					if (troop.getHealth() <= damageTaken) {
						damageTaken -= troop.getHealth();
						origin.undrawTroop(troop);
						i.remove();
					} else {
						troop.setHealth(troop.getHealth() - damageTaken);
						damageTaken = 0;
					}
					
					/* If the target castle no longer has defenses, its owner becomes
					 * the sender owner. The target castle also loses all current productions */
					if (target.getTroops().size() == 0) {
						target.setOwner(this.origin.getOwner());
						target.cancelAllProduction();
					}
				}
			} else {
				troop.refresh();
			}
		}		
	}
}
