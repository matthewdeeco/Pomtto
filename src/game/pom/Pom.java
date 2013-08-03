package game.pom;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class Pom implements Serializable {
	public static final int WIDTH = 24;
	public static final int HEIGHT = 22;
	public static final Pom NULL_POM = new NullPom();
	
	private static final float SPEED = 1.0f;
	
	private transient ImageIcon image;
	private PomSprite sprite;
	private float x, y;
	
	Pom() {}
	
	public Pom(PomSprite sprite, float x, float y) {
		this.sprite = sprite;
		image = sprite.getNormalImage();
		this.x = x;
		this.y = y;
	}
	
	public void moveLeft(float delta) {
		x -= delta;
	}
	
	public void moveRight(float delta) {
		x += delta;
	}
	
	public void moveUp(float delta) {
		y -= delta;
	}
	
	public void moveDown(float delta) {
		y += delta;
	}
	
	public void moveDown() {
		y += SPEED;
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
		image = sprite.getBurstImage();
	}
	
	public boolean isNull() {
		return this instanceof NullPom;
	}
	
	public void paintIcon(Component c, Graphics g) {
		image.paintIcon(c, g, getX(), getY());
	}
}