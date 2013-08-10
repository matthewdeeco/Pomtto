package game.grid.event;

public class BurstChainedPoms extends GridEvent {

	@Override
	public void invoke(GridEventListener listener) {
		listener.burstChainedPoms();
	}
	
	@Override
	public String toString() {
		return "Burst";
	}

}
