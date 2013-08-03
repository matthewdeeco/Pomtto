package game;

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
}
