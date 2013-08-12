package game.pom;

import java.awt.*;

class NullPom extends Pom {

	public NullPom() {}

	@Override
	public boolean matchesColorOf(Pom pom) {
		if (pom.isNull())
			return true;
		else
			return false;
	}
	
	@Override
	public boolean isShiningPom() {
		return false;
	}

	@Override
	public boolean tryToDropBy(float dy) {
		return false;
	}
	
	@Override
	public boolean isNull() {
		return true;
	}
	
	@Override
	public boolean isBursting() {
		return false;
	}
	
	@Override
	public boolean isDropping() {
		return false;
	}
	
	@Override
	public void paint(Component c, Graphics g) {
	}
	
	@Override
	public String getColor() {
		return "NULL";
	}
	
}
