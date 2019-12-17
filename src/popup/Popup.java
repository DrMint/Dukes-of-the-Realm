package popup;

import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import myGame.Castle;
import myGame.Point;

public abstract class Popup {

	Group layer;
	GridPane pane = new GridPane();
	private boolean isVisible = false;
	AnimationTimer gameLoop;

	public Popup(Group root) {
		this.layer = root;
		main();
	}
	
	public void hide() {
		isVisible = false;
		pane.setVisible(isVisible);
		gameLoop.stop();
	}
	
	public void show() {
		isVisible = true;
		pane.setVisible(isVisible);
		main();
	}
	
	public abstract void createPopup();
	public abstract void refreshPopup();
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void main() {
		
		
		gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				refreshPopup();
			}
		};
		gameLoop.start();
	}

	public void shareValues(Castle selectedCastle, Point gridStart, int gridSize, List<Castle> castles) {
		// TODO Auto-generated method stub
		
	}
	

	
}
