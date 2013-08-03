package game.grid;

public interface GameGridObserver {
	public void gameOver();
	public void scoreChanged(Integer score);
}
