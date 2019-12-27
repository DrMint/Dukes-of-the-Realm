package myGame;

import troop.Troop;

public class Production implements java.io.Serializable {

	private static final long serialVersionUID = 599383154148207372L;
	private int totalTime;
	private int timeRemaining;
	private int cost;
	private String name;
	private Troop troop;
	private boolean isCastle = false;
	private boolean isFinish = false;

	public Production(Troop troop) {
		this.totalTime = troop.getTimeProduction();
		this.timeRemaining = troop.getTimeProduction();
		this.troop = troop;
		this.cost = troop.getCostProduction();
		this.name = troop.toString();
	}

	public Production(int duration, int cost) {
		this.isCastle = true;
		this.totalTime = duration;
		this.timeRemaining = duration;
		this.cost = cost;
		this.name = Main.language.getProperty("castle");
	}
	
	public void tick() {
		timeRemaining--;
		if (timeRemaining == 0) {
			this.isFinish = true;
		}
	}
	
	
	
	/* GETTERS AND SETTERS */
	
	

	public boolean isFinish() {return isFinish;}
	public Troop getTroop() {return troop;}
	public int getTimeRemaining() {return timeRemaining;}
	public int getTotalTime() {return totalTime;}
	public String getName() {return name;}
	public boolean isCastle() {return isCastle;}
	public int getCost() {return cost;}	
}