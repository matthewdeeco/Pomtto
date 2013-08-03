package game.pom;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;

/** Represents a 2-pom set controlled by the player. */
public class Dipom implements Serializable {
	private static final float INITIAL_SPEED = 1.0f;
	private static final float SPEED_UP = 1.5f;

	private Pom[] poms;
	private float speed;
	private float normalSpeed;
	
	public Dipom(int x, int y) {
		poms = new Pom[2];
		speed = normalSpeed = INITIAL_SPEED;
		
		poms[0] = PomFactory.createPom(x, y);
		poms[1] = PomFactory.createPom(x, y + Pom.HEIGHT);
	}
	
	/** Swaps the place of the two poms. */
	public void swap() {
		poms[0].moveDown(Pom.HEIGHT);
		poms[1].moveUp(Pom.HEIGHT);
		
		Pom temp = poms[0];
		poms[0] = poms[1];
		poms[1] = temp;
	}
	
	public void moveLeft() { 
		poms[0].moveLeft(Pom.WIDTH);
		poms[1].moveLeft(Pom.WIDTH);
	}
	
	public void moveLeft(int x) {
		poms[0].moveLeft(x);
		poms[1].moveLeft(x);
	}
	
	public void moveRight() { 
		poms[0].moveRight(Pom.WIDTH);
		poms[1].moveRight(Pom.WIDTH);
	}
	
	public void moveRight(int x) { 
		poms[0].moveRight(x);
		poms[1].moveRight(x);
	}
	
	public void moveDown() {
		poms[0].moveDown(speed);
		poms[1].moveDown(speed);
	}

	public void speedUp() {
		speed += SPEED_UP;
	}
	
	public void resetSpeed() {
		speed = normalSpeed;
	}
	
	public Pom getTopPom() {
		return poms[0];
	}
	
	public Pom getBottomPom() {
		return poms[1];
	}
	
	public void paintIcon(Component c, Graphics g) {
		poms[0].paintIcon(c, g);
		poms[1].paintIcon(c, g);
	}
}