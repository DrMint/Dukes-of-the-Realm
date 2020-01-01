package myGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import troop.Troop;

/**
 * An order represents a group of troops, sent by a duke, walking towards a target castle.
 * @author Thomas Barillot and Maël Bouquinet
 * @version 1.0
 * @since   2019-12-23
 *
 */
public class Order implements java.io.Serializable {

	private static final long serialVersionUID = 6955717685163840542L;
	
	/**
	 * Their target castle.
	 */
	private final Castle target;
	
	/**
	 * A list of all troops this order comprised.
	 */
	private final List<Troop> troops;
	
	/**
	 * The duke that originally send them 
	 * (useful if the castle they originate has changed owner since they deployed).
	 */
	private final Duke sender;

	public Order(Castle origin, Castle target, List<Troop> troops) {
		this.sender = origin.getOwner();
		this.target = target;
		this.troops = troops;
	}
	
	public void moveAll(Class<?> c) {
		for (Troop troop: troops) if (troop.getClass() == c) troop.move(target.getLocation().copy());
	}
	
	public void tick() {
		int damageTaken = target.dealDamage();
		
		Iterator<Troop> i = troops.iterator();
		while (i.hasNext()) {
			Troop troop = i.next();
			if (troop.hasArrived()) {
				/* If the target castle is own by the sender of the order,
				 * adds the troops to the target castle */
				if (this.target.getOwner() == this.sender) {
					target.undrawTroop(troop);
					target.addTroop(troop);
					i.remove();
					
				/* Otherwise, this is a enemy castle */
				} else {
					/* Deals this troop's damage to the castle */
					target.takesDamage(troop.getDamage());
					
					/* Takes damage */
					if (troop.getHealth() <= damageTaken) {
						damageTaken -= troop.getHealth();
						target.undrawTroop(troop);
						i.remove();
					} else {
						troop.setHealth(troop.getHealth() - damageTaken);
						damageTaken = 0;
					}
					
					/* If the target castle no longer has defenses, its owner becomes
					 * the sender owner. The target castle also loses all current productions */
					if (target.getTroops().size() == 0) {
						target.setOwner(this.sender);
						target.cancelAllProduction();
					}
				}
			}
		}		
	}
	
	
	/* GETTERS AND SETTERS */
	
	
	
	//public void cancel() {this.target = this.origin;}
	public Castle getTarget() {return target;}
	public Duke getSender() {return sender;}
	public List<Troop> getTroops() {return troops;}
	public void addTroop(Troop c) {troops.add(c);}
	public void addTroops(List<Troop> list) {troops.addAll(list);}
	
	public List<Troop> getTroops(Class<?> c) {
		List<Troop> result = new ArrayList<>();
		for (Troop troop: troops) if (troop.getClass() == c) result.add(troop);
		return result;
	}	
}
