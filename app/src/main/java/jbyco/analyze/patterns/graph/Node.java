package jbyco.analyze.patterns.graph;

import java.util.ArrayList;
import java.util.Collection;

public class Node implements Comparable<Node> {
	
	// identifier
	static int maxid = 0;
	int id;
	
	// item
	Object item;
	
	// how many sequences are represented?
	int counter;
	
	// the input node
	Node in;
	
	// set of output nodes
	Collection<Node> out;

	public Node(Object item) {
		
		this.id = maxid++;
		
		if (id > maxid) {
			throw new ArithmeticException("Overflow of node's idetifier.");
		}
		
		this.item = item;
		
		counter = 0;
		in = null;
		out = new ArrayList<Node>(0);	
	}

	static int getTotal() {
		return maxid;
	}
	
	public int getId() {
		return id;
	}
	
	public Object getItem() {
		return item;
	}
	
	public Node getInputNode() {
		return in;
	}

	public Collection<Node> getOutputNodes() {
		return out;
	}
	
	public int getCount() {
		return counter;
	}
	
	public Node findNextNode(Object item) {
		
		for(Node next : out) {
			if (next.item.equals(item)) {
				return next;
			}
		}
		
		return null;
	}

	public void addEdge(Node node) {
		this.addOutputNode(node);
		node.addInputNode(this);
	}
	
	public void removeEdge(Node node) {
		this.removeOutputNode(node);
		node.removeInputNode(this);
	}
	
	private void addInputNode(Node node) {
		in = node;
	}
	
	private void addOutputNode(Node node) {
		out.add(node);		
	}

	private void removeInputNode(Node node) {
		in = null;
	}
	
	private void removeOutputNode(Node node) {
		out.remove(node);		
	}		
		
	public void incrementCount() {
		counter++;
	}	
	
	public void setItem(Object item) {
		this.item = item;
	}
	
	@Override
	public String toString() {
		return "(" + id + (item == null ? "" : "," + item.toString()) + ")";
	}

	@Override
	public int compareTo(Node node) {
		return Integer.compare(id, node.id);
	}
	
}
