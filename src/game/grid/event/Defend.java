package game.grid.event;

public class Defend extends GridEvent {
	private int strength;
	
	public Defend(int cp) {
		strength = (cp + 1) / 100;
	}
	
	@Override
	public void invoke(GridEventListener listener) {
		listener.defend(strength);
	}

}
