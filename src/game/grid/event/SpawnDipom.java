package game.grid.event;

import game.pom.Dipom;

public class SpawnDipom extends GridEvent {
	private Dipom dipom;
	
	public SpawnDipom(Dipom dipom) {
		this.dipom = dipom;
	}

	@Override
	public void invoke(GridEventListener listener) {
		listener.spawnDipom(dipom);
	}	
}
