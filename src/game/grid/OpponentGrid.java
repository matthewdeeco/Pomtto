package game.grid;

import java.awt.Graphics;

import game.ImageFactory;
import game.pom.Dipom;
import connection.Connection;

public class OpponentGrid extends PlayerGrid {

	public OpponentGrid(Connection conn, GameGridObserver observer) {
		super(conn, observer);
		bgImage = ImageFactory.createImage("map/blue_map.png");
		x = width + 1;
		y = 0;
		spawnDipom();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		System.out.println("X: " + x + " Y: " + y);
		System.out.println("getX: " + getX() + " getY: " + getY());
	}

}
