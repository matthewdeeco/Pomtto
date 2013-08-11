package game.grid;

import game.*;
import game.grid.event.*;
import game.pom.*;
import game.utility.ImageFactory;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

public abstract class GameGrid extends JPanel {
	private enum State {DIPOM_FALLING, BURSTING_POMS, DROPPING_POMS};
	
	protected final int rows = 9;
	protected final int cols = 15;
	protected final int tileWidth = 24;
	protected final int tileHeight = 22;

	private static final int MAX_CP = 999;
	
	protected Queue<GridEvent> commands;
	
	protected Connection conn;
	protected Dipom dipom;
	protected Pom[][] pomGrid;
	protected ImageIcon bgImage, avatarImage;
	
	protected boolean debugMode;
	
	private State state;
	private Integer currentCP;
	private boolean[][] blocked;
	private List<GridObserver> observers;
	
	public GameGrid(Connection conn, int avatarIndex) {
		this.conn = conn;
		int width = rows * tileWidth;
		int height = cols * tileHeight;
		setPreferredSize(new Dimension(width, height));
		initPomGrid();
		initBlocked();
		commands = new LinkedList<GridEvent>();
		currentCP = 0;
		avatarImage = ImageFactory.createAvatarImage("map", avatarIndex);
		dipom = new NullDipom();
		state = State.DIPOM_FALLING;
		observers = new ArrayList<GridObserver>();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		bgImage.paintIcon(this, g, 0, 0);
		avatarImage.paintIcon(this, g, tileWidth, 2 * tileHeight);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				getPomAt(i, j).paintIcon(this, g);
		dipom.paintIcon(this, g);
	}
	
	public void update() {
		if (state == State.DIPOM_FALLING) {
			moveDipomDown();
		} else if (state == State.DROPPING_POMS) {
			dropDownAllPoms();	
		} else if (state == State.BURSTING_POMS) {
			fadeChainedPoms();
		}
	}
	
	public abstract void createDipom();
	
	public void moveDipom(int dx, int dy) {
		tryMove(dx * Pom.WIDTH, dy);
	}
	
	private void moveDipomDown() {
		if (!tryMove(0, 3))
			dipomPlaced();
	}
	
	private boolean tryMove(int dx, int dy) {
		int newX = row(dipom.getX() + dx);
		int newY = col(dipom.getY() + dy);
		if (isFree(newX, newY) && isFree(newX, newY + 1)) {
			dipom.translate(dx, dy);
			return true;
		}
		return false;
	}
	
	protected int row(int x) {
		return (int) Math.ceil(x / (float)tileWidth);
	}
	
	protected int col(int y) {
		return (int) Math.ceil(y / (float)tileHeight);
	}
	
	private boolean isFree(int i, int j) {
		if (i < 0 || i >= rows || j < 0 || j >= cols)
			return false;
		else
			return (!blocked[i][j]) && (getPomAt(i, j).isNull());
	}
	
	protected abstract void dipomPlaced();
	
	public void chainPoms() {
		Set<PomChain> chains = new HashSet<PomChain>();
		chains.clear();
		for (int i = 1; i < rows - 1; i++)
			for(int j = 0; j < cols - 1; j++) {
				PomChain chain = new PomChain(this, i, j);
				if (!chain.isEmpty())
					chains.add(chain);
			}
		if (!chains.isEmpty()) {
			int burstScore = 0;
			for (PomChain chain: chains) {
				for (Point p: chain.getChainedCoords()) {
					//System.out.println(p.x + "," + p.y + "=" + pomGrid[p.x][p.y].toString());
					getPomAt(p.x, p.y).burstingState();
				}
				burstScore += chain.getScore();
			}
			AudioHandler.playBurstEffect();
			currentCP += burstScore;
			if (currentCP > MAX_CP)
				currentCP = MAX_CP;
			for (GridObserver observer: observers)
				observer.scoreChanged();
			burstScore = 0;
			fadeChainedPoms();
		} else { // no more chains
			state = State.DIPOM_FALLING;
			createDipom();
		}
	}

	private void fadeChainedPoms() {
		state = State.BURSTING_POMS;
		boolean fadedAPom = false;
		for (int i = 1; i < rows - 1; i++)
			for (int j = 0; j < cols - 1; j++) {
				Pom pom = getPomAt(i, j);
				if (pom.isBursting()) {
					if (pom.tryToFade())
						fadedAPom = true;
					else {
						setPomAt(i, j, Pom.NULL_POM);
					}
				}
			}
		if (!fadedAPom) {
			markPomsForDropping();
			dropDownAllPoms();
		}
	}
	
	private void markPomsForDropping() {
		for (int i = 1; i < rows - 1 ; i++) {
			int nullSpaces = 0;
			for (int j = cols - 2; j > 0; j--) {
				if (getPomAt(i, j).isNull())
					nullSpaces++;
				else if (nullSpaces > 0) {
					for (int k = j; k > 0; k--)
						getPomAt(i, k).increaseDropBy(nullSpaces * Pom.HEIGHT);
					nullSpaces = 0;
				}
			}
		}
	}
	
	private void dropDownAllPoms() {
		state = State.DROPPING_POMS;
		boolean movedAPom = false;
		for (int i = rows - 2; i > 0 ; i--) {
			for (int j = cols - 3; j > 1; j--) {
				Pom pom = getPomAt(i, j); 
				if (pom.isDropping()) {
					if (debugMode)
						System.out.println(String.format("drop[%d][%d]=%.2f", i, j, pom.getDroppingDistance()));
					if (pom.tryToDropBy(Pom.HEIGHT / 5.0f))
						movedAPom = true;
					else {
						pom.normalState();
						int row = row(pom.getX());
						int col = col(pom.getY());
						int colBeforeDrop = col(pom.getYBeforeDrop());
						setPomAt(row, col, pom);
						setPomAt(row, colBeforeDrop, Pom.NULL_POM);
					}
				}
			}
		}
		if (!movedAPom) // finished moving
			chainPoms();
	}
	
	public void swapDipom() {
		dipom.swap();
	}
	
	public void gameOver() {
		for (GridObserver observer: observers)
			observer.gameOver();
	}
	
	protected boolean isGameOver() {
		return (!isFree(row(dipom.getX()), col(dipom.getY()) + 1));
	}

	public Integer getScore() {
		return currentCP;
	}
	
	public Pom getPomAt(int row, int col) {
		return pomGrid[row][col];
	}

	public void setPomAt(int row, int col, Pom pom) { 
		if (row < 0 || row >= rows || col < 0 || col >= cols)
			System.err.println(String.format("pom[%d][%d]=%s", row, col, pom.getColor()));
		else {
			if (debugMode && !pomGrid[row][col].matchesColorOf(pom))
				System.out.println(String.format("pom[%d][%d]=%s", row, col, pom.getColor()));
			pomGrid[row][col] = pom;
			pom.setX(row * tileWidth);
			pom.setY(col * tileHeight);
			repaint();
		}
	}
	
	public void addGridObserver(GridObserver observer) {
		observers.add(observer);
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
	
	public void printPoms() {
		for (int i = 0; i < rows; i++)
			System.out.print(i + "\t");
		System.out.println();
		for (int j = 0; j < cols; j++) {
			System.out.print(j + "\t");
			for (int i = 0; i < rows; i++) {
				System.out.print(pomGrid[i][j].toString());
				System.out.print("\t");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public int getNumRows() {
		return rows;
	}
	
	public int getNumCols() {
		return cols;
	}
	
	public void toggleDebug() {
		debugMode = true;
		printPoms();
	}
}