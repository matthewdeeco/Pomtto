package game.grid.event;

public class MoveDipomLeft extends GridEvent {

	@Override
	public void invoke(GridEventListener listener) {
		listener.moveDipomLeft();
	}

}
