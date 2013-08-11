package game.grid.event;

public class ChainPoms extends GridEvent {

	@Override
	public void invoke(GridEventListener listener) {
		listener.chainPoms();
	}
	
	@Override
	public String toString() {
		return "Burst";
	}

}
