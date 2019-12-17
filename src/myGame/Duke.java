package myGame;

import javafx.scene.paint.Color;

public class Duke {
	private String name;
	private Color color;
	private boolean isPlayer;
	private boolean isNeutral;

	public Duke(String name, Color color, boolean isPlayer) {
		this.name = name;
		this.color = color;
		this.isPlayer = isPlayer;
		this.isNeutral =  name == "";
	}
	
	public Duke(String name, Color color) {
		this(name, color, false);
	}
	
	public Color getColor() {
		return color;
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean isPlayer() {
		return isPlayer;
	}
	
	public boolean isNeutral() {
		return isNeutral;
	}

	public void isPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}
	
	
	
	
	
}
