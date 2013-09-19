package game.audio;

import java.net.URL;
import kuusisto.tinysound.*;

public class SoundEffect {
	private static final String FILE_EXTENSION = "wav";
	private String title;
	private Sound sound;
	
	public SoundEffect(String title) {
		this.title = title;
	}
	
	private static URL url(String title) {
		String path = String.format("res/audio/%s.%s", title, FILE_EXTENSION);
		return SoundEffect.class.getClassLoader().getResource(path);
	}
	
	public void play() {
		sound = TinySound.loadSound(url(title));
		sound.play();
	}
}