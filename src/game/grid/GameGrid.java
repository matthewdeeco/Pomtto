package game.grid;

import game.*;
import game.grid.event.*;
import game.pom.*;
import game.utility.ImageFactory;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

public abstract class GameGrid extends JPanel implements GridEventListener {
	private enum State {DIPOM_FALLING, BURSTING_POMS, MOVING_POMS_UP, MOVING_POMS_DOWN};
	private static final int MAX_CP = 999;
	protected final int chainComboScore[] = {0, 10, 25, 50, 75};
	
	protected final int rows = 9;
	protected final int visibleCols = 15;
	/** Some of these columns are not visible in the grid. */
	protected final int cols = visibleCols + 9;
	protected final int tileWidth = 24;
	protected final int tileHeight = 22;
	
	protected Queue<GridEvent> commands;
	
	protected Connection conn;
	protected Dipom dipom;
	protected PomGrid poms;
	
	protected ImageIcon bgImage, borderImage, avatarImage;
	protected int currentCP;
	protected int comboCount;
	
	protected boolean debugMode;
	
	private State state;
	private List<GridObserver> observers;
	
	public GameGrid(Connection conn, int avatarIndex) {
		this.conn = conn;
		int width = rows * tileWidth;
		int height = visibleCols * tileHeight;
		setPreferredSize(new Dimension(width, height));
		poms = new PomGrid(rows, visibleCols, cols);
		randomizeInvisiblePoms();
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
		poms.paintIcon(this, g);
		borderImage.paintIcon(this, g, 0, 0);
		if (state == State.DIPOM_FALLING)
			dipom.paint(this, g);
	}
	
	public void update() {
		if (state == State.DIPOM_FALLING) {
			moveDipomDown();
		} else if (state == State.MOVING_POMS_UP || state == State.MOVING_POMS_DOWN) {
			moveAllPoms();	
		} else if (state == State.BURSTING_POMS) {
			fadeBurstingPoms();
		}
	}
	
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
		if (poms.isFree(newX, newY) && poms.isFree(newX, newY + 1)) {
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
	
	public void chainPoms() {
		Set<PomChain> chains = new HashSet<PomChain>();
		chains.clear();
		for (int i = 1; i < rows - 1; i++)
			for(int j = 0; j < visibleCols - 1; j++) {
				PomChain chain = new PomChain(poms, i, j);
				if (!chain.isEmpty())
					chains.add(chain);
			}
		if (!chains.isEmpty()) {
			int burstScore = 0;
			for (PomChain chain: chains) {
				for (Point p: chain.getChainedCoords())
					getPomAt(p.x, p.y).burstingState();
				burstScore += chain.getScore();
			}
			AudioHandler.playBurstEffect();
			setCP(currentCP + burstScore);
			AudioHandler.playComboEffect(comboCount);
			setCP(currentCP + chainComboScore[comboCount]);
			if (comboCount < chainComboScore.length  - 1)
				comboCount++;
			fadeBurstingPoms();
		} else if (shouldAddMorePoms())
			receiveAttack(1);
		else { // no more chains
			state = State.DIPOM_FALLING;
			comboCount = 0;
			createDipom();
		}
	}

	protected void fadeBurstingPoms() {
		state = State.BURSTING_POMS;
		boolean fadedAPom = false;
		for (int i = 1; i < rows - 1; i++)
			for (int j = 0; j < visibleCols - 1; j++) {
				Pom pom = getPomAt(i, j);
				if (pom.isBursting()) {
					if (pom.tryToFade())
						fadedAPom = true;
					else
						setPomAt(i, j, Pom.NULL_POM);
				}
			}
		if (!fadedAPom) {
			markPomsForDropping();
			moveAllPoms();
		}
	}
	
	private void markPomsForDropping() {
		for (int i = 1; i < rows - 1 ; i++) {
			int nullSpaces = 0;
			for (int j = visibleCols - 2; j > 0; j--) {
				if (getPomAt(i, j).isNull())
					nullSpaces++;
				else if (nullSpaces > 0) {
					float distanceToMove = nullSpaces * Pom.HEIGHT;
					for (int k = j; k > 0; k--)
						getPomAt(i, k).increaseMoveBy(distanceToMove);
					nullSpaces = 0;
				}
			}
		}
		state = State.MOVING_POMS_DOWN;
	}
	
	private void moveAllPoms() {
		boolean movedAPom = false;
		if (state == State.MOVING_POMS_UP) {
			for (int i = rows - 2; i > 0 ; i--)
				for (int j = 2; j < cols - 2; j++)
					if (movePom(i, j))
						movedAPom = true;
		} else {
			for (int i = rows - 2; i > 0 ; i--)
				for (int j = cols - 3; j > 1; j--)
					if (movePom(i, j))
						movedAPom = true;
		}
		if (!movedAPom) { // finished moving
			randomizeInvisiblePoms();
			chainPoms();
		}
	}
	
	/** @return true if the pom was moved. */
	private boolean movePom(int i, int j) {
		Pom pom = getPomAt(i, j); 
		if (pom.isDropping()) {
			float distanceToMove = Pom.HEIGHT / 4.0f;
			if (pom.tryToMoveBy(distanceToMove))
				return true;
			else {
				pom.normalState();
				int row = row(pom.getX());
				int col = col(pom.getY());
				int colBeforeMove = col(pom.getYBeforeMove());
				setPomAt(row, col, pom);
				setPomAt(row, colBeforeMove, Pom.NULL_POM);
				return false;
			}
		} else
			return false;
	}
	
	/** Assigns poms that come from below the map. */
	private void randomizeInvisiblePoms() {
		for (int i = 1; i < rows - 1; i++)
			for (int j = visibleCols - 1; j < cols; j++)
				if (getPomAt(i, j).isNull())
					setPomAt(i, j, PomFactory.createRandomPom());
	}

	@Override
	public void receiveAttack(int strength) {
		if (strength > 0) {
			for (int i = 1; i < rows - 1 ; i++) {
				for (int j = cols - 2; j > 0; j--) {
					if (!getPomAt(i, j).isNull()) {
						float distanceToMove = -1 * Pom.HEIGHT * strength;
						getPomAt(i, j).increaseMoveBy(distanceToMove);
					}
				}
			}
			moveAllPoms();
			state = State.MOVING_POMS_UP;
		}
	}

	@Override
	public void defend(int strength) {
		if (strength > 0) {
			for (int j = 0; j < strength; j++) {
				for (int i = 1; i < rows - 1; i++) {
					poms.get(i, visibleCols - 2 - j).burstingState();
				}
			}
			setCP(0);
			AudioHandler.playBurstEffect();
			fadeBurstingPoms();
		}
	}
	
	protected abstract void createDipom();
	protected abstract void dipomPlaced();
	protected abstract boolean shouldAddMorePoms();
	
	public void swapDipom() {
		dipom.swap();
	}
	
	public void gameOver() {
		for (GridObserver observer: observers)
			observer.gameOver();
	}
	
	protected boolean isGameOver() {
		int startX = dipom.getX();
		int startY = dipom.getY();
		return !poms.isFree(row(startX), col(startY) + 1);
	}
	
	public Pom getPomAt(int row, int col) {
		return poms.get(row, col);
	}

	public void setPomAt(int row, int col, Pom pom) {
		if (row < 0 || row >= rows || col < 0 || col >= cols) {
			System.err.println(String.format("pom[%d][%d]=%s", row, col, pom.getColor()));
			return;
		}
		if (debugMode && !poms.get(row, col).matchesColorOf(pom) && pom.isNull()) {
			System.out.println(String.format("pom[%d][%d]=%s", row, col, pom.getColor()));
		}
		poms.set(row, col, pom);
		pom.setX(row * tileWidth);
		pom.setY(col * tileHeight);
		repaint();
	}
	
	public void addGridObserver(GridObserver observer) {
		observers.add(observer);
	}
	
	public void setCP(int score) {
		currentCP = score;
		if (currentCP > MAX_CP)
			currentCP = MAX_CP;
		for (GridObserver observer: observers)
			observer.scoreChanged(currentCP);
	}
	
	public void toggleDebug() {
		debugMode = true;
		System.out.println(poms.toString());
	}
}