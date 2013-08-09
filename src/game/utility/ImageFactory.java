package game.utility;

import java.awt.Image;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageFactory {
	private ImageFactory() {}
	
	public static ImageIcon createImage(String filepath) {
		try {
			Image image = ImageIO.read(new File("res/" + filepath));
			return new ImageIcon(image);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
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
