package game.grid;

import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.*;

import game.ImageFactory;
import game.pom.Dipom;
import connection.Connection;

public class PlayerGrid extends GameGrid implements KeyEventDispatcher {

	public PlayerGrid(Connection conn, GameGridObserver observer) {
		super(conn, observer);
		bgImage = ImageFactory.createImage("map/green_map.png");
		x = 0;
		y = 0;

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
        
        spawnDipom();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (!isFinishedFalling) {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN)
					moveDipomDown();
			} else if (e.getID() == KeyEvent.KEY_RELEASED) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					moveDipomLeft();
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					moveDipomRight();
				else if (e.getKeyCode() == KeyEvent.VK_UP)
					dipom.swap();
			}
		}
		return false;
	}

}
