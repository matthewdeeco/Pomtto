package game.grid;

public interface GridObserver {
	public void gameOver();
	public void scoreChanged(int score);
}
