package game.pom;

import game.grid.*;

import java.awt.Point;
import java.util.*;

public class PomChain {
	private static final int MIN_CHAIN_LENGTH = 3;
	private static final int NORMAL_POM_SCORE = 1;
	private static final int SHINING_POM_SCORE = 100;

	private Set<Point> chain; // coords of the poms in the chain
	private int score;
	
	private List<Point> visitedCoords; // coords visited
	private Queue<Point> q;
	private GameGrid grid;
	private int shiningPomCount;
	
	public PomChain(GameGrid grid, int row, int col) {
		chain = new HashSet<Point>();
		
		Pom startingPom = grid.getPomAt(row, col);
		if (!startingPom.isShiningPom() && !startingPom.isNull()) {
			score = 3; // at least 1 pom is in the chain
			
			visitedCoords = new ArrayList<Point>();
			q = new LinkedList<Point>();
			this.grid = grid;
			shiningPomCount = 0;
			
			Point startingCoord = new Point(row, col);
			q.add(startingCoord);
			chain.add(startingCoord);
			createChain();
		}
	}
	
	private void createChain() {
		while (!q.isEmpty()) {
			Point p = q.remove();
			visitedCoords.add(p);
			Pom currentPom = grid.getPomAt(p.x, p.y);
			Pom adjacentPom;
			for (int i = -1; i <= 1; i++)
				for (int j = -1; j <= 1; j++)
					if (Math.abs(i + j) == 1) { // only 1 step away from the middle
						int row = p.x + i;
						int col = p.y + j;
						boolean withinXBound = (row >= 0) && (row < grid.getWidth());
						boolean withinYBound = (col >= 0) && (col < grid.getHeight());
						Point adjPomCoords = new Point(row, col);
						boolean notYetVisited = !visitedCoords.contains(adjPomCoords);
						if (withinXBound && withinYBound && notYetVisited) {
							adjacentPom = grid.getPomAt(row, col);
							if (adjacentPom == null)
								continue;
							else if (currentPom.matchesColorOf(adjacentPom)) {
								chain.add(adjPomCoords);
								q.add(adjPomCoords);
								score += NORMAL_POM_SCORE; 
							}
							else if (adjacentPom.isShiningPom()) {
								chain.add(adjPomCoords);
								score += SHINING_POM_SCORE;
								shiningPomCount++;
							}
						}
					}
		}
		if (chain.size() - shiningPomCount < MIN_CHAIN_LENGTH) { // did not meet min to make a chain
			chain.clear();
			score = 0;
		}
	}
	
	public boolean isEmpty() {
		return chain.isEmpty();
	}
	
	public Set<Point> getChainedCoords() {
		return chain;
	}
	
	public int getScore() {
		return score;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chain == null) ? 0 : chain.hashCode());
		return result;
	}

	/** PomChains are equal if they generate the same chainedCoords. */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PomChain other = (PomChain) obj;
		if (chain == null) {
			if (other.chain != null)
				return false;
		} else if (!chain.equals(other.chain))
			return false;
		return true;
	}
}