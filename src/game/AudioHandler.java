package game;

import java.net.URL;

import org.newdawn.slick.*;;

public class AudioHandler {
	private static Sound menuTrack;
	private static Sound playTrack;
	private static Sound[] playTracks;
	private static Sound burstEffect;
	private static Sound clickEffect;
	private static Sound comboEffects[];
	
	static {
		try {
			menuTrack = new Sound(path("Shall We Fight.ogg"));
			playTracks = new Sound[3];
			playTracks[0] = new Sound(path("Miss You.ogg"));
			playTracks[1] = new Sound(path("A New Daily Life.ogg"));
			playTracks[2] = new Sound(path("Fight! Cherry Blossom Viewing Dumpling Counterattack.ogg"));
			randomizeTrack();
			burstEffect = new Sound(path("Burst.wav"));
			clickEffect = new Sound(path("Click.wav"));
			comboEffects = new Sound[4];
			for (int i = 1; i <= comboEffects.length; i++)
				comboEffects[i - 1] = new Sound(path("combo/" + i + ".wav"));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	private static String path(String path) {
		return "res/audio/" + path;
	}
	
	private static void randomizeTrack() {
		playTrack = playTracks[(int)(Math.random() * 3)];
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
	
	public static void playClickEffect() {
		clickEffect.play();
	}
	
	public static void playComboEffect(int comboCount) {
		comboCount = Math.min(comboCount, comboEffects.length - 1);
		comboEffects[comboCount].play();
	}
}