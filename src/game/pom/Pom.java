package game.pom;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;

public class Pom implements Serializable {
	private enum State {NORMAL, BURSTING, DROPPING};
	public static final int WIDTH = 24;
	public static final int HEIGHT = 22;
	public static final Pom NULL_POM = new NullPom();
	
	private PomSprite sprite;
	private float x, y, yBeforeMove;
	private State state;
	private float distanceToMove;
	private float alpha;
	
	Pom() {}
	
	public Pom(PomSprite sprite) {
		this.sprite = sprite;
		state = State.NORMAL;
		alpha = 1.0f;
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
	
	public int getYBeforeMove() {
		return (int)this.yBeforeMove;
	}
	
	public boolean matchesColorOf(Pom pom) {
		return this.sprite.equals(pom.sprite);
	}
	
	public boolean isShiningPom() {
		return this.sprite == PomSprite.SHINING;
	}
	
	public void normalState() {
		state = State.NORMAL;
	}
	
	public void burstingState() {
		state = State.BURSTING;
	}
	
	public void increaseMoveBy(float dy) {
		state = State.DROPPING;
		yBeforeMove = y;
		distanceToMove += dy;
	}
	
	public boolean tryToMoveBy(float dy) {
		if (Math.abs(distanceToMove) < 1.0) {
			distanceToMove = 0;
			return false;
		}
		if (distanceToMove < 0)
			dy *= -1;
		distanceToMove -= dy;
		y += dy;
		return true;
	}
	
	public float getDroppingDistance() {
		return distanceToMove;
	}
	
	public boolean tryToFade() {
		if (alpha < 0.2)
			return false;
		alpha -= 0.2;
		return true;
	}
	
	public boolean isNull() {
		return false;
	}
	
	public boolean isBursting() {
		return state == State.BURSTING;
	}
	
	public boolean isDropping() {
		return state == State.DROPPING;
	}
	
	public boolean hasFaded() {
		return alpha == 0.0;
	}
	
	public void paint(Component c, Graphics g) {
		if (isBursting())
			sprite.paintBurstIcon(c, g, getX(), getY(), alpha);
		else
			sprite.paintNormalIcon(c, g, getX(), getY());
	}
	
	public String getColor() {
		return sprite.toString();
	}
	
	@Override
	public String toString() {
		return getColor();
	}
}