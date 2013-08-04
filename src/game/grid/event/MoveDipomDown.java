package game.grid.event;

public class MoveDipomDown extends GridEvent {

	@Override
	public void invoke(GridEventListener listener) {
		listener.moveDipomDown();
	}

}
