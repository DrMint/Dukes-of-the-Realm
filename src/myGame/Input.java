package myGame;

import static javafx.scene.input.KeyCode.SPACE;
import static javafx.scene.input.KeyCode.ESCAPE;
import static javafx.scene.input.KeyCode.CONTROL;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.L;
import static javafx.scene.input.KeyCode.NUMPAD0;
import static javafx.scene.input.KeyCode.NUMPAD1;
import static javafx.scene.input.KeyCode.NUMPAD2;
import static javafx.scene.input.KeyCode.NUMPAD3;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.DECIMAL;

import java.util.BitSet;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Input {

	/**
	 * Bitset which registers if any {@link KeyCode} keeps being pressed or if it is
	 * released.
	 */
	private BitSet keyboardBitSet = new BitSet();
	private Scene scene = null;
	public Input(Scene scene) {
		this.scene = scene;
	}

	public void addListeners() {
		scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
		scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
	}

	public void removeListeners() {
		scene.removeEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
		scene.removeEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
	}

	/**
	 * "Key Pressed" handler for all input events: register pressed key in the
	 * bitset
	 */
	private EventHandler<KeyEvent> keyPressedEventHandler = event -> {
		// register key down
		keyboardBitSet.set(event.getCode().ordinal(), true);
		event.consume();
	};

	/**
	 * "Key Released" handler for all input events: unregister released key in the
	 * bitset
	 */
	private EventHandler<KeyEvent> keyReleasedEventHandler = new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent event) {
			// register key up
			keyboardBitSet.set(event.getCode().ordinal(), false);
			event.consume();
		}
	};

	private boolean is(KeyCode key) {
		boolean result = keyboardBitSet.get(key.ordinal());
		keyboardBitSet.set(key.ordinal(), false);
		return result;	
	}

	
	/* GETTERS AND SETTERS */
	
	public boolean isPause() {return is(SPACE);}
	public boolean isExit() {return is(ESCAPE);}
	public boolean isClose() {return is(DECIMAL);}
	public boolean isSave() {return is(CONTROL) && is(S);}
	public boolean isLoad() {return is(CONTROL) && is(L);}
	public boolean isLevelUp() {return is(NUMPAD0);}
	public boolean isAddTroop1() {return is(NUMPAD1);}
	public boolean isAddTroop2() {return is(NUMPAD2);}
	public boolean isAddTroop3() {return is(NUMPAD3);}
	public boolean isEnter() {return is(ENTER);}
}