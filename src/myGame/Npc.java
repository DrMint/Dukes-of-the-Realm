package myGame;

import java.util.ArrayList;
import java.util.List;

import troop.*;

public class Npc implements java.io.Serializable{

	private static final long serialVersionUID = 8627611846432297083L;
	private Duke identity;
	private int timerCreateTroop;
	private int timerLaunchAttack;
	private int timerCastleLevel;
	
	public Npc(Duke identity) {
		this.identity = identity;
		
		timerCreateTroop = Main.getRandomIntegerBetweenRange(
				(int) (Settings.NPC_TIMER_TROOP * Settings.NPC_TIMER_MIN), Settings.NPC_TIMER_TROOP);
		
		timerLaunchAttack = Main.getRandomIntegerBetweenRange(
				(int) (Settings.NPC_TIMER_ATTACK * Settings.NPC_TIMER_MIN), Settings.NPC_TIMER_ATTACK);
		
		timerCastleLevel = Main.getRandomIntegerBetweenRange(
				(int) (Settings.NPC_TIMER_LEVELUP * Settings.NPC_TIMER_MIN), Settings.NPC_TIMER_LEVELUP);
	}
	
	public void tick() {
		List<Castle> castles = Main.getCastlesFromDuke(this.identity);
		Castle myRandomCastle;
		
		timerCreateTroop--;
		timerLaunchAttack--;
		timerCastleLevel--;
		
		/* If the time for a troop creation has passed,
		 * select a random type of troop to create and try to add it
		 * the function addProduction already verify if the castle has
		 * the amount of money necessary. */
		if (timerCreateTroop <= 0) {
			
			timerCreateTroop = Main.getRandomIntegerBetweenRange(
					(int) (Settings.NPC_TIMER_TROOP * Settings.NPC_TIMER_MIN), Settings.NPC_TIMER_TROOP) / castles.size();
			
			myRandomCastle = (Castle) Main.getRandomElemInList(castles);
			switch(Main.getRandomIntegerBetweenRange(0, 3)) {
				case 0: myRandomCastle.addProduction(new Spearman());
				case 1: myRandomCastle.addProduction(new Knight());
				case 2: myRandomCastle.addProduction(new Catapult());
			}
		}
		
		/* If the time for a new attack has passed,
		 * select a random castle, a random set of available troops
		 * and attack. It is totally possible that the NPC send soldiers
		 * to a castle it owns, or even to the castle their already in.*/
		if (timerLaunchAttack <= 0) {
			
			timerLaunchAttack = Main.getRandomIntegerBetweenRange(
					(int) (Settings.NPC_TIMER_ATTACK * Settings.NPC_TIMER_MIN), Settings.NPC_TIMER_ATTACK) / castles.size();
			
			myRandomCastle = (Castle) Main.getRandomElemInList(castles);
			Castle targetRandomCastle = (Castle) Main.getRandomElemInList(Main.castles);
			List<Troop> myTroops = new ArrayList<>();
			
			for (Troop troop:myRandomCastle.getTroops()) {
				if (Main.getRandomBoolean()) myTroops.add(troop);
			}
			
			myRandomCastle.addOrder(targetRandomCastle, myTroops);
		}
		
		/* If the time for a leveling up a castle has passed,
		 * select a random castle, and level it up only if the castle
		 * is at least 2 times to cost to level up.*/
		if (timerCastleLevel <= 0) {
			
			timerCastleLevel = Main.getRandomIntegerBetweenRange(
					(int) (Settings.NPC_TIMER_LEVELUP * Settings.NPC_TIMER_MIN), Settings.NPC_TIMER_LEVELUP) / castles.size();
			
			
			myRandomCastle = (Castle) Main.getRandomElemInList(castles);
			if (myRandomCastle.costToLevel() < myRandomCastle.getMoney()) myRandomCastle.levelUp();
		}
	}
	
	
	
	
}
