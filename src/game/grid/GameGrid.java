package game.grid;

import game.*;
import game.grid.event.*;
import game.pom.*;
import game.utility.ImageFactory;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public abstract class GameGrid extends JPanel implements GridEventListener {
	protected final int rows = 9;
	protected final int cols = 15;
	protected final int tileWidth = 24;
	protected final int tileHeight = 22;

	private static final int MAX_CP = 999;
	
	protected Queue<GridEvent> commands;
	
	protected Connection conn;
	protected Dipom dipom;
	protected Pom[][] pomGrid;
	protected ImageIcon bgImage;
	protected ImageIcon avatarImage;

	private boolean performingAction;
	private Integer currentCP;
	private boolean[][] blocked;
	private int burstScore;
	private GameGridObserver observer;
	
	public GameGrid(Connection conn, int avatarIndex) {
		this.conn = conn;
		int width = rows * tileWidth;
		int height = cols * tileHeight;
		this.setPreferredSize(new Dimension(width, height));
		initPomGrid();
		initBlocked();
		commands = new LinkedList<GridEvent>();
		currentCP = 0;
		avatarImage = ImageFactory.createAvatarImage("map", avatarIndex);
		dipom = new NullDipom();
	}
	
	public abstract void createDipom();
	
	public void dipomCreated(Dipom dipom) {
		this.dipom = dipom;
		if (!isFree(row(dipom.getX()), col(dipom.getY()) + 1))
			observer.gameOver();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		bgImage.paintIcon(this, g, 0, 0);
		avatarImage.paintIcon(this, g, tileWidth, 2 * tileHeight);
		for(int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				pomGrid[i][j].paintIcon(this, g);
		dipom.paintIcon(this, g);
	}
	
	public void update() {
		if (commands.isEmpty()) // nothing to do
			moveDipomDown();
		else if (!performingAction) {
			GridEvent event = commands.remove();
			event.invoke(this);
			System.out.println(event.toString());
		}
	}
	
	protected boolean tryMove(Pom pom, int dx, int dy) {
		int newX = row(pom.getX() + dx);
		int newY = col(pom.getY() + dy);
		if (isFree(newX, newY)) {
			pom.translate(dx, dy);
			return true;
		}
		return false;
	}
	
	protected boolean tryMove(int dx, int dy) {
		int newX = row(dipom.getX() + dx);
		int newY = col(dipom.getY() + dy);
		if (isFree(newX, newY) && isFree(newX, newY + 1)) {
			dipom.translate(dx, dy);
			return true;
		}
		return false;
	}
	
	public void moveDipom(int dx, int dy) {
		tryMove(dx * Pom.WIDTH, dy * Pom.HEIGHT);
	}
	
	private void moveDipomDown() {
		if (!tryMove(0, 2))
			dipomPlaced();
	}
	
	private int row(int x) {
		return (int) Math.ceil(x / (float)tileWidth);
	}
	
	private int col(int y) {
		return (int) Math.ceil(y / (float)tileHeight);
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
		int row = row(dipom.getX());
		int col = col(dipom.getY());
		addToPomGrid(topPom, row, col);
		addToPomGrid(bottomPom, row, col + 1);
		
		burstChainedPoms();
		//commands.add(new BurstChainedPoms());
	}
	
	private void addToPomGrid(Pom pom, int row, int col) {
		pomGrid[row][col] = pom;
		snapToPlace(pom, row, col);
	}
	
	private void snapToPlace(Pom pom, int row, int col) {
		pom.setX(row * tileWidth);
		pom.setY(col * tileHeight);
	}

	public void burstChainedPoms() {
		chainPoms();
		boolean hasPomToBurst = false;
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				if (pomGrid[i][j].isBursting()) {
					pomGrid[i][j] = Pom.NULL_POM;
					hasPomToBurst = true;
				}
		if (hasPomToBurst) {
			performingAction = true;
			AudioHandler.playBurstEffect();
			currentCP += burstScore;
			if (currentCP > MAX_CP)
				currentCP = MAX_CP;
			observer.scoreChanged();
			burstScore = 0;
			dropDownAllPoms();
			performingAction = false;
			commands.add(new BurstChainedPoms()); // there may be new chains
		} else { // no more chains
			performingAction = false;
			createDipom();
		}
	}
	
	private void chainPoms() {
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
					//System.out.println(p.x + "," + p.y + "=" + pomGrid[p.x][p.y].toString());
					pomGrid[p.x][p.y].burst();
				}
				burstScore += chain.getScore();
			}
		}
	}

	public Integer getScore() {
		return currentCP;
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
	}
	
	public void swapDipom() {
		dipom.swap();
	}
	
	public Pom pomAt(int row, int col) {
		return pomGrid[row][col];
	}
	
	public void setGameGridObserver(GameGridObserver observer) {
		this.observer = observer;
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
	
	protected void printPoms() {
		for (int j = 0; j < cols; j++) {
			for (int i = 0; i < rows; i++) {
				if (blocked[i][j])
					System.out.print("BLKD");
				else
					System.out.print(pomGrid[i][j].toString());
				System.out.print("\t");
			}
			System.out.println();
		}
		System.out.println();
	}
}
