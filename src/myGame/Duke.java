package myGame;

import javafx.scene.paint.Color;

public class Duke implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4031712221322708268L;
	private String name;
	private String color;
	private boolean isPlayer;
	private boolean isNeutral;
	private Npc npc;

	public Duke(String name, Color color, boolean isPlayer) {
		this.name = name;
		this.color = color.toString();
		this.isPlayer = isPlayer;
		this.isNeutral = name == "";
		
		/* If this isn't the player, and it isn't a neutral
		 * duke, then it is a NPC */
		if (!isPlayer && !isNeutral) npc = new Npc(this);
	}
	
	public Duke(String name, Color color) {
		this(name, color, false);
	}
	
	/**
	 * If the dukes is an NPC, this will make the NPC
	 * do its thing, otherwise this will do nothing.
	 */
	public void tick() {
		if (!isPlayer && !isNeutral) npc.tick();
	}
	
	/* GETTERS AND SETTERS */
	
	public Color getColor() {return Color.web(color);}
	public String toString() {return name;}
	public boolean isPlayer() {return isPlayer;}
	public boolean isNeutral() {return isNeutral;}
	public void isPlayer(boolean isPlayer) {this.isPlayer = isPlayer;}
}
