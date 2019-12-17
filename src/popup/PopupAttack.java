package popup;

import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import myGame.Castle;
import myGame.Order;
import myGame.Point;
import myGame.Settings;
import troop.Catapult;
import troop.Knight;
import troop.Spearman;

public class PopupAttack extends Popup{

	private Text textOstsCastleName[] = new Text[Settings.NUM_OSTS_SHOWN];
	private Text textOstsSpear[] = new Text[Settings.NUM_OSTS_SHOWN];
	private Text textOstsKnight[] = new Text[Settings.NUM_OSTS_SHOWN];
	private Text textOstsCatapult[] = new Text[Settings.NUM_OSTS_SHOWN];
	private Button buttonAddOrder = new Button("Envoyer un ost");
	
	private Castle selectedCastle;
	private boolean waitingToSelectCastle;
	private Castle targetSelectedCastle;
	
	
	public PopupTroop popupTroop;
	
	public PopupAttack(Group root) {
		super(root);
		createPopup();
		hide();
		this.popupTroop = new PopupTroop(root);
	}

	@Override
	public void createPopup() {
		
		for(int i = 0; i < textOstsCastleName.length; i++) {textOstsCastleName[i] = new Text();}
		for(int i = 0; i < textOstsSpear.length; i++) {textOstsSpear[i] = new Text();}
		for(int i = 0; i < textOstsKnight.length; i++) {textOstsKnight[i] = new Text();}
		for(int i = 0; i < textOstsCatapult.length; i++) {textOstsCatapult[i] = new Text();}
		
		layer.getChildren().add(pane);
		//popupListOst.setVisible(false);
		
		/* Appearance Size and position of Popup */
		pane.getStyleClass().add("popup");
		pane.setPrefSize(Settings.POPUP_WIDTH, Settings.POPUP_HEIGHT);
		pane.setTranslateX((Settings.SCENE_WIDTH - Settings.POPUP_WIDTH) / 2);
		pane.setTranslateY(((Settings.SCENE_HEIGHT - Settings.STATUS_BAR_HEIGHT) - Settings.POPUP_HEIGHT) / 2);
		
		Button buttonExitPopup = new Button("X");
		buttonExitPopup.setOnAction(value ->  {
			hide();
        });
		pane.add(buttonExitPopup, 1, 0);
		pane.getRowConstraints().add(new RowConstraints(20));
		pane.getColumnConstraints().add(new ColumnConstraints(60));
		pane.getColumnConstraints().add(new ColumnConstraints(60));
		pane.getColumnConstraints().add(new ColumnConstraints(60));
		
		for (int i = 0; i < Settings.NUM_OSTS_SHOWN; i++) {
			pane.add(textOstsCastleName[i], 0, 1 + i * 2, 3, 1);
			pane.add(textOstsSpear[i], 0, 1 + i * 2 + 1);
			pane.add(textOstsKnight[i], 1, 1 + i * 2 + 1);
			pane.add(textOstsCatapult[i], 2, 1 + i * 2 + 1);
		}
		
		pane.add(buttonAddOrder, 0, Settings.NUM_OSTS_SHOWN + 1, 3, 1);
				
	}

	@Override
	public void shareValues(Castle selectedCastle, Point gridStart, int gridSize, List<Castle> castles) {
		this.selectedCastle = selectedCastle;
		
		layer.setOnMousePressed(e -> {
			
			if (waitingToSelectCastle) {
				// Click coordinates to Grid coordinates
				Point GridClick = new Point();
				GridClick.x = (int) (e.getX() - gridStart.x) / gridSize;
				GridClick.y = (int) (e.getY() - gridStart.y) / gridSize;
				
				for (Castle castle: castles) {
					if (castle.getLocation().equals(GridClick)) {
						if (castle != selectedCastle) {							
							targetSelectedCastle = castle;
						}
					}
				}
			}
		
		});
		
		buttonAddOrder.setOnAction(value ->  {
			targetSelectedCastle = null;
			waitingToSelectCastle = true;
			pane.setVisible(false);
        });
		
	}
	
	@Override
	public void refreshPopup() {		
		Order tmp;
		for (int i = 0; i < Settings.NUM_OSTS_SHOWN; i++) {
			tmp = selectedCastle.getOrder(i);
			if (tmp != null) {
				textOstsCastleName[i].setFill(tmp.getTarget().getOwner().getColor());
				textOstsCastleName[i].setText(tmp.getTarget().getNickname());
				textOstsSpear[i].setText("S : " + tmp.getTroops(new Spearman()).size());
				textOstsKnight[i].setText("K : " + tmp.getTroops(new Knight()).size());
				textOstsCatapult[i].setText("C : " + tmp.getTroops(new Catapult()).size());
			}
		}
		
	}
	
	@Override
	public void main() {
		
		
		gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				refreshPopup();
				if (waitingToSelectCastle) {
					if (targetSelectedCastle != null) {
						waitingToSelectCastle = false;
						popupTroop.show();
						popupTroop.shareValues(selectedCastle, targetSelectedCastle);
						popupTroop.refreshPopup();
						pane.setVisible(true);
					}
				}
			}
		};
		gameLoop.start();
	}
	

}
