package game.pom;

import java.awt.*;

class NullPom extends Pom {

	public NullPom() {}

	public boolean matchesColorOf(Pom pom) {
		return false;
	}
	
	public boolean isShiningPom() {
		return false;
	}
	
	public void paintIcon(Component c, Graphics g) {
	}
	
}
