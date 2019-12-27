package troop;

import myGame.Main;

public class Catapult extends Troop {
	
	public Catapult() {
		super(1000, 50, 1, 5, 10);
	}

	public String toString() {
		return Main.language.getProperty("catapult");
	}
}
