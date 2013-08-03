package game.grid;

import connection.Connection;
import game.AudioHandler;
import game.pom.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

public abstract class GameGrid extends JPanel {
	protected final int rows = 9;
	protected final int cols = 15;
	protected final int tileWidth = 24;
	protected final int tileHeight = 22;
	protected final int width = rows * tileWidth;
	protected final int height = cols * tileHeight;

	private static final int MAX_CP = 999;
	
	protected Connection conn;
	protected Dipom dipom;
	protected Pom[][] pomGrid;
	protected boolean isFinishedFalling;
	protected ImageIcon bgImage;
	
	protected int curX, curY;
	private Integer currentCP;
	private boolean[][] blocked;
	private int burstScore;
	private GameGridObserver observer;
	
	public GameGrid(Connection conn, GameGridObserver observer) {
		this.conn = conn;
		this.observer = observer;
		this.setPreferredSize(new Dimension(width, height));
		setDoubleBuffered(true);
		initPomGrid();
		initBlocked();
		currentCP = 0;
		spawnDipom();
	}

	protected void spawnDipom() {
		curX = getX() + (rows / 2) * tileWidth;
		curY = getY();
		if (!isFree(row(curX), col(curY) + 1))
			observer.gameOver();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		bgImage.paintIcon(this, g, getX(), getY());
		for(int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				pomGrid[i][j].paintIcon(this, g);
	}
	
	public void update() {
		if (isFinishedFalling) {
			isFinishedFalling = false;
			spawnDipom();
		}
		else
			moveDipomDown();
	}
	
	protected boolean tryMove(Pom pom, int dx, int dy) {
		int newX = row(pom.getX() + dx);
		int newY = col(pom.getY() + dy);
		if (isFree(newX, newY)) {
			dipom.translate(dx, dy);
			return true;
		}
		return false;
	}
	
	protected boolean tryMove(int dx, int dy) {
		int newX = row(curX + dx);
		int newY = col(curY + dy);
		if (isFree(newX, newY) && isFree(newX, newY + 1)) {
			dipom.translate(dx, dy);
			curX += dx;
			curY += dy;
			return true;
		}
		return false;
	}
	
	protected void moveDipomLeft() {
		tryMove(-1 * Pom.WIDTH, 0);
	}
	
	protected void moveDipomRight() {
		tryMove(Pom.WIDTH, 0);
	}
	
	protected void moveDipomDown() {
		if (!tryMove(0, 5))
			dipomPlaced();
	}
	
	private int row(int x) {
		return (int) Math.ceil((x - this.getX()) / (float)tileWidth);
	}
	
	private int col(int y) {
		return (int) Math.ceil((y - this.getY()) / (float)tileHeight);
	}
	
	private boolean isFree(int i, int j) {
		if (i < 0 || i >= rows || j < 0 || j >= cols)
			return false;
		else
			return (!blocked[i][j]) && (pomGrid[i][j].isNull());
	}
	
	private void dipomPlaced() {
		Pom topPom = dipom.getTopPom();
		Pom bottomPom = dipom.getBottomPom();
		int row = row(curX);
		int col = col(curY);
		addToPomGrid(topPom, row, col);
		addToPomGrid(bottomPom, row, col + 1);

		burstChains();
		isFinishedFalling = true;
	}
	
	private void addToPomGrid(Pom pom, int row, int col) {
		pomGrid[row][col] = pom;
		snapToPlace(pom, row, col);
	}
	
	private void snapToPlace(Pom pom, int row, int col) {
		pom.setX(getX() + row * tileWidth);
		pom.setY(getY() + col * tileHeight);
	}
	
	private void burstChains() {
		Set<PomChain> chains = new HashSet<PomChain>();
		chains.clear();
		for (int i = 1; i < rows - 1; i++)
			for(int j = 0; j < cols - 1; j++) {
				PomChain chain = new PomChain(this, i, j);
				if (!chain.isEmpty())
					chains.add(chain);
			}
		if (!chains.isEmpty()) {
			for (PomChain chain: chains) {
				for (Point p: chain.getChainedCoords()) {
					System.out.println(p.x + "," + p.y + "=" + pomGrid[p.x][p.y].toString());
					pomGrid[p.x][p.y].burst();
				}
				burstScore += chain.getScore();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						new Timer(800, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								burstChainedPoms();
							}
						}).start();
					}
				});
			}
		}
	}

	protected void burstChainedPoms() {
		boolean hasPomToBurst = false;
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				if (pomGrid[i][j].isBursting()) {
					pomGrid[i][j] = Pom.NULL_POM;
					hasPomToBurst = true;
				}
		if (hasPomToBurst) {
			AudioHandler.playBurstEffect();
			currentCP += burstScore;
			if (currentCP > MAX_CP)
				currentCP = MAX_CP;
			observer.scoreChanged(currentCP);
			burstScore = 0;
			dropDownAllPoms();
		}
	}
	
	private void dropDownAllPoms() {
		for (int i = rows - 2; i > 0 ; i--) {
			int colPomCount = 0; 
			for (int j = cols - 2; j > 1; j--)
				if (!pomGrid[i][j].isNull())
					colPomCount++;
			
			for (int j = cols - 2; j > 1; j--) {
				if (colPomCount == 0)
					break;
				while (pomGrid[i][j].isNull()) {
					for (int k = j; k > 1; k--) {
						pomGrid[i][k] = pomGrid[i][k-1];
						snapToPlace(pomGrid[i][k], i, k);
					}
				}
				colPomCount--;
			}
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Timer(800, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						burstChains();
					}
				}).start();
			}
		});
	}
	
	public Pom pomAt(int row, int col) {
		return pomGrid[row][col];
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
	}
	
	private void initPomGrid() {
		pomGrid = new Pom[rows][cols];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				pomGrid[i][j] = Pom.NULL_POM;
	}
}
