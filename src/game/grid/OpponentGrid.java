package game.grid;

import game.Connection;
import game.pom.Dipom;
import game.utility.ImageFactory;

public class OpponentGrid extends GameGrid {

	public OpponentGrid(Connection conn, int avatarIndex) {
		super(conn, avatarIndex);
		bgImage = ImageFactory.createImage("map/blue_map.png");
		borderImage = ImageFactory.createImage("map/blue_border.png");
	}

	@Override
	protected void createDipom() {
	}
	
	public void dipomCreated(Dipom dipom) {
		this.dipom = dipom;
	}

	@Override
	protected void dipomPlaced() {
		// chainPoms();
	}

	@Override
	protected boolean shouldAddMorePoms() {
		return false;
	}
}
