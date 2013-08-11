package game.pom;

import game.utility.ImageFactory;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.ImageIcon;

import darrylbu.icon.AlphaImageIcon;

public enum PomSprite implements Serializable {
	BLUE("blue"),
	RED("red"),
	GREEN("green"),
	YELLOW("yellow"),
	SHINING("shining");
	
	private String color;
	private transient ImageIcon normal, burst;
	
	PomSprite(String color) {
		this.color = color;
		normal = createNormalImage(color);
		burst = createBurstImage(color);
	}
	
	private ImageIcon createNormalImage(String color) {
		return ImageFactory.createImage("pom/" + color + "1.png");
	}
	
	private ImageIcon createBurstImage(String color) {
		return ImageFactory.createImage("pom/" + color + "2.png");
	}
	
	public void paintNormalIcon(Component c, Graphics g, int x, int y) {
		normal.paintIcon(c, g, x, y);
	}
	
	public void paintBurstIcon(Component c, Graphics g, int x, int y, float alpha) {
		AlphaImageIcon image = (AlphaImageIcon)burst; 
		image.paintIcon(c, g, x, y, alpha);
	}
	
	public void resetImage() {
		for (PomSprite ps: PomSprite.values())
			if (this.color == ps.color) {
				this.normal = ps.normal;
				this.burst = ps.burst;
			}
	}
}