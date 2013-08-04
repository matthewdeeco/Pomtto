package game.grid.event;

import game.pom.*;

public interface GridEventListener {
	public void spawnDipom(Dipom dipom);
	public void moveDipomLeft();
	public void moveDipomRight();
	public void moveDipomDown();
	public void updatePomGrid(Pom[][] pomGrid);
	public void swapDipom();
}
