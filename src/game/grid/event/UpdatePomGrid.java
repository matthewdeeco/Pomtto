package game.grid.event;

import game.pom.Pom;
import game.pom.PomFactory;

public class UpdatePomGrid extends GridEvent {
	private String[][] pomColors;
	private int rows, cols;
	
	public UpdatePomGrid(Pom[][] pomGrid) {
		rows = pomGrid.length;
		cols = pomGrid[0].length;
		pomColors = new String[rows][cols];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				pomColors[i][j] = pomGrid[i][j].toString();
	}

	@Override
	public void invoke(GridEventListener listener) {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				String color = pomColors[i][j];
				Pom pom = PomFactory.createPom(color, 0, 0);
				listener.setPomAt(i, j, pom);
			}
	}
	
	@Override
	public String toString() {
		return "Update";
	}

}
