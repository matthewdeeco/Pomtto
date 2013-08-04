package game.grid.event;

public class MoveDipomRight extends GridEvent {

	@Override
	public void invoke(GridEventListener listener) {
		listener.moveDipomRight();
	}

}
