package myGame;

import troop.Troop;

public class Production implements java.io.Serializable {

	private static final long serialVersionUID = 599383154148207372L;
	private int totalTime;
	private int timeElasped;
	private int cost;
	private String name;
	private Troop troop;
	private boolean isCastle = false;
	private boolean isFinish = false;

	public Production(Troop troop) {
		this.totalTime = troop.getTimeProduction();
		this.timeElasped = 0;
		this.troop = troop;
		this.cost = troop.getCostProduction();
		this.name = troop.toString();
	}

	public Production(int duration, int cost) {
		this.isCastle = true;
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