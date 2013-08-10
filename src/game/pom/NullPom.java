package game.pom;

import java.awt.*;

class NullPom extends Pom {

	public NullPom() {}

	@Override
	public boolean matchesColorOf(Pom pom) {
		return false;
	}
	
	@Override
	public boolean isShiningPom() {
		return false;
	}
	
	@Override
	public boolean isNull() {
		return true;
	}
	
	@Override
	public void paintIcon(Component c, Graphics g) {
	}
	
	@Override
	public String getColor() {
		return "NULL";
	}
	
}
