package jbyco.analyze.patterns.graph;

import java.util.Collection;
import java.util.LinkedList;

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
		this.item = item;
		
		counter = 0;
		in = null;
		out = new LinkedList<>();
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
			if (next.item == item) {
				return next;
			}
		}
		
		return null;
	}

	public void addEdge(Node node) {
		
		// add input and output nodes
		this.addOutputNode(node);
		node.addInputNode(this);
	}
	
	private void addInputNode(Node node) {
		in = node;
	}
	
	private void addOutputNode(Node node) {
		out.add(node);		
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
