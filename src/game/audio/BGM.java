package game.audio;

import java.net.URL;
import kuusisto.tinysound.*;

public class BGM {
	private static final String FILE_EXTENSION = "ogg";
	private String title;
	private Music music;
	
	public BGM(String title) {
		this.title = title;
	}

	private static URL url(String title) {
		String path = String.format("res/audio/%s.%s", title, FILE_EXTENSION);
		return BGM.class.getClassLoader().getResource(path);
	}
	
	public void play() {
		music.play(false);
	}
	
	public void loop() {
		music = TinySound.loadMusic(url(title));
		music.play(true);
	}
	
	public void stop() {
		if (music != null)
			music.stop();
	}
}