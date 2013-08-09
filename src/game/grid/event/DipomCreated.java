package game.grid.event;

import game.pom.Dipom;

public class DipomCreated extends GridEvent {
	private Dipom dipom;
	
	public DipomCreated(Dipom dipom) {
		this.dipom = dipom;
	}

	@Override
	public void invoke(GridEventListener listener) {
		listener.dipomCreated(dipom);
	}
	
	@Override
	public String toString() {
		return "Create " + dipom.toString();
	}
}
