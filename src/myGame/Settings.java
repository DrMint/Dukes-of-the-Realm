package myGame;

import troop.Knight;
import troop.Spearman;
import troop.Troop;

public class Settings {

	public static final int SCENE_WIDTH = 1800;
    public static final int SCENE_HEIGHT = 900;
	public static final int STATUS_BAR_HEIGHT = 200;
	
	public static final int POPUP_WIDTH = 400;
    public static final int POPUP_HEIGHT = 600;
	
	public static final int TURN_DURATION = 200; 		// in milliseconds
	public static final Troop[] PLAYER_DEFAULT_TROOP = {new Spearman(), new Spearman(), new Knight()};
	public static final int PLAYER_DEFAULT_MONEY = 0;
	
	public static final int NEUTRAL_MAX_MONEY = 5000;
	public static final int NEUTRAL_MAX_LEVEL = 5;
	
	public static final	int GRID_WIDTH = 200;
	public static final	int GRID_HEIGHT = 120;
	public static final float GRID_THICKNESS = 0.1f;	// Thickness of the grid lines
	
	public static final float CANCEL_PRODUCTION_RETURN_RATIO = 0.5f;
	
	public static final int CASTLES_SIZE = 5; 			// Preferably, an odd number
	public static final int SOLDIER_SIZE = 1; 			
	public static final int MIN_DISTANCE_CASTLES = 2; 	// The minimum distance in between castle
	
	public static final int NUM_DUKES = 5; 				// The number of non-neutral dukes (including the player)
	public static final int MAX_NEUTRAL = 20; 			// The maximum number of neutral castles
	
	public static final int NUM_PRODUCTION_SHOWN = 5; 	// The number of items listed in the production menu
	
	public static final int NUM_OSTS_SHOWN = 7; 		// The number of osts listed in the popup menu

}