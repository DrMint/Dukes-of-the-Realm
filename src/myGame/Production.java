package myGame;

import troop.Troop;

public class Production {

	private int totalTime;
	private int timeRemaining;
	private int cost;
	private Troop troop;
	private boolean isCastle = false;
	private boolean isFinish = false;

	public Production(Troop troop) {
		this.totalTime = troop.getTimeProduction();
		this.timeRemaining = troop.getTimeProduction();
		this.troop = troop;
		this.cost = troop.getCostProduction();
	}

	public Production(int duration, int cost) {
		this.isCastle = true;
		this.totalTime = duration;
		this.timeRemaining = duration;
		this.cost = cost;
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
		if (isCastle) {
			return "Chateau";
		} else {			
			return troop.toString();
		}
	}

	public boolean isCastle() {
		return isCastle;
	}

	public int getCost() {
		return cost;
	}
	
	
}