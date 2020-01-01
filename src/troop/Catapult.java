package troop;

import myGame.Main;

/**
 * A Catapult is a type of troop.
 * @author Thomas Barillot and Maël Bouquinet
 * @version 1.0
 * @since   2019-12-23
 *
 */
public class Catapult extends Troop {
	
	private static final long serialVersionUID = -2808253247483083177L;

	public Catapult() {
		super(1000, 50, 1, 5, 10);
	}

	public String toString() {
		return Main.language.getProperty("catapult");
	}
}
