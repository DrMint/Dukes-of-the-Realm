package myGame;

import troop.Troop;

/**
 * A production is an enhancement for a castle: a level up for the castle or a new troop.
 * A production takes time and has a cost.
 * @author Thomas Barillot and Maël Bouquinet
 * @version 1.0
 * @since   2019-12-23
 *
 */
public class Production implements java.io.Serializable {

	private static final long serialVersionUID = 599383154148207372L;

	/**
	 * The total number of turns to complete the production.
	 */
	private final int totalTime;

	/**
	 * The time elapsed.
	 */
	private int timeElasped;

	/**
	 * Its cost.
	 */
	private final int cost;
	
	/**
	 * Its name.
	 */
	private final String name;

	/**
	 * If the production is a troop production, it is the made troop.
	 */
	private final Troop troop;
	
	/**
	 * True if the production is a castle level up, otherwise false.
	 */
	private final boolean isCastle;
	
	/**
	 * True if the production is finish, otherwise false.
	 */
	private boolean isFinish = false;

	public Production(Troop troop) {
		this.isCastle = false;
		this.troop = troop;
		this.totalTime = troop.getTimeProduction();
		this.timeElasped = 0;
		this.cost = troop.getCostProduction();
		this.name = troop.toString();
	}

	public Production(int duration, int cost) {
		this.isCastle = true;
		this.troop = null;
		this.totalTime = duration;
		this.timeElasped = 0;
		this.cost = cost;
		this.name = Main.language.getProperty("castle");
	}
	
	public void tick() {
		timeElasped++;
		if (timeElasped == totalTime) {
			this.isFinish = true;
		}
	}
	
	
	
	/* GETTERS AND SETTERS */
	
	

	public boolean isFinish() {return isFinish;}
	public Troop getTroop() {return troop;}
	public int getTimeElasped() {return timeElasped;}
	public int getTotalTime() {return totalTime;}
	public String getName() {return name;}
	public boolean isCastle() {return isCastle;}
	public int getCost() {return cost;}	
}