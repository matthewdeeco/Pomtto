package game.grid;

import java.awt.*;
import java.awt.event.*;

import javax.swing.Timer;

import game.*;
import game.grid.event.*;
import game.pom.*;
import game.utility.Dialog;
import game.utility.ImageFactory;

public class PlayerGrid extends GameGrid implements KeyEventDispatcher {
	private Timer updatePomGridTimer = new Timer(1000, new ResyncListener());
	private Timer acceptPressesTimer = new Timer(300, new AcceptPressesListener());
	private boolean acceptPresses = true;
	private int dipomsPlaced = 0;
			
	public PlayerGrid(Connection conn, int avatarIndex) {
		super(conn, avatarIndex);
		bgImage = ImageFactory.createImage("map/green_map.png");
		borderImage = ImageFactory.createImage("map/green_border.png");
		populate();
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
        updatePomGridTimer.setRepeats(true);
        updatePomGridTimer.start();
        createDipom();
	}
	
	private void populate(){
		for (int i = 1; i < rows - 1; i++)
			for (int j = 10; j < visibleCols - 1; j++)
				setPomAt(i, j, PomFactory.createRandomPom());
	}
	
	@Override
	public void gameOver() {
		sendEvent(new GameOver());
		super.gameOver();
	}

	@Override
	protected void createDipom() {
		dipom = new Dipom();
		dipom.setX((rows / 2) * tileWidth);
		dipom.setY(0);
		GridEvent event = new DipomCreated(dipom); 
		sendEvent(event);
		if (isGameOver())
			gameOver();
	}

	@Override
	public void dipomCreated(Dipom dipom) {
	}
	
	protected void dipomPlaced() {
		Pom topPom = dipom.getTopPom();
		Pom bottomPom = dipom.getBottomPom();
		int row = row(dipom.getX());
		int col = col(dipom.getY());
		setPomAt(row, col, topPom);
		setPomAt(row, col + 1, bottomPom);
		dipom = new NullDipom();
		dipomsPlaced++;
		chainPoms();
	}
	
	@Override
	protected boolean shouldAddMorePoms() {
		if (dipomsPlaced % 3 == 0) { // add more poms every 3 dipoms placed
			dipomsPlaced = 1;
			return true;
		} else
			return false;
	}
	
	@Override
	public void setPomAt(int row, int col, Pom pom) {
		super.setPomAt(row, col, pom);
		sendEvent(new SetPomAt(row, col, pom));
	}
	
	private void sendEvent(GridEvent event) {
		try {
			conn.writeObject(event);
		} catch (Exception e) {
			Dialog.errorMessage("Opponent disconnected!");
			System.exit(0);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		GridEvent event = null;
		if (e.getID() == KeyEvent.KEY_RELEASED) {
			if (e.getKeyCode() == KeyEvent.VK_DOWN)
				event = new MoveDipom(0, 5);
			else if (e.getKeyCode() == KeyEvent.VK_D)
				event = new Defend(currentCP);
			else if (e.getKeyCode() == KeyEvent.VK_A) {
				event = new Attack(currentCP);
				setCP(0);
			}
			else if (acceptPresses) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					event = new MoveDipom(-1, 0);
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					event = new MoveDipom(1, 0);
				else if (e.getKeyCode() == KeyEvent.VK_UP)
					event = new SwapDipom();
				if (event != null) {
					acceptPresses = false;
					acceptPressesTimer.start();
				}
			}
			if (event != null) {
				sendEvent(event);
				if (!(event instanceof Attack))
					event.invoke(this);
			}
		}
		return false;
	}
	
	class ResyncListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			sendEvent(new Resync(poms, currentCP));
		}
	}
	
	class AcceptPressesListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			acceptPresses = true; 
		}
	}

}