package popup;

import javafx.scene.layout.GridPane;

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
