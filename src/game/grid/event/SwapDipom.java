package game.grid.event;

public class SwapDipom extends GridEvent {

	@Override
	public void invoke(GridEventListener listener) {
		listener.swapDipom();
	}
	
	@Override
	public String toString() {
		return "Swap";
	}

}
