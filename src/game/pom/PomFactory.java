package game.pom;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PomFactory {
	private static final List<PomSprite> SPRITES = Arrays.asList(PomSprite.values());
	private static final int SIZE = SPRITES.size() - 1; // exclude Shining Pom
	private static final Random RANDOM = new Random();
	private static final int SHINING_POM_CHANCE = 2; // percentage
	
	public static Pom createPom(float x, float y) {
		return new Pom(randomColor(), x, y);
	}
	
	private static PomSprite randomColor() {
		if (RANDOM.nextInt(100) < SHINING_POM_CHANCE)
			return PomSprite.SHINING;
		else
			return SPRITES.get(RANDOM.nextInt(SIZE));
	}
}
