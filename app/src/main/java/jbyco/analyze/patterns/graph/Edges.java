package jbyco.analyze.patterns.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import jbyco.analyze.patterns.instructions.CachedInstruction;

public class Edges extends ArrayList<Node> {

	// generated UID
	private static final long serialVersionUID = -1280596779016321495L;
	
	public Edges() {
		super(0);
	}
	
	public Collection<Node> getNodes() {
		return this;
	}

	public void addNode(Node node) {
		
		// get number from -1 to size()
		int index = binarySearch(node.getItem(), true) + 1;

		if (!checkBounds(index)) {
			add(node);
		}
		else {
			add(index, node);
		}
	}
	
	protected int compareItems(Object i1, Object i2) {
		return ((CachedInstruction)i1).compareTo((CachedInstruction)i2);
		//return ((Integer)i1).compareTo((Integer)i2);
	}

	public void removeNode(Node node) {
		remove(getNode(node.getItem()));
	}
	
	public Node getNode(Object item) {
		int index = binarySearch(item, false);	
		return checkBounds(index) ? get(index) : null;
	}
	
	protected boolean checkBounds(int index) {
		return !(index < 0 || index >= size());
	}
	
	protected int binarySearch(Object item, boolean insertion) {
		
		int min = 0;
		int max = size() - 1;
		int index = -1;
		int cmp;
		
		while (min <= max) {
			
			index = ((max - min) / 2) + min;
			cmp = compareItems(item, get(index).getItem());
			
			// found same element
			if (cmp == 0) {
				return index;
			}
			// correct max
			else if (cmp < 0) {
				index -= 1;
				max = index;
			}
			// correct min
			else {
				index += 1;
				min = index;
			}
		}
		
		return (insertion) ? index : -1;
	}
	
	public static void main(String[] args) {
		
		Edges out = new Edges();
		out.addNode(new Node(-4));
		out.addNode(new Node(2));
		out.addNode(new Node(-1));
		
		out.addNode(new Node(3));
		out.addNode(new Node(4));
		
		out.addNode(new Node(0));
		out.addNode(new Node(3));
		
		out.addNode(new Node(-3));
		
		System.out.println(out.getNode(new Integer(-4)));
		
		for (Node n : out) {
			System.out.printf("Node %d, item %s\n", n.getId(), n.getItem().toString());
			
		}
		
	}
	
}
