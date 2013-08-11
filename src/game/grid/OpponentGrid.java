package game.grid;

import game.Connection;
import game.grid.event.*;
import game.pom.Dipom;
import game.utility.ImageFactory;

public class OpponentGrid extends GameGrid implements GridEventListener {

	public OpponentGrid(Connection conn, int avatarIndex) {
		super(conn, avatarIndex);
		bgImage = ImageFactory.createImage("map/blue_map.png");
		new Thread(new GridEventReceiver()).start();
	}
	
	public void dipomCreated(Dipom dipom) {
		this.dipom = dipom;
	}

	@Override
	protected void dipomPlaced(){
		
	}
	
	private class GridEventReceiver implements Runnable {
		@Override
		public void run() {
			GridEvent event;
			while ((event = (GridEvent)conn.readObject()) != null) {
				event.invoke(OpponentGrid.this);
			}
		}
	}

	@Override
	public void createDipom() {
	}
}
