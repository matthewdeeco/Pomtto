package game.grid;

import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import game.ImageFactory;
import game.grid.event.*;
import game.pom.*;
import connection.Connection;

public class OpponentGrid extends GameGrid implements GridEventListener {

	public OpponentGrid(Connection conn, GameGridObserver observer) {
		super(conn, observer);
		bgImage = ImageFactory.createImage("map/blue_map.png");
		new Thread(new GridEventReceiver()).start();
	}
	
	@Override
	public void spawnDipom() {
		dipom = new NullDipom();
	}
	
	/*
	@Override
	public void spawnDipom() {
		super.spawnDipom();
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					dipom = (Dipom)conn.readObject();
				}
				
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	@Override
	public void spawnDipom(Dipom dipom) {
		super.spawnDipom();
		this.dipom = dipom;
		repaint();
	}

	@Override
	public void updatePomGrid(Pom[][] pomGrid) {
		this.pomGrid = pomGrid;
		System.out.println("Pom grid updated");
		repaint();
	}
	
	private class GridEventReceiver implements Runnable {
		@Override
		public void run() {
			GridEvent event;
			while ((event = (GridEvent)conn.readObject()) != null)
				event.invoke(OpponentGrid.this);
		}	
	}
}
