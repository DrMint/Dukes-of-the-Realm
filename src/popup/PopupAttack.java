package popup;

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

public class PopupAttack extends Popup {

	private Text textOstsCastleName[] = new Text[Settings.NUM_OSTS_SHOWN];
	private Text textOstsSpear[] = new Text[Settings.NUM_OSTS_SHOWN];
	private Text textOstsKnight[] = new Text[Settings.NUM_OSTS_SHOWN];
	private Text textOstsCatapult[] = new Text[Settings.NUM_OSTS_SHOWN];
	
	private boolean waitingToSelectCastle;
	
	
	public PopupTroop popupTroop;
	
	public PopupAttack() {
		hide();
		
		for(int i = 0; i < textOstsCastleName.length; i++) {textOstsCastleName[i] = new Text(); textOstsCastleName[i].getStyleClass().add("normal");}
		for(int i = 0; i < textOstsSpear.length; i++) {textOstsSpear[i] = new Text(); textOstsSpear[i].getStyleClass().add("subtitle");}
		for(int i = 0; i < textOstsKnight.length; i++) {textOstsKnight[i] = new Text(); textOstsKnight[i].getStyleClass().add("subtitle");}
		for(int i = 0; i < textOstsCatapult.length; i++) {textOstsCatapult[i] = new Text(); textOstsCatapult[i].getStyleClass().add("subtitle");}
		
		Main.root.getChildren().add(pane);
		
		/* Appearance Size and position of Popup */
		pane.getStyleClass().add("popup");
		pane.setPrefSize(Settings.POPUP_WIDTH, Settings.POPUP_HEIGHT);
		pane.setTranslateX((Settings.WINDOW_WIDTH - Settings.POPUP_WIDTH) / 2);
		pane.setTranslateY(((Settings.WINDOW_HEIGHT - Settings.STATUS_BAR_HEIGHT) - Settings.POPUP_HEIGHT) / 2);
		
		Button buttonExitPopup = new Button(Main.language.getProperty("closeButton"));
		buttonExitPopup.setOnAction(value ->  {buttonExitPopup();});
		pane.add(buttonExitPopup, 3, 0);
		
		pane.getRowConstraints().add(new RowConstraints(40));
		pane.getColumnConstraints().add(new ColumnConstraints(60));
		pane.getColumnConstraints().add(new ColumnConstraints(60));
		pane.getColumnConstraints().add(new ColumnConstraints(60));
		
		for (int i = 0; i < Settings.NUM_OSTS_SHOWN; i++) {
			pane.getRowConstraints().add(new RowConstraints(40));
			pane.getRowConstraints().add(new RowConstraints(20));
			pane.add(textOstsCastleName[i], 0, 1 + i * 2, 3, 1);
			pane.add(textOstsSpear[i], 0, 1 + i * 2 + 1);
			pane.add(textOstsKnight[i], 1, 1 + i * 2 + 1);
			pane.add(textOstsCatapult[i], 2, 1 + i * 2 + 1);
		}
		
		pane.getRowConstraints().add(new RowConstraints(100));
		
		Button buttonAddOrder = new Button(Main.language.getProperty("popupAttackSendButton"));
		buttonAddOrder.getStyleClass().add("addButton");
		pane.add(buttonAddOrder, 0, Settings.NUM_OSTS_SHOWN  * 2 + 1, 3, 1);
		buttonAddOrder.setOnAction(value ->  {buttonAddOrderClicked();});		

		this.popupTroop = new PopupTroop();
		
		Main.root.setOnMousePressed(e -> {	
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
				textOstsSpear[i].setText(Main.language.getProperty("spearMini") + tmp.getTroops(Spearman.class).size());
				textOstsKnight[i].setText(Main.language.getProperty("knightMini") + tmp.getTroops(Knight.class).size());
				textOstsCatapult[i].setText(Main.language.getProperty("catapultMini") + tmp.getTroops(Catapult.class).size());
			} else {
				textOstsCastleName[i].setText("");
				textOstsSpear[i].setText("");
				textOstsKnight[i].setText("");
				textOstsCatapult[i].setText("");
			}
		}
		
	}
	
	public void buttonExitPopup() {
		hide();
	}
	
	public void buttonAddOrderClicked() {
		waitingToSelectCastle = true;
		pane.setVisible(false);
	}

	public PopupTroop getPopupTroop() {return popupTroop;}
}
