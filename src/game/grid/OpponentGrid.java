package game.grid;

import game.Connection;
import game.grid.event.*;
import game.pom.*;
import game.utility.ImageFactory;

public class OpponentGrid extends GameGrid {

	public OpponentGrid(Connection conn, int avatarIndex) {
		super(conn, avatarIndex);
		bgImage = ImageFactory.createImage("map/blue_map.png");
		new Thread(new GridEventReceiver()).start();
	}
	
	private class GridEventReceiver implements Runnable {
		@Override
		public void run() {
			GridEvent event;
			while ((event = (GridEvent)conn.readObject()) != null) {
				commands.add(event);
			}
		}
	}

	@Override
	public void createDipom() {
	}
}
