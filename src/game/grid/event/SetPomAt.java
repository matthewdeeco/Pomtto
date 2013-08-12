package game.grid.event;

import game.pom.Pom;
import game.pom.PomFactory;

public class SetPomAt extends GridEvent {
	private int row, col;
	private String color;
	
	public SetPomAt(int row, int col, Pom pom) {
		this.row = row;
		this.col = col;
		this.color = pom.getColor();
	}

	@Override
	public void invoke(GridEventListener listener) {
		Pom pom = PomFactory.createPom(color);
		listener.setPomAt(row, col, pom);
	}
}
