package troop;

import myGame.Main;

/**
 * A Knight is a type of troop.
 * @author Thomas Barillot and Maël Bouquinet
 * @version 1.0
 * @since   2019-12-23
 *
 */
public class Knight extends Troop {
	
	private static final long serialVersionUID = 6004745113628864816L;

	public Knight() {
		super(500, 20, 4, 3, 5);
	}
	
	public String toString() {
		return Main.language.getProperty("knight");
	}
}
