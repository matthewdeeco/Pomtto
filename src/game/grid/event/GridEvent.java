package game.grid.event;

import java.io.Serializable;

// Only an abstract class instead of interface to make it Serializable
public abstract class GridEvent implements Serializable {
	public abstract void invoke(GridEventListener listener);
}
