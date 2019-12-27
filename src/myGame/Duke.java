package myGame;

import javafx.scene.paint.Color;

/**
 * 
 * @author Thomas Barillot and Maël Bouquinet
 * @version 1.0
 * @since   2019-12-23
 *
 */
public class Duke implements java.io.Serializable {

	private static final long serialVersionUID = 4031712221322708268L;
	
	/**
	 * The name of the duke.
	 */
	private String name;
	
	/**
	 * The duke color. It is a javafx.scene.paint.Color.toString()
	 */
	private String color;
	
	/**
	 * Is the duke the player?
	 */
	private boolean isPlayer;
	
	/**
	 * Is the duke a neutral duke?
	 */
	private boolean isNeutral;
	
	/**
	 * If this duke is controlled by an NPC, here it is.
	 */
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
	 * If it is controlled by an NPC, propagates the tick to its NPC
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
