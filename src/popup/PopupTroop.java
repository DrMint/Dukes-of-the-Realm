package popup;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import myGame.Castle;
import myGame.Main;
import myGame.Settings;
import troop.Catapult;
import troop.Knight;
import troop.Spearman;
import troop.Troop;

public class PopupTroop extends Popup{

	private Text textSpearAvailable = new Text();
	private Text textKnightAvailable = new Text();
	private Text textCatapultAvailable = new Text();
	private Text textTotalAvailable = new Text();
	
	private Text textSpearUsed = new Text();
	private Text textKnightUsed = new Text();
	private Text textCatapultUsed = new Text();
	private Text textTotalUsed = new Text();
		
	private int SpearAvailable = 0;
	private int KnightAvailable = 0;
	private int CatapultAvailable = 0;
	private int TotalAvailable = 0;
	
	private int SpearUsed = 0;
	private int KnightUsed = 0;
	private int CatapultUsed = 0;
	private int TotalUsed = 0;
	
	private Castle targetSelectedCastle;
	
	public PopupTroop(Group root) {
		super(root);
		hide();		
		
		layer.getChildren().add(pane);
		pane.toFront();
		
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
		pane.getColumnConstraints().add(new ColumnConstraints(100));
		pane.getColumnConstraints().add(new ColumnConstraints(100));
		pane.getColumnConstraints().add(new ColumnConstraints(100));
		
		pane.add(textSpearAvailable, 0, 1);
		pane.add(textKnightAvailable, 0, 2);
		pane.add(textCatapultAvailable, 0, 3);
		pane.add(textTotalAvailable, 0, 4);
		
		Button buttonAddSpear = new Button("+");
		buttonAddSpear.setOnAction(value ->  {addSpear();});
		
		Button buttonAddKnight = new Button("+");
		buttonAddKnight.setOnAction(value ->  {addKnight();});
		
		Button buttonCatapult = new Button("+");
		buttonCatapult.setOnAction(value ->  {addCatapult();});
		
		pane.add(buttonAddSpear, 1, 1);
		pane.add(buttonAddKnight, 1, 2);
		pane.add(buttonCatapult, 1, 3);
		
		pane.add(textSpearUsed, 2, 1);
		pane.add(textKnightUsed, 2, 2);
		pane.add(textCatapultUsed, 2, 3);
		pane.add(textTotalUsed, 2, 4);
		
		Button confirmButton = new Button("Confirmer");
		confirmButton.setOnAction(value ->  {
			if (TotalUsed != 0) {
				List<Troop> troops = new ArrayList<>();
				for (int i = 0; i < SpearUsed; i++) {troops.add(Main.selectedCastle.getTroops(new Spearman()).get(i));}
				for (int i = 0; i < KnightUsed; i++) {troops.add(Main.selectedCastle.getTroops(new Knight()).get(i));}
				for (int i = 0; i < CatapultUsed; i++) {troops.add(Main.selectedCastle.getTroops(new Catapult()).get(i));}
				
				Main.selectedCastle.addOrder(this.targetSelectedCastle, troops);
			}
			hide();
			this.needRefresh = true;
        });
		
		pane.add(confirmButton, 0, 5, 3, 1);
	}

	public void addSpear() {
		if (SpearUsed < SpearAvailable) {
			SpearUsed++;
			refreshValues();
		}
	}
	
	public void addKnight() {
		if (KnightUsed < KnightAvailable) {
			KnightUsed++;
			refreshValues();
		}
	}
	
	public void addCatapult() {
		if (CatapultUsed < CatapultAvailable) {
			CatapultUsed++;
			refreshValues();
		}
	}
	
	@Override
	public void refresh() {
		this.needRefresh = false;
		SpearAvailable = Main.selectedCastle.getTroops(new Spearman()).size();
		KnightAvailable = Main.selectedCastle.getTroops(new Knight()).size();
		CatapultAvailable = Main.selectedCastle.getTroops(new Catapult()).size();
		TotalAvailable = SpearAvailable + KnightAvailable + CatapultAvailable;
		
		SpearUsed = 0;
		KnightUsed = 0;
		CatapultUsed = 0;
		
		refreshValues();
	}
	
	public void refreshValues() {
		TotalUsed = SpearUsed + KnightUsed + CatapultUsed;
		
		textSpearAvailable.setText("Spearman : " + (SpearAvailable - SpearUsed));
		textKnightAvailable.setText("Knight : " + (KnightAvailable - KnightUsed));
		textCatapultAvailable.setText("Catapult : " + (CatapultAvailable - CatapultUsed));
		textTotalAvailable.setText("Total : " + (TotalAvailable - TotalUsed));
		
		textSpearUsed.setText(Integer.toString(SpearUsed));
		textKnightUsed.setText(Integer.toString(KnightUsed));
		textCatapultUsed.setText(Integer.toString(CatapultUsed));
		textTotalUsed.setText(Integer.toString(TotalUsed));
	}

	public void setTargetSelectedCastle(Castle targetSelectedCastle) {
		this.targetSelectedCastle = targetSelectedCastle;
	}
}
