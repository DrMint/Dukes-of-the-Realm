package troop;

import myGame.Main;

public class Knight extends Troop {
	
	public Knight() {
		super(500, 20, 4, 3, 5);
	}
	
	public String toString() {
		return Main.language.getProperty("knight");
	}
}
