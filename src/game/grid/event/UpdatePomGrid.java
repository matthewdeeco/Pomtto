package game.grid.event;

import game.pom.Pom;

public class UpdatePomGrid extends GridEvent {
	Pom[][] pomGrid;
	
	public UpdatePomGrid(Pom[][] pomGrid) {
		this.pomGrid = pomGrid;
	}

	@Override
	public void invoke(GridEventListener listener) {
		listener.updatePomGrid(pomGrid);
	}
	
	@Override
	public String toString() {
		return "";
	}

}
