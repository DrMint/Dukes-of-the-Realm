package troop;

public class Catapult extends Troop {
	
	public Catapult() {
		super(1000, 50, 1, 5, 10);
	}

	@Override
	public String toString() {
		return "Catapult";
	}
}
