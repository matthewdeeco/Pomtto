package game.pom;

import game.utility.ImageFactory;
import darrylbu.icon.AlphaImageIcon;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.ImageIcon;

public enum PomSprite implements Serializable {
	BLUE("blue"),
	RED("red"),
	GREEN("green"),
	YELLOW("yellow"),
	SHINING("shining");
	
	private transient ImageIcon normal, burst;
	
	PomSprite(String color) {
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
}