package game.grid.event;

import game.pom.*;

public class Resync extends GridEvent {
	private String[][] pomColors;
	private int rows, cols;
	private int score;
	
	public Resync(PomGrid pomGrid, int score) {
		this.score = score;
		rows = pomGrid.getNumRows();
		cols = pomGrid.getNumCols();
		pomColors = new String[rows][cols];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				pomColors[i][j] = pomGrid.get(i, j).getColor();
	}

	@Override
	public void invoke(GridEventListener listener) {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				String color = pomColors[i][j];
				Pom pom = PomFactory.createPom(color);
				listener.setPomAt(i, j, pom);
			}
		listener.setCP(score);
	}
	
	@Override
	public String toString() {
		return "Update";
	}

}
