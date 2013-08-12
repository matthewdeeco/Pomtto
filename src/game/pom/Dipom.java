package game.pom;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;

/** Represents a 2-pom set controlled by the player. */
public class Dipom implements Serializable {
	private Pom[] poms;
	
	public Dipom() {
		poms = new Pom[2];
		
		poms[0] = PomFactory.createRandomPom();
		poms[1] = PomFactory.createRandomPom();
	}
	
	/** Swaps the place of the two poms. */
	public void swap() {
		poms[0].translate(0, Pom.HEIGHT);
		poms[1].translate(0, -1 * Pom.HEIGHT);
		
		Pom temp = poms[0];
		poms[0] = poms[1];
		poms[1] = temp;
	}
	
	public void translate(int dx, int dy) {
		poms[0].translate(dx, dy);
		poms[1].translate(dx, dy);
	}
	
	public Pom getTopPom() {
		return poms[0];
	}
	
	public Pom getBottomPom() {
		return poms[1];
	}
	
	public int getX() {
		return poms[0].getX();
	}
	
	public int getY() {
		return poms[0].getY();
	}
	
	public void setX(int x) {
		poms[0].setX(x);
		poms[1].setX(x);
	}
	
	public void setY(int y) {
		poms[0].setY(y);
		poms[1].setY(y + Pom.HEIGHT);
	}
	
	public String toString() {
		return poms[0].toString() + "," + poms[1].toString();
	}
	
	public void paint(Component c, Graphics g) {
		poms[0].paint(c, g);
		poms[1].paint(c, g);
	}
}