package game.audio;

import kuusisto.tinysound.TinySound;

public class AudioHandler {
	private static BGM menuTrack;
	private static BGM playTrack;
	private static SoundEffect burstEffect;
	private static SoundEffect clickEffect;
	private static SoundEffect comboEffects[];
	private static final int NUM_COMBO_EFFECTS = 4;
	private static boolean mute = false;
	
	public static void initialize() {
		TinySound.init();
		menuTrack = new BGM("Shall We Fight");
		playTrack = new BGM("Miss You");
		burstEffect = new SoundEffect("Burst");
		clickEffect = new SoundEffect("Click");
		
		comboEffects = new SoundEffect[NUM_COMBO_EFFECTS];
		for (int i = 0; i < 4; i++)
			comboEffects[i] = new SoundEffect("combo/" + String.valueOf(i + 1));
	}
	
	public static void shutdown() {
		TinySound.shutdown();
	}
	
	public static void playMenuTrack() {
		if (!mute) {
			menuTrack.loop();
			playTrack.stop();
		}
	}
	
	public static void playMainGameTrack() {
		if (!mute) {
			playTrack.loop();
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
	
	public static void toggleMute() {
		mute = !mute;
		menuTrack.stop();
		playTrack.stop();
	}
}