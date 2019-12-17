package myGame;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import popup.Popup;
import popup.PopupAttack;
import troop.Catapult;
import troop.Knight;
import troop.Spearman;
import troop.Troop;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Main extends Application {
	
	private Point gridStart = new Point();
	private int gridSize;
	
	private Pane playfieldLayer;

	private List<Enemy> enemies = new ArrayList<>();
	private List<Missile> missiles = new ArrayList<>();

	private Text textCastleName = new Text();
	private Text textCastleOwner = new Text();
	private Text textCastleLevel = new Text();
	private Text textCastleMoney = new Text();
	
	private Text textCastleSpear = new Text();
	private Text textCastleKnight = new Text();
	private Text textCastleCatapult = new Text();
	private Text textCastleTroopTotal = new Text();
	
	private Text textPause = new Text();
	private Text textProductions[] = new Text[Settings.NUM_PRODUCTION_SHOWN];
	
	private boolean collision = false;
	
	private Castle selectedCastle;
	private GridPane statusBar = new GridPane();
	private GridPane statsCastle = new GridPane();
	private GridPane statsTroops = new GridPane();
	private GridPane statsProduction = new GridPane();
    
	private Button addSpearmanButton = new Button("+");
	private Button addKnightButton = new Button("+");
	private Button addCatapultButton = new Button("+");
	private Button addLevelButton = new Button("+");
	private Button attackButton = new Button("Attaque");
	
    private boolean isPaused = false;
    private boolean hasPressPause = false;
	
    private Date turnStart;
    private Date turnEnd;
    private int turnCounter = -1;

	private Scene scene;
	private Input input;
	private AnimationTimer gameLoop;
	
	private List<Duke> dukes = new ArrayList<>();
	private List<Castle> castles = new ArrayList<>();
	
	private Group root;
	private Popup popupAttack;

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void start(Stage primaryStage) {

		root = new Group();
		scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		/* Initialize the text arrays */		
		for(int i = 0; i < textProductions.length; i++) {textProductions[i] = new Text();}
	
		/* Compare the window's ratio and the grid's ratio, so that the grid is as big as possible
		 * without being stretch. There is currently a problem with some grid size (ie: 20*10) */
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
		playfieldLayer = new Pane();
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
		int nbNeutral = getRandomIntegerBetweenRange(Settings.MAX_NEUTRAL,Settings.MAX_NEUTRAL);
		for(int i = 0; i < Settings.NUM_DUKES + nbNeutral; i++) {
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
				points.add(new Point(x,y));
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
		for (Troop troop: Settings.PLAYER_DEFAULT_TROOP) {
			defaultTroop.add(troop);
		}
		
		for (Duke duke: dukes) {
			selectedName = (String) getRandomElemInList(castlesNames);
			castlesNames.remove(selectedName);
			selectedPoint = (Point) getRandomElemInList(points);
			for (int x = -Settings.MIN_DISTANCE_CASTLES * Settings.CASTLES_SIZE; x <= Settings.MIN_DISTANCE_CASTLES * Settings.CASTLES_SIZE; x++) {
				for (int y = -Settings.MIN_DISTANCE_CASTLES * Settings.CASTLES_SIZE; y <= Settings.MIN_DISTANCE_CASTLES * Settings.CASTLES_SIZE; y++) {
					points.remove(points.removeIf(Point.PredicatIsEquals(new Point(selectedPoint.x + x, selectedPoint.y + y))));
				}
			}
			Castle c = new Castle(selectedName, duke, Settings.PLAYER_DEFAULT_MONEY, 1, defaultTroop, selectedPoint, playfieldLayer, getRandomIntegerBetweenRange(0,4));
			if (duke.isNeutral()) {
				 c.setMoney(c.getMoney() + getRandomIntegerBetweenRange(0, Settings.NEUTRAL_MAX_MONEY));
				 c.setLevel(c.getLevel() + getRandomIntegerBetweenRange(0, Settings.NEUTRAL_MAX_LEVEL));
				 for (int i = 0; i <= c.getLevel() - 2; i++) {
					 switch (getRandomIntegerBetweenRange(0,3)) {
						 case 0: c.addTroop(new Spearman()); break;
						 case 1: c.addTroop(new Knight()); break;
						 case 2: c.addTroop(new Catapult()); break;
					 }
				 }
			}
			addToLayer(c);
			castles.add(c);
		}
		
		loadGame();
		
		/* Already select the player's castle and show the Status Bar */
		selectedCastle = castles.get(0);
		refreshStatusBar();
		statusBar.setVisible(true);
		
		
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
				
				if (!isPaused && !popupAttack.isVisible()) {
					if (turnCounter == -1) {
						turnCounter++;
						turnStart = new Date();	
					} else {
						turnEnd = new Date();
						int timeElapsed = (int)((turnEnd.getTime() - turnStart.getTime()) / 1000);
						if (timeElapsed >= Settings.TURN_DURATION) {
							turnCounter++;
							turnStart = new Date();
							for (Castle castle: castles) {
								castle.tick();
								for (Castle c:castles) {
									for (Order o:c.getOrders()) {
										refreshTroops(o);
									}
								}
								
								if (statusBar.isVisible()) {
									refreshStatusBar();
								}
							}
						}
					}
				}
				
				processInput(input, now);


				// update sprites in scene
				enemies.forEach(sprite -> sprite.updateUI());
				missiles.forEach(sprite -> sprite.updateUI());

				// check if sprite can be removed
				enemies.forEach(sprite -> sprite.checkRemovability());
				missiles.forEach(sprite -> sprite.checkRemovability());

				// update score, health, etc
				update();
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

	private void loadGame() {
		input = new Input(scene);
		input.addListeners();

		createStatusBar();
		popupAttack = new PopupAttack(root);
		
		/*Here's what to do when the player click on the scene*/
		scene.setOnMousePressed(e -> {
			// Click coordinates to Grid coordinates
			Point GridClick = new Point();
			GridClick.x = (int) (e.getX() - this.gridStart.x) / this.gridSize;
			GridClick.y = (int) (e.getY() - this.gridStart.y) / this.gridSize;
			
			manageClicks(GridClick);
		});
	}
	
	public void manageClicks(Point p) {
		
		if (!popupAttack.isVisible()) {
			for (Castle castle: castles) {
				if (castle.getLocation().equals(p)) {
					selectedCastle = castle;
					refreshStatusBar();
					return;
				}
			}		
		}
	}
	
	
	/* Refresh all the content of the Text element in the statusBar.
	 * All the information is regarding the castle: selectedCastle.*/
	public void refreshStatusBar() {
		
		/* Show more information if the castle is owned by the player */
		boolean bool = selectedCastle.getOwner().isPlayer();
		attackButton.setVisible(bool);
		statsProduction.setVisible(bool);
		addSpearmanButton.setVisible(bool);
		addKnightButton.setVisible(bool);
		addCatapultButton.setVisible(bool);
		addLevelButton.setVisible(bool);
				
		// Castle Stats
		textCastleName.setText(selectedCastle.getNickname());
		textCastleName.setFill(selectedCastle.getOwner().getColor());
		if (selectedCastle.getOwner().isNeutral()) {
			textCastleOwner.setText("Appartient à un baron sans ambition");
		} else {
			textCastleOwner.setText("Appartient à : " + selectedCastle.getOwner().toString());
		}
		textCastleLevel.setText("Niveau : " + Integer.toString(selectedCastle.getLevel()));
		textCastleMoney.setText("Trésor : " + Integer.toString(selectedCastle.getMoney()));
		
		//Troops Stats
		textCastleSpear.setText("Piquier : " + Integer.toString(selectedCastle.getTroops(new Spearman()).size()));
		textCastleKnight.setText("Chevalier : " + Integer.toString(selectedCastle.getTroops(new Knight()).size()));
		textCastleCatapult.setText("Onagre : " + Integer.toString(selectedCastle.getTroops(new Catapult()).size()));
		textCastleTroopTotal.setText("Total : " + Integer.toString(selectedCastle.getTroops().size()));
		
		//Production Stats
		Production tmp;
		int index = 0;
		for (Text text: textProductions) {
			tmp = selectedCastle.getProduction(index);
			text.setText("");
			if (tmp != null) {
				text.setText(tmp.getName() + " (" + (tmp.getTotalTime() - tmp.getTimeRemaining()) + "/" + tmp.getTotalTime() + ")");
			}
			index++;
		}
		
		attackButton.setOnAction(value ->  {
        	popupAttack.show();
        	popupAttack.shareValues(selectedCastle, gridStart, gridSize, castles);
        	popupAttack.refreshPopup();
        });
		
		// Refresh Tooltip
		addLevelButton.getTooltip().setText("Coût : " + selectedCastle.costToLevel() + "\nTemps : " + selectedCastle.timeToLevel());;
		
	}
	
	
	public void createStatusBar() {

		statusBar.setHgap(0);
		statusBar.setVgap(0);
		statusBar.setPadding(new Insets(0, 0, 0, 0));
		//statusBar.setGridLinesVisible(true);
	    
		statusBar.getStyleClass().add("statusBar");
		statusBar.relocate(0, Settings.SCENE_HEIGHT - Settings.STATUS_BAR_HEIGHT);
		statusBar.setPrefSize(Settings.SCENE_WIDTH, Settings.STATUS_BAR_HEIGHT);
		
		statusBar.getColumnConstraints().add(new ColumnConstraints(700));
		statusBar.getColumnConstraints().add(new ColumnConstraints(500));
		statusBar.getColumnConstraints().add(new ColumnConstraints(500));
		
	    root.getChildren().add(statusBar);
	    
	    statusBar.add(statsCastle, 0, 0);
	    statusBar.add(statsTroops, 1, 0);
	    statusBar.add(statsProduction, 2, 0);
		
	    textCastleName.getStyleClass().add("title");
	    attackButton.getStyleClass().add("normal");
		textCastleOwner.getStyleClass().add("subtitle");
		textCastleLevel.getStyleClass().add("normal");
		textCastleMoney.getStyleClass().add("normal");
		
		textCastleSpear.getStyleClass().add("normal");
		textCastleKnight.getStyleClass().add("normal");
		textCastleCatapult.getStyleClass().add("normal");
		textCastleTroopTotal.getStyleClass().add("normal");
		
		// Castle Stats
		statsCastle.getColumnConstraints().add(new ColumnConstraints(150));
		statsCastle.getColumnConstraints().add(new ColumnConstraints(150));
		addLevelButton.getStyleClass().add("addButton");
		
		addLevelButton.setOnAction(value ->  {
        	selectedCastle.levelUp();
        	refreshStatusBar();
        });
		
		addLevelButton.setTooltip(new Tooltip(""));
		
		statsCastle.add(attackButton, 3, 0);
		statsCastle.add(textCastleName, 0, 0, 2, 1);
		statsCastle.add(textCastleOwner, 0, 1, 2, 1);
		statsCastle.add(textCastleLevel, 0, 2);
		statsCastle.add(textCastleMoney, 0, 3);
		statsCastle.add(addLevelButton, 1, 2);
		
		statsCastle.setMargin(textCastleOwner, new Insets(0,0,20,0));
		
		//Troops Stats
		statsTroops.add(textCastleSpear, 0, 0);
		statsTroops.add(textCastleKnight, 0, 1);
		statsTroops.add(textCastleCatapult, 0, 2);
		statsTroops.add(textCastleTroopTotal, 0, 3);
        
        addSpearmanButton.getStyleClass().add("addButton");
        addKnightButton.getStyleClass().add("addButton");
        addCatapultButton.getStyleClass().add("addButton");
        
        
        // Adds tooltips
        Spearman tmpSpear = new Spearman();        
        addSpearmanButton.setTooltip(new Tooltip( "Vitesse : " + tmpSpear.getSpeed()
        									   	+ "\nVie : " + tmpSpear.getHealth()
        										+ "\nDégats : " + tmpSpear.getDamage()
        										+ "\n\nCoût: " + tmpSpear.getCostProduction()
        										+ "\nTemps : " + tmpSpear.getTimeProduction()));

        Knight tmpKnight = new Knight(); 
        addKnightButton.setTooltip(new Tooltip( "Vitesse : " + tmpKnight.getSpeed()
											   	+ "\nVie : " + tmpKnight.getHealth()
												+ "\nDégats : " + tmpKnight.getDamage()
												+ "\n\nCoût: " + tmpKnight.getCostProduction()
												+ "\nTemps : " + tmpKnight.getTimeProduction()));
        
        Catapult tmpCatapult = new Catapult(); 
        addCatapultButton.setTooltip(new Tooltip( "Vitesse : " + tmpCatapult.getSpeed()
											   	+ "\nVie : " + tmpCatapult.getHealth()
												+ "\nDégats : " + tmpCatapult.getDamage()
												+ "\n\nCoût: " + tmpCatapult.getCostProduction()
												+ "\nTemps : " + tmpCatapult.getTimeProduction()));
        
        
        statsTroops.getColumnConstraints().add(new ColumnConstraints(150));

        addSpearmanButton.setOnAction(value ->  {
        	selectedCastle.addProduction(new Spearman());
        	refreshStatusBar();
        });

        addKnightButton.setOnAction(value ->  {
        	selectedCastle.addProduction(new Knight());
        	refreshStatusBar();
        });

        addCatapultButton.setOnAction(value ->  {
        	selectedCastle.addProduction(new Catapult());
        	refreshStatusBar();
        });
		
		statsTroops.add(addSpearmanButton, 1, 0);
		statsTroops.add(addKnightButton, 1, 1);
		statsTroops.add(addCatapultButton, 1, 2);
		
		statsTroops.setMargin(textCastleTroopTotal, new Insets(20,0,0,0));
		
		// Production stats
		
		int index = 0;
		for (Text text: textProductions) {
			
			text.getStyleClass().add("normal");
			statsProduction.add(text, 0, index);
			index++;
		}
	}
	
	private void gameOver() {
		HBox hbox = new HBox();
		hbox.setPrefSize(Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
		hbox.getStyleClass().add("message");
		Text message = new Text();
		message.getStyleClass().add("message");
		message.setText("Game over");
		hbox.getChildren().add(message);
		root.getChildren().add(hbox);
		gameLoop.stop();
	}

	private void update() {
		if (collision) {
			//scoreMessage.setText("Score : " + scoreValue + "          Life : " + player.getHealth());
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Object getElemInList(List list, Object target) {
		for (Object o:list) {
			if (target.equals(o)) {
				return o;
			}
		}
		return null;
	}
	
	public void addToLayer(Castle c) {
    	int x = this.gridStart.x + c.getLocation().x * this.gridSize;
    	int y = this.gridStart.y + c.getLocation().y * this.gridSize;
    	Rectangle shape = new Rectangle(x, y, this.gridSize * Settings.CASTLES_SIZE, this.gridSize * Settings.CASTLES_SIZE);
    	shape.setFill(c.getOwner().getColor());
    	this.playfieldLayer.getChildren().add(shape);
        
        int width = 0;
        int height = 0;
        
        switch(c.getDoorDirection()) {
        case 0:
        	x +=  0.25 * this.gridSize * Settings.CASTLES_SIZE;
        	width = (int) (this.gridSize * Settings.CASTLES_SIZE * 0.5);
        	height = (int) (this.gridSize * Settings.CASTLES_SIZE * 0.25);
        	break;
        case 1:
        	x += 0.75 * this.gridSize * Settings.CASTLES_SIZE;
        	y += 0.25 * this.gridSize * Settings.CASTLES_SIZE;
        	width = (int) (this.gridSize * Settings.CASTLES_SIZE * 0.25);
        	height = (int) (this.gridSize * Settings.CASTLES_SIZE * 0.5);
        	break;
        case 2:
        	x += 0.25 * this.gridSize * Settings.CASTLES_SIZE;
        	y += 0.75 * this.gridSize * Settings.CASTLES_SIZE;
        	width = (int) (this.gridSize * Settings.CASTLES_SIZE * 0.5);
        	height = (int) (this.gridSize * Settings.CASTLES_SIZE * 0.25);
        	break;
        case 3:
        	y += 0.25 * this.gridSize * Settings.CASTLES_SIZE;
        	width = (int) (this.gridSize * Settings.CASTLES_SIZE * 0.25);
        	height = (int) (this.gridSize * Settings.CASTLES_SIZE * 0.5);
        	break;
        }
        
        Rectangle door = new Rectangle(x, y, width, height);
        door.setFill(Color.WHITE);
        this.playfieldLayer.getChildren().add(door);  
    }
	
	public void refreshTroops(Order o) {
		for (Troop troop:o.getTroops()) {
			Rectangle r = troop.getShape();
			r.setX(this.gridStart.x + troop.getLocation().x * this.gridSize);
			r.setY(this.gridStart.y + troop.getLocation().y * this.gridSize);
		}
    }
	
	
	@SuppressWarnings("rawtypes")
	public static Object getRandomElemInList(List list) {
		int listLenght = list.size();
		if (!list.isEmpty()) {
			int randomIndex = getRandomIntegerBetweenRange(0, listLenght);
			return list.get(randomIndex);
		}
		return null;
	}
	
	/* Generate a random number between [min; max[ */
	public static int getRandomIntegerBetweenRange(double min, double max){
		max--;
	    double x = (int) (Math.random() * ((max-min) + 1)) + min;
	    return (int) x;

	}

	public static void main(String[] args) {
		launch(args);
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}

}