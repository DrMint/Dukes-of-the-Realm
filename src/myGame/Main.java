/*
 * TODO: 
 * 
 * Pour avoir 15 :
 * 
 * Ecrire la javaDoc
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

	static public List<Castle> castles = new ArrayList<>();
	static public Castle selectedCastle;
	static public Properties language = new Properties();

	private List<Duke> dukes = new ArrayList<>();
	
	private Pane playfieldLayer = new Pane();
	private Text textPause = new Text();
	private Text textEnd = new Text();
	private Group root = new Group();
	private Scene scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);

	private Input input = new Input(scene);
	private AnimationTimer gameLoop;

	private StatusBar statusBar;

	public static void main(String[] args) {
		launch(args);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void start(Stage primaryStage) throws IOException, InstantiationException, IllegalAccessException {
		
		/* Import the proper language properties file according to Settings.LANGUAGE and COUNTRY */
        InputStream resourceStream = Main.class.getResourceAsStream(
        		"/languages/" + Settings.LANGUAGE + "_" + Settings.COUNTRY + ".properties");
		language.load(resourceStream);
		
		/* Also load global.properties regardless of the language */
		resourceStream = Main.class.getResourceAsStream("/languages/global.properties");
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
		for (Class<?> c : Settings.PLAYER_DEFAULT_TROOP) {
			defaultTroop.add((Troop) c.newInstance());
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
						case 0:	c.addTroop(new Spearman()); break;
						case 1:	c.addTroop(new Knight()); break;
						case 2:	c.addTroop(new Catapult()); break;
					}
				}
			}
		}
		
		/* Already select the player's castle and show the Status Bar */
		selectedCastle = castles.get(0);
		
		
		
		// DEBUG: Cheat MAXIMUM
		
		/*
		for (int i = 0; i < 50; i++) {
			castles.get(0).addTroop(new Catapult());
			castles.get(0).addTroop(new Knight());
			castles.get(0).addTroop(new Spearman());
			castles.get(0).setMoney(1000);
		}
		*/
		/*
		
		// DEBUG: Launch order in all direction
		for (Castle castle:castles) {
			List<Troop> tmp = new ArrayList<>();
			tmp.add(castles.get(0).getTroops(Catapult.class).get(0));
			tmp.add(castles.get(0).getTroops(Knight.class).get(0));
			tmp.add(castles.get(0).getTroops(Spearman.class).get(0));
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
		
		/* Add ending text */
		textEnd.setText("");
		textEnd.getStyleClass().add("pause");
		textEnd.setX(Settings.SCENE_WIDTH / 2 - 112);
		textEnd.setY(Settings.SCENE_HEIGHT / 2 - 15);
		textEnd.setVisible(false);
		root.getChildren().add(textEnd);

		gameLoop = new AnimationTimer() {
			
			private int spearFrequency = Settings.NUM_ANIMATION_SUBDIVISION / new Spearman().getSpeed();
			private int knightFrequency = Settings.NUM_ANIMATION_SUBDIVISION / new Knight().getSpeed();
			private int catapultFrequency = Settings.NUM_ANIMATION_SUBDIVISION / new Catapult().getSpeed();
			
			private Date turnStart = new Date();
			private Date turnEnd;
			private int animationTimeSubdivision = 0;
			
			private boolean isPaused = false;
			private boolean hasPressPause = false;
			
			@Override
			public void handle(long now) {

				/* If the game isn't pause, sends ticks to all the castles and moves the orders */
				if (!isPaused && !statusBar.getPopupAttack().isVisible()) {
					turnEnd = new Date();
					int timeElapsed = (int) ((turnEnd.getTime() - turnStart.getTime()));
					if (timeElapsed >= Settings.TURN_DURATION) {
						turnStart = new Date();
						if (animationTimeSubdivision < Settings.NUM_ANIMATION_SUBDIVISION) {
							//System.out.print("Throttling " + animationTimeSubdivision + "\n");
							for (int i = 0; i < Settings.NUM_ANIMATION_SUBDIVISION - animationTimeSubdivision; i++) {
								animationMove();
							}
						}
						animationTimeSubdivision = 0;
						statusBar.refreshStatusBar();
						for (Castle castle : castles) {
							castle.tick();
							for (Order order : castle.getOrders()) order.tick();
						}
						
						Iterator<Duke> i = dukes.iterator();
						while (i.hasNext()) {
							Duke duke = i.next();
							if (getCastlesFromDuke(duke).size() == 0) {
								if (duke.isPlayer()) gameOver(false);
								i.remove();
							} else {
								duke.tick();
							}
						}
						
						if (dukes.size() <= 1) gameOver(true);
						
						if (statusBar.askForLoad) {
							statusBar.askForLoad = false;
							load();
						}
						if (statusBar.askForSave) {
							statusBar.askForSave = false; 
							save();
						}
						
					/* If we reached a subdivision of a turn for animation purposes */
					} else if (timeElapsed >= Settings.ANIMATION_SUBDIVISION * animationTimeSubdivision) {
						animationMove();
						animationTimeSubdivision++;
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
			
			private void animationMove() {
				if ((animationTimeSubdivision + 1) % spearFrequency == 0) {
					for (Castle c : castles) {
						for (Order o : c.getOrders()) {
							o.moveAll(Spearman.class);
						}
					}
				}
				
				if ((animationTimeSubdivision + 1) % knightFrequency == 0) {
					for (Castle c : castles) {
						for (Order o : c.getOrders()) {
							o.moveAll(Knight.class);
						}
					}
				}
				
				if ((animationTimeSubdivision + 1) % catapultFrequency == 0) {
					for (Castle c : castles) {
						for (Order o : c.getOrders()) {
							o.moveAll(Catapult.class);
						}
					}
				}
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
	 * Returns the list of all castles owned by a duke given in parameter.
	 * @param duke the duke that own the castle
	 * @return the list of all castles owned by the duke
	 */
	static public List<Castle> getCastlesFromDuke(Duke duke) {
		List<Castle> result = new ArrayList<>();
		for (Castle castle:castles) if (castle.getOwner() == duke) result.add(castle);
		return result;
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
		for (Object o : list) if (target.equals(o)) return o;
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
	public static int getRandomIntegerBetweenRange(int min, int max) {
		return (int) ((Math.random() * ((max - 1 - min) + 1)) + min);

	}
	
	/**
	 * @return a random boolean
	 */
	public static boolean getRandomBoolean() {
		return Math.random() >= 0.5;
	}
	
	
	public void gameOver(boolean win) {
		if (win) {
			textEnd.setText("Win");
		} else {
			textEnd.setText("Lose");
		}
		
		textEnd.setVisible(true);
		gameLoop.stop();
	}
	
	public void save() {
		// create a new file with an ObjectOutputStream
		FileOutputStream out;
		ObjectOutputStream oout;
		try {
			
			out = new FileOutputStream("last_save.txt");
			try {
				oout = new ObjectOutputStream(out);
				
		        // write something in the file
				oout.writeObject(selectedCastle);
		        oout.writeObject(castles);
		        oout.writeObject(dukes);
		        
		        // close the stream
		        oout.close();
				
			} catch (IOException e) {
				System.out.print("IOException");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			System.out.print("FileNotFoundException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void load() {
		// create an ObjectInputStream for the file we created before
        ObjectInputStream ois;
		try {
			
			ois = new ObjectInputStream(new FileInputStream("last_save.txt"));
			try {
				
				this.playfieldLayer.getChildren().clear();
				
				selectedCastle = (Castle) ois.readObject();
				castles = (List<Castle>) ois.readObject();
				dukes = (List<Duke>) ois.readObject();
		        // close the stream
		        ois.close();
		        
		        for (Castle castle:castles) {
		        	castle.setLayer(playfieldLayer);
		        	castle.drawSelf();
		        	castle.drawAllOrders();
		        }
		        
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}