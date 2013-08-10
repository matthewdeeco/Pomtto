package game;

import org.newdawn.slick.*;

public class AudioHandler {
	private static Sound menuTrack;
	private static Sound playTrack;
	private static Sound burstEffect;
	
	static {
		try {
			menuTrack = new Sound("res/audio/Shall We Fight.ogg");
			playTrack = new Sound("res/audio/Miss You.ogg");
			burstEffect = new Sound("res/audio/Burst.wav");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public static void playMenuTrack() {
		if (!menuTrack.playing()) {
			// menuTrack.loop();
			playTrack.stop();
		}
	}
	
	public static void playMainGameTrack() {
		if (!playTrack.playing()) {
			// playTrack.loop();
			menuTrack.stop();			
		}
	}
	
	public static void playBurstEffect() {
		burstEffect.play();
	}
}