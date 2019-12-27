package troop;

import myGame.Main;

public class Catapult extends Troop {
	
	private static final long serialVersionUID = -2808253247483083177L;

	public Catapult() {
		super(1000, 50, 1, 5, 10);
	}

	public String toString() {
		return Main.language.getProperty("catapult");
	}
}
