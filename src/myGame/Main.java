/*
 * TODO: 
 * 
 * Pour avoir 15 :
 * 
 * Ecrire la javaDoc
 * 
 * Les différents types de soldats doivent être aisément identifiable.
 * 
 * Il sera possible de sauvegarder une partie et de charger une sauvegarde depuis le disque (voir ObjectOutputStream et ObjectInputStream).
 * 
 * Il peut y avoir aucune IA
 * 
 * Les troupes ne doivent toujours pas se superposer au départ
 * 
 * Pour avoir + :
 * 
 * Les joueurs adverses devront disposer d’une intelligence artificielle minimaliste les faisant agir.
 * On se satisfera totalement que les ducs adverses effectuent des actions aléatoires à intervalles réguliers.
 * 
 * Un château pourra augmenter le nombre de production simultanée qu’il peut effectuer en produisant une amélioration appelée caserne. 
 * 
 * */

package myGame;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import troop.Catapult;
import troop.Knight;
import troop.Spearman;
import troop.Troop;

/**
 * This game is called Duke of the Realm, a real-time strategy game set in
 * medieval time. Please read the README file to know more about the game and
 * how to play it.
 * 
 * @author Thomas Barillot and Maël Bouquinet
 * @version 1.0
 * @since 2019-12-23
 */
public class Main extends Application {

	/**
	 * Top left point where the grid starts.
	 */
	static public Point gridStart = new Point();
	/**
	 * The size in pixels of one grid cell.
	 */
	static public int gridSize;

	public List<Duke> dukes = new ArrayList<>();
	static public List<Castle> castles = new ArrayList<>();
	static public Castle selectedCastle;
	static public Properties language = new Properties();

	private Pane playfieldLayer = new Pane();
	private Text textPause = new Text();
	private Group root = new Group();
	private Scene scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);

	private boolean isPaused = false;
	private boolean hasPressPause = false;

	private Date turnStart;
	private Date turnEnd;
	private int turnCounter = -1;

	private Input input = new Input(scene);
	private AnimationTimer gameLoop;

	private StatusBar statusBar;

	public static void main(String[] args) {
		launch(args);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		/* Import the proper language properties file according to Settings.LANGUAGE and COUNTRY */
        InputStream resourceStream = Main.class.getResourceAsStream(
        		"/languages/MessagesBundle_" + Settings.LANGUAGE + "_" + Settings.COUNTRY + ".properties");
		language.load(resourceStream);

		/* Prepare the scene.
		 * Here's what to do when the player click on the scene */
		scene.setOnMousePressed(e -> {
			Point p = new Point((int) e.getX(), (int) e.getY());
			p = pixelToGridCoordinates(p);

			if (!statusBar.getPopupAttack().isVisible()) {
				Castle tmp = getCastleFromPoint(p);
				if (tmp != null && tmp != selectedCastle) {
					selectedCastle = tmp;
					statusBar.refreshStatusBar();
				}
			}
		});
		
		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

		/* Compare the window's ratio and the grid's ratio, so that the grid is as big
		 * as possible without being stretch. There is currently a problem with some
		 * grid size (ie: 20*10)
		 */
		float win_ratio = (float) (Settings.SCENE_WIDTH / (Settings.SCENE_HEIGHT - Settings.STATUS_BAR_HEIGHT));
		float grid_ratio = (float) (Settings.GRID_WIDTH / Settings.GRID_HEIGHT);

		if (win_ratio > grid_ratio) {
			gridSize = (Settings.SCENE_HEIGHT - Settings.STATUS_BAR_HEIGHT) / Settings.GRID_HEIGHT;
			gridStart.x = (Settings.SCENE_WIDTH - Settings.GRID_WIDTH * gridSize) / 2;
			gridStart.y = 0;

		} else {
			gridSize = Settings.SCENE_WIDTH / Settings.GRID_WIDTH;
			gridStart.x = 0;
			gridStart.y = ((Settings.SCENE_HEIGHT - Settings.STATUS_BAR_HEIGHT) - Settings.GRID_HEIGHT * gridSize) / 2;
		}

		// Draw the grid by adding lines
		Line line = new Line();
		int xPos;
		for (int x = 0; x <= Settings.GRID_WIDTH; x++) {
			xPos = gridStart.x + x * gridSize;
			line = new Line(xPos, gridStart.y, xPos, gridStart.y + Settings.GRID_HEIGHT * gridSize);
			line.setStrokeWidth(Settings.GRID_THICKNESS);
			root.getChildren().add(line);
		}

		int yPos;
		for (int y = 0; y <= Settings.GRID_HEIGHT; y++) {
			yPos = gridStart.y + y * gridSize;
			line = new Line(gridStart.x, yPos, gridStart.x + Settings.GRID_WIDTH * gridSize, yPos);
			line.setStrokeWidth(Settings.GRID_THICKNESS);
			root.getChildren().add(line);
		}

		// Create layers
		root.getChildren().add(playfieldLayer);

		/* Create a list of Duke's names from a file called dukes.txt */
		List<String> dukeNames = new ArrayList<>();
		try {
			Path filePath = Paths.get("resources/names/dukes.txt");
			dukeNames = Files.readAllLines(filePath, Charset.forName("UTF8"));
		} catch (IOException e) {
			dukeNames.add("France");
			dukeNames.add("Germany");
			dukeNames.add("Russia");
			dukeNames.add("Spain");
			dukeNames.add("Italy");
			e.printStackTrace();
		}

		/* Right now, if there are more Dukes than colors, it loops */
		List<Color> colorList = new ArrayList<>();
		colorList.add(Color.BLUEVIOLET);
		colorList.add(Color.RED);
		colorList.add(Color.GREEN);
		colorList.add(Color.PINK);
		colorList.add(Color.BURLYWOOD);
		colorList.add(Color.PURPLE);

		/* Creates a list of Dukes + a random number of Neutral dukes */
		String selectedName;
		Color selectedColor;
		int nbNeutral = getRandomIntegerBetweenRange(Settings.MAX_NEUTRAL, Settings.MAX_NEUTRAL);
		for (int i = 0; i < Settings.NUM_DUKES + nbNeutral; i++) {
			if (i < Settings.NUM_DUKES) {
				selectedColor = colorList.get(i % (colorList.size() - 1));
				selectedName = (String) getRandomElemInList(dukeNames);
				dukeNames.remove(selectedName);
			} else {
				selectedColor = Color.GRAY;
				selectedName = "";
			}
			dukes.add(new Duke(selectedName, selectedColor));
		}
		
		/* We will consider that Duke 0 is the player */
		dukes.get(0).isPlayer(true);

		/* Generate a list of all points of the grid */
		List<Point> points = new ArrayList<>();
		for (int x = Settings.CASTLES_SIZE; x < Settings.GRID_WIDTH - Settings.CASTLES_SIZE; x++) {
			for (int y = Settings.CASTLES_SIZE; y < Settings.GRID_HEIGHT - Settings.CASTLES_SIZE; y++) {
				points.add(new Point(x, y));
			}
		}

		// Create Castles
		List<String> castlesNames = new ArrayList<>();
		try {
			Path filePath = Paths.get("resources/names/castles.txt");
			castlesNames = Files.readAllLines(filePath, Charset.forName("UTF8"));
		} catch (IOException e) {
			castlesNames.add("Karuken");
			e.printStackTrace();
		}

		Point selectedPoint;
		List<Troop> defaultTroop = new ArrayList<>();
		for (Troop troop : Settings.PLAYER_DEFAULT_TROOP) {
			defaultTroop.add(troop);
		}

		Direction doorDirection;
		for (Duke duke : dukes) {
			selectedName = (String) getRandomElemInList(castlesNames);
			castlesNames.remove(selectedName);
			selectedPoint = (Point) getRandomElemInList(points);
			doorDirection = new Direction();
			doorDirection.randomize();
			
			Castle c = new Castle(selectedName, duke, Settings.PLAYER_DEFAULT_MONEY, 1, defaultTroop, selectedPoint,
					  playfieldLayer, doorDirection);
			
			c.drawSelf();
			castles.add(c);
			
			/* Remove all points around and covered by the castle */
			for (int x = -Settings.MIN_DISTANCE_CASTLES * Settings.CASTLES_SIZE; x <= Settings.MIN_DISTANCE_CASTLES * Settings.CASTLES_SIZE; x++) {
				for (int y = -Settings.MIN_DISTANCE_CASTLES * Settings.CASTLES_SIZE; y <= Settings.MIN_DISTANCE_CASTLES	* Settings.CASTLES_SIZE; y++) {
					points.remove(points.removeIf(Point.PredicatIsEquals(new Point(selectedPoint.x + x, selectedPoint.y + y))));
				}
			}
			
			if (duke.isNeutral()) {
				c.setMoney(c.getMoney() + getRandomIntegerBetweenRange(0, Settings.NEUTRAL_MAX_MONEY));
				c.setLevel(c.getLevel() + getRandomIntegerBetweenRange(0, Settings.NEUTRAL_MAX_LEVEL));
				for (int i = 0; i <= c.getLevel() - 2; i++) {
					switch (getRandomIntegerBetweenRange(0, 3)) {
					case 0:
						c.addTroop(new Spearman());
						break;
					case 1:
						c.addTroop(new Knight());
						break;
					case 2:
						c.addTroop(new Catapult());
						break;
					}
				}
			}
		}
		
		/* Already select the player's castle and show the Status Bar */
		selectedCastle = castles.get(0);
		
		
		/*
		// DEBUG: Cheat MAXIMUM
		for (int i = 0; i< 30; i++) {
			castles.get(0).addTroop(new Catapult());
			castles.get(0).addTroop(new Knight());
			castles.get(0).addTroop(new Spearman());
			castles.get(0).setMoney(1000);
		}
		
		// DEBUG: Launch order in all direction
		for (Castle castle:castles) {
			List<Troop> tmp = new ArrayList<>();
			tmp.add(castles.get(0).getTroops(new Catapult()).get(0));
			castles.get(0).addOrder(castle, tmp);
		}
		*/
		


		input.addListeners();

		statusBar = new StatusBar(root);
		statusBar.refreshStatusBar();

		/* Add pause text */
		textPause.setText("PAUSE");
		textPause.getStyleClass().add("pause");
		textPause.setX(Settings.SCENE_WIDTH / 2 - 112);
		textPause.setY(Settings.SCENE_HEIGHT / 2 - 15);
		textPause.setVisible(false);
		root.getChildren().add(textPause);

		gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {

				/* If the game isn't pause, sends ticks to all the castles and moves the orders */
				if (!isPaused && !statusBar.getPopupAttack().isVisible()) {
					if (turnCounter == -1) {
						turnCounter++;
						turnStart = new Date();
					} else {
						turnEnd = new Date();
						int timeElapsed = (int) ((turnEnd.getTime() - turnStart.getTime()));
						if (timeElapsed >= Settings.TURN_DURATION) {
							turnCounter++;
							turnStart = new Date();
							statusBar.refreshStatusBar();
							for (Castle castle : castles) {
								castle.tick();
								for (Castle c : castles) {
									for (Order o : c.getOrders()) {
										o.tick();
									}
								}
							}
						}
					}
				}

				/* If a pop-up asks for a refresh, do it */
				if (statusBar.getPopupAttack().isVisible()) {
					if (statusBar.getPopupAttack().needRefresh()) {
						statusBar.getPopupAttack().refresh();
					}

					if (statusBar.getPopupAttack().getPopupTroop().needRefresh()) {
						statusBar.getPopupAttack().refresh();
					}
				}

				processInput(input, now);
			}

			private void processInput(Input input, long now) {
				if (input.isExit()) {
					Platform.exit();
					System.exit(0);
				} else if (input.isPause()) {
					if (!hasPressPause) {
						hasPressPause = true;
						isPaused = !isPaused;
						textPause.setVisible(isPaused);
					}
				} else {
					hasPressPause = false;
				}

			}

		};
		gameLoop.start();
	}

	/**
	 * Convert a pixel from the window into its grid coordinates
	 * 
	 * @param p the pixel that must be converted
	 * @return the corresponding grid coordinates
	 */
	static public Point pixelToGridCoordinates(Point p) {
		Point GridClick = new Point();
		GridClick.x = (int) (p.x - Main.gridStart.x) / Main.gridSize;
		GridClick.y = (int) (p.y - Main.gridStart.y) / Main.gridSize;
		return GridClick;

	}

	/**
	 * From a grid coordinates, returns the castle at that position.
	 * 
	 * @param p the coordinates on the grid
	 * @return the corresponding castle if there is one, otherwise null
	 */
	static public Castle getCastleFromPoint(Point p) {

		for (Castle castle : Main.castles) {
			if (p.x >= castle.getLocation().x && 
				p.y >= castle.getLocation().y &&
				p.x < castle.getLocation().x + Settings.CASTLES_SIZE &&
				p.y < castle.getLocation().y + Settings.CASTLES_SIZE)
			{
				return castle;
			}
		}
		return null;
	}

	/**
	 * Search an element in a list and returns it.
	 * 
	 * @param list   the list in which to search
	 * @param target the searched element
	 * @return the element if it exists in the list, otherwise null
	 */
	@SuppressWarnings("rawtypes")
	public static Object getElemInList(List list, Object target) {
		for (Object o : list) {
			if (target.equals(o)) {
				return o;
			}
		}
		return null;
	}

	/**
	 * Takes a list of any kind and return one random element for it.
	 * 
	 * @param list the list from which the element will be picked.
	 * @return a random element if the list is not empty, otherwise null
	 */
	@SuppressWarnings("rawtypes")
	public static Object getRandomElemInList(List list) {
		int listLenght = list.size();
		if (!list.isEmpty()) {
			int randomIndex = getRandomIntegerBetweenRange(0, listLenght);
			return list.get(randomIndex);
		}
		return null;
	}

	/**
	 * Generates a random integer between min and max (max excluded)
	 * 
	 * @param min lower range
	 * @param max higher range
	 * @return a random integer
	 */
	public static int getRandomIntegerBetweenRange(double min, double max) {
		max--;
		double x = (int) (Math.random() * ((max - min) + 1)) + min;
		return (int) x;

	}

	/**
	 * Getter for isPaused
	 * 
	 * @return value of isPaused
	 */
	public boolean isPaused() {
		return isPaused;
	}

}