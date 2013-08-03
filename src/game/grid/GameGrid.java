package game.grid;

import connection.Connection;
import game.AudioHandler;
import game.pom.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

public abstract class GameGrid extends JPanel {
	public static final int WIDTH = 9;
	public static final int HEIGHT = 15;
	public static final int TILE_WIDTH = 24;
	public static final int TILE_HEIGHT = 22;

	protected Connection conn;
	protected Dipom dipom;
	protected Pom[][] pomGrid;
	protected ImageIcon bgImage;
	private int currentCP;
	
	private boolean[][] blocked;
	
	public GameGrid(Connection conn) {
		this.conn = conn;
		int screenWidth = WIDTH * TILE_WIDTH;
		int screenHeight = HEIGHT * TILE_HEIGHT;
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		initPomGrid();
		initBlocked();
		spawnDipom();
	}

	protected abstract void spawnDipom();

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		bgImage.paintIcon(this, g, getX(), getY());
		for(int i = 0; i < WIDTH; i++)
			for (int j = 0; j < HEIGHT; j++)
				pomGrid[i][j].paintIcon(this, g);
	}
	
	public void update() {
		moveDipomDown();
		dropDownAllPoms();
		burstChains();
	}

	
	protected void moveDipomLeft() {
		int row = row(dipom.getTopPom());
		int col1 = col(dipom.getTopPom());
		int col2 = col(dipom.getBottomPom()); 
		boolean topPomCanMoveLeft = isFree(row - 1, col1);
		boolean botPomCanMoveLeft = isFree(row - 1, col2);
		if (topPomCanMoveLeft && botPomCanMoveLeft) 
			dipom.moveLeft();
	}
	
	protected void moveDipomRight() {
		int row = row(dipom.getTopPom());
		int col1 = col(dipom.getTopPom());
		int col2 = col(dipom.getBottomPom()); 
		boolean topPomCanMoveRight = isFree(row + 1, col1);
		boolean botPomCanMoveRight = isFree(row + 1, col2);
		if (topPomCanMoveRight && botPomCanMoveRight) 
			dipom.moveRight();
	}
	
	protected void moveDipomDown() {
		int row = row(dipom.getTopPom());
		int col1 = col(dipom.getTopPom());
		int col2 = col(dipom.getBottomPom()); 
		
		if (isFree(row, col2 + 1))
			dipom.moveDown();
		else
			placeDipom(row, col1);
	}
	
	private int row(Pom pom) {
		return (pom.getX() - this.getX()) / TILE_WIDTH;
	}
	
	private int col(Pom pom) {
		return (pom.getY() - this.getY()) / TILE_HEIGHT;
	}
	
	private boolean isFree(int i, int j) {
		return (!blocked[i][j]) && (pomGrid[i][j].isNull());
	}
	
	private void placeDipom(int row, int col) {
		Pom topPom = dipom.getTopPom();
		Pom bottomPom = dipom.getBottomPom();
		addToPomGrid(topPom, row, col);
		addToPomGrid(bottomPom, row, col + 1);

		spawnDipom();
	}
	
	private void addToPomGrid(Pom pom, int row, int col) {
		pomGrid[row][col] = pom;
		snapToPlace(pom, row, col);
	}
	
	private void snapToPlace(Pom pom, int row, int col) {
		pom.setX(getX() + row * TILE_WIDTH);
		pom.setY(getY() + col * TILE_HEIGHT);
	}
	
	private void burstChains() {
		Set<PomChain> chains = new HashSet<PomChain>();
		chains.clear();
		for (int i = 1; i < WIDTH - 1; i++)
			for(int j = 0; j < HEIGHT - 1; j++) {
				PomChain chain = new PomChain(this, i, j);
				if (!chain.isEmpty())
					chains.add(chain);
			}
		for (PomChain chain: chains) {
			for (Point p: chain.getChainedCoords()) {
				pomGrid[p.x][p.y].burst();
				pomGrid[p.x][p.y] = Pom.NULL_POM;
			}
			currentCP += chain.getScore();
		}
		if (!chains.isEmpty())
			AudioHandler.playBurstEffect();
	}
	
	private void dropDownAllPoms() {
		for (int j = HEIGHT - 1; j > 1; j--) {
			for (int i = 0; i < WIDTH; i++) {
				Pom pom = pomGrid[i][j];
				int row = row(pom);
				int col = col(pom); 
				
				if (pom.isNull()) {
				} else if (isFree(row, col + 1)) {
					movePomDown(pom, row, col);
				}
				else {
					pomGrid[row][col] = pom;
					snapToPlace(pom, row, col);
				}
			}
		}
	}
	
	private void movePomDown(Pom pom, int row, int col) {
		for (int i = 0; i < Pom.HEIGHT; i++) {
			pom.moveDown(1);
		}
	}
	
	private boolean shouldMoveDown(Pom pom, int row, int col) {
		boolean isOnGridEdge = blocked[row][col + 1];
		int bottomEdgeY = pom.getY() + Pom.HEIGHT;
		boolean isRightOnTopOfBottomPom = (bottomEdgeY == pomGrid[row][col + 1].getY());
		return !isOnGridEdge && !isRightOnTopOfBottomPom;
	}
	
	public Pom getPomAt(int row, int col) {
		return pomGrid[row][col];
	}
	
	private void initBlocked() {
		blocked = new boolean[WIDTH][HEIGHT];
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
	}
	
	private void initPomGrid() {
		pomGrid = new Pom[WIDTH][HEIGHT];
		for (int i = 0; i < WIDTH; i++)
			for (int j = 0; j < HEIGHT; j++)
				pomGrid[i][j] = Pom.NULL_POM;
	}
}
