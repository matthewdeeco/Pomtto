package game.grid.event;

public class MoveDipom extends GridEvent {
	private int dx, dy;
	
	public MoveDipom(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	@Override
	public void invoke(GridEventListener listener) {
		listener.moveDipom(dx, dy);
	}
	
	@Override
	public String toString() {
		return String.format("Move %d, %d", dx, dy);
	}

}
