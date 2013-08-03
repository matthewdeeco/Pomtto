package game.grid;

import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.*;

import game.ImageFactory;
import game.pom.Dipom;
import connection.Connection;

public class PlayerGrid extends GameGrid implements KeyEventDispatcher {

	public PlayerGrid(Connection conn) {
		super(conn);
		bgImage = ImageFactory.createImage("map/green_map.png");

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
	}

	@Override
	protected void spawnDipom() {
		int startX = getX() + TILE_WIDTH * (WIDTH / 2);
		int startY = getY();
		dipom = new Dipom(startX, startY);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		dipom.paintIcon(this, g);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			if (e.getKeyCode() == KeyEvent.VK_DOWN)
				dipom.speedUp();
		} else if (e.getID() == KeyEvent.KEY_RELEASED) {
			if (e.getKeyCode() == KeyEvent.VK_DOWN)
				dipom.resetSpeed();
			else if (e.getKeyCode() == KeyEvent.VK_LEFT)
				moveDipomLeft();
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				moveDipomRight();
			else if (e.getKeyCode() == KeyEvent.VK_UP)
				dipom.swap();
		}
		return false;
	}

}
