package game.pom;

import java.awt.Component;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Queue;

/** Used to get a preview of the dipoms to come. */
public class DipomQueue {
	private Queue<Dipom> q;
	
	public DipomQueue() {
		q = new LinkedList<Dipom>();
	}
	
	public void enqueue(Dipom dipom) {
		q.add(dipom);
		dipom.setY(q.size() * 2 * Pom.HEIGHT);
	}
	
	public Dipom dequeue() {
		Dipom dipom = q.remove();
		for (Dipom d: q) {
			d.setY(d.getY() - Pom.HEIGHT * 2);
		}
		return dipom;
	}
	
	public void paint(Component c, Graphics g) {
		for (Dipom dipom: q)
			dipom.paint(c, g);
	}
}
