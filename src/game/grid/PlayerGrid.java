package game.grid;

import java.awt.*;
import java.awt.event.*;

import javax.swing.Timer;

import game.*;
import game.grid.event.*;
import game.pom.*;
import game.utility.Dialog;
import game.utility.ImageFactory;

public class PlayerGrid extends GameGrid implements KeyEventDispatcher, ActionListener {
	private Timer timer = new Timer(5000, this);
			
	public PlayerGrid(Connection conn, int avatarIndex) {
		super(conn, avatarIndex);
		bgImage = ImageFactory.createImage("map/green_map.png");
		
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
        timer.start();
        createDipom();
	}

	@Override
	public void createDipom() {
		Dipom dipom = new Dipom((rows / 2) * tileWidth, 0);
		GridEvent event = new DipomCreated(dipom); 
		sendEvent(event);
		commands.add(event);
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
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			if (e.getKeyCode() == KeyEvent.VK_DOWN)
				event = new MoveDipom(0, 1);
		}
		else if (e.getID() == KeyEvent.KEY_RELEASED) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT)
				event = new MoveDipom(-1, 0);
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				event = new MoveDipom(1, 0);
			else if (e.getKeyCode() == KeyEvent.VK_UP)
				event = new SwapDipom();
		}
		if (event != null) {
			sendEvent(event);
			commands.add(event);
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// System.out.println("Sent poms:");
		// printPoms();
		final GridEvent updateEvent = new UpdatePomGrid(pomGrid);
		new Timer(2000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendEvent(updateEvent);
			}
		}).start();
		
		timer.start();
	}

	@Override
	public void updatePomGrid(Pom[][] pomGrid) {
	}

}