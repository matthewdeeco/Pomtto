package game.grid.event;

import game.pom.*;

public interface GridEventListener {
	public void dipomCreated(Dipom dipom);
	public void moveDipom(int dx, int dy);
	public void setPomAt(int i, int j, Pom pom);
	public void swapDipom();
	public void chainPoms();
	public void gameOver();
}
