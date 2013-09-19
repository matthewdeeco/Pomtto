package game.utility;

import java.net.URL;
import javax.swing.ImageIcon;
import darrylbu.icon.AlphaImageIcon;

public class ImageFactory {
	private ImageFactory() {}
	
	public static ImageIcon createImage(String filepath) {
		ImageIcon icon = new ImageIcon(url(filepath));
		return new AlphaImageIcon(icon);
	}
	
	private static URL url(String path) {
		return ImageFactory.class.getClassLoader().getResource("res/" + path);
	}
	
	public static ImageIcon createAvatarImage(String type, int avatarIndex) {
		ImageIcon icon = new ImageIcon(url(String.format("avatar/%s/%d.png", type, avatarIndex)));
		return icon;
	}
}
