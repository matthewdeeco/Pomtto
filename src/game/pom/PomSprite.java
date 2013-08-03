package game.pom;

import game.ImageFactory;

import java.awt.Image;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;

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
		burst = createBurstImage(color, 0.9f);
	}
	
	private ImageIcon createNormalImage(String color) {
		return ImageFactory.createImage("pom/" + color + "1.png");
	}
	
	private ImageIcon createBurstImage(String color, float alpha) {
		return ImageFactory.createImage("pom/" + color + "2.png");
	}
	
	public ImageIcon getNormalImage() {
		return normal;
	}
	
	public ImageIcon getBurstImage() {
		return burst;
	}
	
	public void resetImage() {
		for (PomSprite ps: PomSprite.values())
			if (this.color == ps.color) {
				this.normal = ps.normal;
				this.burst = ps.burst;
			}
	}
}