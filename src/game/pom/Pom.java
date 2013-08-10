package game.pom;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;

public class Pom implements Serializable {
	public static final int WIDTH = 24;
	public static final int HEIGHT = 22;
	public static final Pom NULL_POM = new NullPom();
	
	private PomSprite sprite;
	private float x, y;
	private boolean isBursting;
	
	Pom() {}
	
	public Pom(PomSprite sprite, float x, float y) {
		this.sprite = sprite;
		this.x = x;
		this.y = y;
	}
	
	public void translate(int dx, int dy) {
		x += dx;
		y += dy;
	}
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean matchesColorOf(Pom pom) {
		return this.sprite.equals(pom.sprite);
	}
	
	public boolean isShiningPom() {
		return this.sprite == PomSprite.SHINING;
	}
	
	public void burst() {
		isBursting = true;
	}
	
	public boolean isNull() {
		return false;
	}
	
	public boolean isBursting() {
		return isBursting;
	}
	
	public void paintIcon(Component c, Graphics g) {
		if (isBursting)
			sprite.getBurstImage().paintIcon(c, g, getX(), getY());
		else
			sprite.getNormalImage().paintIcon(c, g, getX(), getY());
	}
	
	public String getColor() {
		return sprite.toString();
	}
	
	@Override
	public String toString() {
		return getColor();
	}
}