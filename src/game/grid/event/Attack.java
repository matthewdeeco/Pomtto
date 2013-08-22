package game.grid.event;

public class Attack extends GridEvent {
	private int strength;
	
	public Attack(int cp) {
		strength = (cp + 1) / 100;
	}

	@Override
	public void invoke(GridEventListener listener) {
		listener.receiveAttack(strength);
	}

}
