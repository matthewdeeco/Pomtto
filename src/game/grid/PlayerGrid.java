package game.grid;

import java.awt.*;
import java.awt.event.*;

import javax.swing.Timer;

import game.*;
import game.grid.event.*;
import game.pom.*;
import game.utility.Dialog;
import game.utility.ImageFactory;

public class PlayerGrid extends GameGrid implements KeyEventDispatcher, GridEventListener {
	private Timer updatePomGridTimer = new Timer(2000, new UpdatePomGridListener());
	private Timer acceptPressesTimer = new Timer(400, new AcceptPressesListener());
	private boolean acceptPresses = true;
			
	public PlayerGrid(Connection conn, int avatarIndex) {
		super(conn, avatarIndex);
		bgImage = ImageFactory.createImage("map/green_map.png");
		populate();
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
        updatePomGridTimer.start();
        createDipom();
	}
	
	private void populate(){
		for (int i = 1; i < rows - 1; i++)
			for (int j = 10; j < cols - 1; j++)
				setPomAt(i, j, PomFactory.createRandomPom(0, 0));
	}
	
	@Override
	public void gameOver() {
		sendEvent(new GameOver());
		super.gameOver();
	}

	@Override
	public void createDipom() {
		dipom = new Dipom((rows / 2) * tileWidth, 0);
		GridEvent event = new DipomCreated(dipom); 
		sendEvent(event);
		if (isGameOver())
			gameOver();
	}
	
	protected void dipomPlaced() {
		Pom topPom = dipom.getTopPom();
		Pom bottomPom = dipom.getBottomPom();
		int row = row(dipom.getX());
		int col = col(dipom.getY());
		setPomAt(row, col, topPom);
		setPomAt(row, col + 1, bottomPom);
		
		chainPoms();
	}
	
	private void sendEvent(GridEvent event) {
		try {
			conn.writeObject(event);
		} catch (Exception e) {
			e.printStackTrace();
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
				event.invoke(this);
			}
		}
		return false;
	}
	
	class UpdatePomGridListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			final GridEvent updateEvent = new UpdatePomGrid(pomGrid);
			new Timer(2000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					sendEvent(updateEvent);
				}
			}).start();
			
			updatePomGridTimer.start();
		}
	}
	
	class AcceptPressesListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			acceptPresses = true; 
		}
	}

	@Override
	public void dipomCreated(Dipom dipom) {
	}

}