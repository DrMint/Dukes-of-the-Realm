package myGame;

import troop.*;

public class Settings {

	/**
	 * Width of the game's window
	 */
	public static final int WINDOW_WIDTH = 1800;
	
	/**
	 * Height of the game's window
	 */
    public static final int WINDOW_HEIGHT = 900;
    
	/**
	 * Height of the status bar at the bottom
	 */
	public static final int STATUS_BAR_HEIGHT = 200;
	
	/**
	 * Width of the pop-ups windows
	 */
	public static final int POPUP_WIDTH = 400;
	
	/**
	 * Height of the pop-ups windows
	 */
    public static final int POPUP_HEIGHT = 600;
	
    
	/**
	 * The duration of a turn (in milliseconds) 
	 */
	public static final int TURN_DURATION = 40;
	
	/**
	 * The number of {@link TURN_DURATION} subdivisions, used for animation.
	 * <p>This value is in fact the speed of the fastest troop.
	 * Due to the way this program is written, this value needs to be a least common multiple in order
	 * to make them move at accurate speed. This is why the troops speeds are all powers of 2.
	 */
	public static final int NUM_ANIMATION_SUBDIVISION = 4;
	
	/**
	 * The duration of an animation subdivision.
	 * This value is equal to {@link TURN_DURATION} / {@link NUM_ANIMATION_SUBDIVISION}.
	 */
	public static final int ANIMATION_SUBDIVISION = TURN_DURATION / NUM_ANIMATION_SUBDIVISION;
	
	/**
	 * The troops of the player and NPCs will start with.
	 * <p>
	 * By default this list is empty, but you can add troops this way:
	 * {Spearman.class, Spearman.class, Knight.class}
	 */
	public static final Class<?>[] PLAYER_DEFAULT_TROOP = {};
	
	/**
	 * The money the player and NPCs will start with.
	 */
	public static final int PLAYER_DEFAULT_MONEY = 0;
	
	/**
	 * The maximum amount of money the neutral castles start with.
	 * <p>
	 * The exact value is picked at random in between {@link NEUTRAL_MAX_MONEY} / 2 and
	 * {@link NEUTRAL_MAX_MONEY}, for each neutral castle
	 */
	public static final int NEUTRAL_MAX_MONEY = 5000;
	
	/**
	 * The maximum level of a neutral castles
	 * <p>
	 * The exact value is picked at random in between 1 and {@link NEUTRAL_MAX_LEVEL}.
	 * This will also affect how many troops they are in the castle.
	 */
	public static final int NEUTRAL_MAX_LEVEL = 10;
	
	/**
	 * The default troops of the neutral castles.
	 * <p>
	 * The neutral castles will actually have one more random troop for each level above 1.
	 */
	public static final Class<?>[] NEUTRAL_DEFAULT_TROOP = {Knight.class, Spearman.class, Spearman.class};
	
	/**
	 * The width of the map.
	 */
	public static final	int GRID_WIDTH = 300;
	
	/**
	 * The height of the map.
	 */
	public static final	int GRID_HEIGHT = 110;

	/**
	 * The thickness of the grid lines
	 */
	public static final float GRID_THICKNESS = 0.1f;
	
	/**
	 * Which language should be used.
	 * The available languages can be found in the resources/languages folder.
	 */
	public static final String LANGUAGE = "en_US";
	
	/**
	 * The size of the castles (in grid cells).
	 * This value should preferably be an odd number because
	 * otherwise, the troops will not be centered with the castle's door when they are send out. 
	 */
	public static final int CASTLES_SIZE = 7;
	
	/**
	 * The size of the troops (in grid cells)
	 */
	public static final int SOLDIER_SIZE = 1; 			
	
	/**
	 * The minimum distance in between two castle (in {@link CASTLES_SIZE}).
	 * <p>
	 * The distance is equal to {@link MIN_DISTANCE_CASTLES} * {@link CASTLES_SIZE} grid cells
	 */
	public static final int MIN_DISTANCE_CASTLES = 2;
	
	/**
	 * The number of NPCs
	 */
	public static final int NUM_NPC = 5;
	
	/**
	 * The maximum number of neutral castles.
	 * <p>
	 * The exact value is picked at random in between {@link MAX_NEUTRAL} / 2 and {@link MAX_NEUTRAL}.
	 */
	public static final int MAX_NEUTRAL = 20;
	
	/**
	 * The maximum number of production shown in the production menu.
	 * <p>
	 * The maximum number of productions planned isn't bound by this value.
	 */
	public static final int NUM_PRODUCTION_SHOWN = 5;
	
	/**
	 * The maximum number of OSTs shown in the Send menu.
	 * <p>
	 * The maximum number of OSTs planned isn't bound by this value.
	 */
	public static final int NUM_OSTS_SHOWN = 7;
	
	/**
	 * The maximum number of turns before a NPC tries to produce a new troop.
	 * <p>
	 * The exact value is picked at random in between {@link NPC_TIMER_TROOP} * {@link NPC_TIMER_MIN} and {@link NPC_TIMER_TROOP}.
	 */
	public static final int NPC_TIMER_TROOP = 50;
	
	/**
	 * The maximum number of turns before a NPC tries to launch an attack.
	 * <p>
	 * The exact value is picked at random in between {@link NPC_TIMER_ATTACK} * {@link NPC_TIMER_MIN} and {@link NPC_TIMER_ATTACK}.
	 */
	public static final int NPC_TIMER_ATTACK = 200;
	
	/**
	 * The maximum number of turns before a NPC tries to level up its castle.
	 * <p>
	 * The exact value is picked at random in between {@link NPC_TIMER_LEVELUP} * {@link NPC_TIMER_MIN} and {@link NPC_TIMER_LEVELUP}.
	 */
	public static final int NPC_TIMER_LEVELUP = 500;
	
	/**
	 * This value is used by the NPC_TIMERs.
	 * <p>
	 * NPCs will do some actions at random interval.
	 * The exact value is picked at random in between the maximum value * {@link NPC_TIMER_MIN} and the maximum value.
	 */
	public static final float NPC_TIMER_MIN = 0.5f;
	
	/**
	 * Gives 100000 gold to the player.
	 */
	public static final boolean CHEAT_MONEY = false;
	
	/**
	 * Gives 100 troops of each type to the player.
	 */
	public static final boolean CHEAT_TROOPS = true;
	
	/**
	 * The player will send an attack made of the tree type of soldier, to each other castle.
	 */
	public static final boolean CHEAT_LAUNCH_ATTACK = false;
	

}