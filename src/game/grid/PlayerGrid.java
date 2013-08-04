package game.grid;

import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.*;

import javax.swing.Timer;

import game.ImageFactory;
import game.grid.event.*;
import game.pom.Dipom;
import connection.Connection;

public class PlayerGrid extends GameGrid implements KeyEventDispatcher, ActionListener {
	private Timer timer = new Timer(1000, this);
			
	public PlayerGrid(Connection conn, GameGridObserver observer) {
		super(conn, observer);
		bgImage = ImageFactory.createImage("map/green_map.png");

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
        timer.start();
	}

	@Override
	public void spawnDipom() {
		super.spawnDipom();
		dipom = new Dipom(dipomX, dipomY);
		conn.writeObject(new SpawnDipom(dipom));
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (!isFinishedFalling) {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					moveDipomDown();
					conn.writeObject(new MoveDipomDown());
				}
			} else if (e.getID() == KeyEvent.KEY_RELEASED) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					moveDipomLeft();
					conn.writeObject(new MoveDipomLeft());
				}
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					moveDipomRight();
					conn.writeObject(new MoveDipomRight());
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP) {
					swapDipom();
					conn.writeObject(new SwapDipom());
			}
			}
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		conn.writeObject(new UpdatePomGrid(pomGrid));
		timer.start();
	}

}
