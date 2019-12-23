package myGame;

import troop.Knight;
import troop.Spearman;
import troop.Troop;

public class Settings {

	public static final int SCENE_WIDTH = 1800;
    public static final int SCENE_HEIGHT = 900;
	public static final int STATUS_BAR_HEIGHT = 150;
	
	public static final int POPUP_WIDTH = 400;
    public static final int POPUP_HEIGHT = 700;
	
	public static final int TURN_DURATION = 1; // in seconds
	public static final Troop[] PLAYER_DEFAULT_TROOP = {new Spearman(), new Spearman(), new Knight()};
	public static final int PLAYER_DEFAULT_MONEY = 1000;
	
	public static final int NEUTRAL_MAX_MONEY = 5000;
	public static final int NEUTRAL_MAX_LEVEL = 5;
	
	public static final	int GRID_WIDTH = 60;
	public static final	int GRID_HEIGHT = 40;
	public static final float GRID_THICKNESS = 0.1f;
	
	public static final int CASTLES_SIZE = 3; // The minimum distance in between castle
	public static final int MIN_DISTANCE_CASTLES = 2; // The minimum distance in between castle
	
	public static final int NUM_DUKES = 3; // The number of non-neutral dukes (including the player)
	public static final int MAX_NEUTRAL = 15; // The maximum number of neutral castles
	
	public static final int NUM_PRODUCTION_SHOWN = 5; // The number of items listed in the production menu
	
	public static final int NUM_OSTS_SHOWN = 7; // The number of osts listed in the popup menu

}