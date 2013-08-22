package game.utility;

import java.awt.Image;
import java.io.*;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import darrylbu.icon.AlphaImageIcon;

public class ImageFactory {
	private ImageFactory() {}
	
	public static ImageIcon createImage(String filepath) {
		try {
			Image image = ImageIO.read(new File("res/" + filepath));
			return new AlphaImageIcon(new ImageIcon(image));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static URL url(String path) {
		return ImageFactory.class.getClassLoader().getResource("res/" + path);
	}
	
	public static ImageIcon createAvatarImage(String type, int avatarIndex) {
		try {
			Image image = ImageIO.read(new File(String.format("res/avatar/%s/%d.png", type, avatarIndex)));
			return new ImageIcon(image);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
