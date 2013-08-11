package game.grid.event;

public class GameOver extends GridEvent {

	@Override
	public void invoke(GridEventListener listener) {
		listener.gameOver();
	}
	
}
