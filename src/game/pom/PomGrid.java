package game.pom;

import java.awt.Component;
import java.awt.Graphics;

public class PomGrid {
	private Pom[][] grid;
	private int rows, visibleCols, cols;
	private boolean[][] blocked;
	
	public PomGrid(int rows, int visibleCols, int cols) {
		this.rows = rows;
		this.visibleCols = visibleCols;
		this.cols = cols;
		grid = new Pom[rows][cols];
		initBlocked();
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				grid[i][j] = Pom.NULL_POM;
	}
	
	public void paintIcon(Component c, Graphics g) {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				//if (!blocked[i][j])
					grid[i][j].paint(c, g);
	}
	
	public boolean isFree(int row, int col) {
		if (row < 0 || row >= rows || col < 0 || col >= visibleCols)
			return false;
		else
			return (!blocked[row][col] && grid[row][col].isNull());
	}
	
	public Pom get(int row, int col) {
		return grid[row][col];
	}

	public void set(int row, int col, Pom pom) { 
		grid[row][col] = pom;
	}
	
	public int getNumRows() {
		return rows;
	}
	
	public int getNumCols() {
		return visibleCols;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rows; i++)
			sb.append(i).append("\t");
		sb.append("\n");
		for (int j = 0; j < visibleCols; j++) {
			sb.append(j).append("\t");
			for (int i = 0; i < rows; i++)
				sb.append(grid[i][j].toString()).append("\t");
			sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}
	
	private void initBlocked() {
		blocked = new boolean[rows][cols];
		blocked[0][0] = true;
		blocked[0][1] = true;
		blocked[0][2] = true;
		blocked[0][3] = true;
		blocked[0][4] = true;
		blocked[0][5] = true;
		blocked[0][6] = true;
		blocked[0][7] = true;
		blocked[0][8] = true;
		blocked[0][9] = true;
		blocked[0][10] = true;
		blocked[0][11] = true;
		blocked[0][12] = true;
		blocked[0][13] = true;
		blocked[0][14] = true;
		blocked[1][0] = true;
		blocked[1][14] = true;
		blocked[2][0] = true;
		blocked[2][14] = true;
		blocked[3][0] = true;
		blocked[3][14] = true;
		blocked[4][14] = true;
		blocked[5][0] = true;
		blocked[5][14] = true;
		blocked[6][0] = true;
		blocked[6][14] = true;
		blocked[7][0] = true;
		blocked[7][14] = true;
		blocked[8][0] = true;
		blocked[8][1] = true;
		blocked[8][2] = true;
		blocked[8][3] = true;
		blocked[8][4] = true;
		blocked[8][5] = true;
		blocked[8][6] = true;
		blocked[8][7] = true;
		blocked[8][8] = true;
		blocked[8][9] = true;
		blocked[8][10] = true;
		blocked[8][11] = true;
		blocked[8][12] = true;
		blocked[8][13] = true;
		blocked[8][14] = true;
		for (int i = 0; i < rows; i++)
			for (int j = visibleCols; j < cols; j++)
				blocked[i][j] = true;
	}
}
