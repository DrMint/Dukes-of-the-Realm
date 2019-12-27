package troop;

import myGame.Main;

public class Knight extends Troop {
	
	private static final long serialVersionUID = 6004745113628864816L;

	public Knight() {
		super(500, 20, 4, 3, 5);
	}
	
	public String toString() {
		return Main.language.getProperty("knight");
	}
}
