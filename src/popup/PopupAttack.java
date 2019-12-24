package popup;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import myGame.Castle;
import myGame.Main;
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
	
	private boolean waitingToSelectCastle;
	
	
	public PopupTroop popupTroop;
	
	public PopupAttack(Group root) {
		super(root);
		hide();
		
		for(int i = 0; i < textOstsCastleName.length; i++) {textOstsCastleName[i] = new Text();}
		for(int i = 0; i < textOstsSpear.length; i++) {textOstsSpear[i] = new Text();}
		for(int i = 0; i < textOstsKnight.length; i++) {textOstsKnight[i] = new Text();}
		for(int i = 0; i < textOstsCatapult.length; i++) {textOstsCatapult[i] = new Text();}
		
		layer.getChildren().add(pane);
		
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
		
		Button buttonAddOrder = new Button("Envoyer un ost");
		pane.add(buttonAddOrder, 0, Settings.NUM_OSTS_SHOWN + 1, 3, 1);
		buttonAddOrder.setOnAction(value ->  {
			waitingToSelectCastle = true;
			pane.setVisible(false);
        });		

		this.popupTroop = new PopupTroop(root);
		
		layer.setOnMousePressed(e -> {	
			if (waitingToSelectCastle) {
				Point p = new Point((int) e.getX(), (int) e.getY());
				p = Main.pixelToGridCoordinates(p);
				
				Castle targetSelectedCastle = Main.getCastleFromPoint(p);
				if (targetSelectedCastle != null && targetSelectedCastle != Main.selectedCastle) {
					waitingToSelectCastle = false;
					popupTroop.setTargetSelectedCastle(targetSelectedCastle);
					popupTroop.refresh();
					popupTroop.show();
					pane.setVisible(true);
				}
			}
		});
	}
	
	@Override
	public void refresh() {
		this.needRefresh = false;
		this.popupTroop.needRefresh = false;
		Order tmp;
		for (int i = 0; i < Settings.NUM_OSTS_SHOWN; i++) {
			tmp = Main.selectedCastle.getOrder(i);
			if (tmp != null) {
				textOstsCastleName[i].setFill(tmp.getTarget().getOwner().getColor());
				textOstsCastleName[i].setText(tmp.getTarget().getNickname());
				textOstsSpear[i].setText("S : " + tmp.getTroops(new Spearman()).size());
				textOstsKnight[i].setText("K : " + tmp.getTroops(new Knight()).size());
				textOstsCatapult[i].setText("C : " + tmp.getTroops(new Catapult()).size());
			} else {
				textOstsCastleName[i].setText("");
				textOstsSpear[i].setText("");
				textOstsKnight[i].setText("");
				textOstsCatapult[i].setText("");
			}
		}
		
	}

	public PopupTroop getPopupTroop() {
		return popupTroop;
	}
}
