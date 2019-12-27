package troop;

import myGame.Main;

public class Spearman extends Troop {
	
	public Spearman() {
		super(100, 5, 2, 1, 1);
	}
	
	public String toString() {
		return Main.language.getProperty("spear");
	}
	
}
