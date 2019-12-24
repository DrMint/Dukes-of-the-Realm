package popup;

import javafx.scene.Group;
import javafx.scene.layout.GridPane;

public abstract class Popup {

	Group layer;
	GridPane pane = new GridPane();
	private boolean isVisible = false;
	boolean needRefresh = false;

	public Popup(Group root) {
		this.layer = root;
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
	
	public boolean isVisible() {
		return isVisible;
	}

	public boolean needRefresh() {
		return needRefresh;
	}
	
}
