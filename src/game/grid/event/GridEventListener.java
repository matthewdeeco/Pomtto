package game.grid.event;

import game.pom.*;

public interface GridEventListener {
	public void dipomCreated(Dipom dipom);
	public void moveDipom(int dx, int dy);
	public void updatePomGrid(Pom[][] pomGrid);
	public void swapDipom();
	public void burstChainedPoms();
}
