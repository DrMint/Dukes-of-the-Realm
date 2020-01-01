package popup;

import javafx.scene.layout.GridPane;

/**
 * A popup is a new window displayed above the game.
 * @author Thomas Barillot and Maël Bouquinet
 * @version 1.0
 * @since   2019-12-23
 *
 */
public abstract class Popup {

	GridPane pane = new GridPane();
	private boolean isVisible = false;
	boolean needRefresh = false;

	public Popup() {
	}
	
	/**
	 * Makes the pop-up appear.
	 */
	public void hide() {
		isVisible = false;
		pane.setVisible(isVisible);
	}
	
	/**
	 * Makes the pop-up disappear.
	 */
	public void show() {
		isVisible = true;
		pane.setVisible(isVisible);
	}
	
	
	/**
	 * Refresh all the necessary the information
	 */
	public abstract void refresh();
	
	
	/* GETTERS AND SETTERS */

	public boolean isVisible() {return isVisible;}
	public boolean needRefresh() {return needRefresh;}
	
}
