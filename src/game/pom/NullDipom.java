package game.pom;

import java.awt.Component;
import java.awt.Graphics;

public class NullDipom extends Dipom {

	public NullDipom() {
		super(0, 0);
	}
	
	public void swap() {
	}
	
	public void translate(int dx, int dy) {
	}
	
	public Pom getTopPom() {
		return Pom.NULL_POM;
	}
	
	public Pom getBottomPom() {
		return Pom.NULL_POM;
	}
	
	@Override
	public void paintIcon(Component c, Graphics g) {
	}
	
	@Override
	public String toString() {
		return "NULL";
	}
}
