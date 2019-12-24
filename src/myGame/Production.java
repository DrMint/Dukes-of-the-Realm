package myGame;

import troop.Troop;

public class Production {

	private int timeRemaining;
	private int totalTime;
	private String name;
	private Troop troop;
	private boolean isCastle;
	private boolean isFinish;

	public Production(Troop troop) {
		this.isCastle = false;
		this.totalTime = troop.getTimeProduction();
		this.timeRemaining = troop.getTimeProduction();
		this.troop = troop;
		this.name = troop.toString();
	}

	public Production(int duration) {
		this.isCastle = true;
		this.timeRemaining = duration;
		this.totalTime = duration;
		this.name = "Chateau";
	}
	
	public void tick() {
		timeRemaining--;
		if (timeRemaining == 0) {
			this.isFinish = true;
		}
	}

	public boolean isFinish() {
		return isFinish;
	}

	public Troop getTroop() {
		return troop;
	}

	public int getTimeRemaining() {
		return timeRemaining;
	}
	
	public int getTotalTime() {
		return totalTime;
	}
	
	public String getName() {
		return name;
	}

	public boolean isCastle() {
		return isCastle;
	}
	
	
}