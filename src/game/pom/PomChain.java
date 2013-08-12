package game.pom;

import java.awt.Point;
import java.util.*;

public class PomChain {
	private static final int MIN_CHAIN_LENGTH = 3;
	private static final int NORMAL_POM_SCORE = 3;
	private static final int SHINING_POM_SCORE = 100;

	private Set<Point> chain; // coords of the poms in the chain
	private int score;
	
	private List<Point> visitedCoords; // coords visited
	private Queue<Point> q;
	private PomGrid poms;
	
	public PomChain(PomGrid grid, int row, int col) {
		chain = new HashSet<Point>();
		
		Pom startingPom = grid.get(row, col);
		if (!startingPom.isShiningPom() && !startingPom.isNull()) {
			score = 3; // at least 1 pom is in the chain
			
			visitedCoords = new ArrayList<Point>();
			q = new LinkedList<Point>();
			this.poms = grid;
			
			Point startingCoord = new Point(row, col);
			q.add(startingCoord);
			chain.add(startingCoord);
			createChain();
		}
	}
	
	private void createChain() {
		int shiningPomCount = 0;
		while (!q.isEmpty()) {
			Point p = q.remove();
			visitedCoords.add(p);
			Pom currentPom = poms.get(p.x, p.y);
			Pom adjacentPom;
			for (int i = -1; i <= 1; i++)
				for (int j = -1; j <= 1; j++)
					if (Math.abs(i + j) == 1) { // only 1 step away from the middle
						int row = p.x + i;
						int col = p.y + j;
						boolean withinXBound = (row > 0) && (row < poms.getNumRows());
						boolean withinYBound = (col >= 0) && (col < poms.getNumCols() - 1);
						Point adjPomCoords = new Point(row, col);
						boolean notYetVisited = !visitedCoords.contains(adjPomCoords);
						if (withinXBound && withinYBound && notYetVisited) {
							adjacentPom = poms.get(row, col);
							if (currentPom.matchesColorOf(adjacentPom)) {
								chain.add(adjPomCoords);
								q.add(adjPomCoords); 
							}
							else if (adjacentPom.isShiningPom()) {
								chain.add(adjPomCoords);
								q.add(adjPomCoords);
								shiningPomCount++;
							}
						}
					}
		}
		if (chain.size() - shiningPomCount < MIN_CHAIN_LENGTH) { // did not meet min to make a chain
			chain.clear();
			score = 0;
		} else if (shiningPomCount > 0)
			score = (chain.size() - shiningPomCount) * SHINING_POM_SCORE;
		else
			score = chain.size() * NORMAL_POM_SCORE;
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