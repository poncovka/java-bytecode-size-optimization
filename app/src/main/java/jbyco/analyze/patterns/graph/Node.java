package jbyco.analyze.patterns.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Node {
	
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
	Map<Object, Node> out;

	public Node(Object item) {
		
		this.id = maxid++;
		this.item = item;
		
		if (this.id > maxid) {
			throw new ArithmeticException("Integer overflow.");
		}
		
		this.counter = 0;
		this.in = null;
		this.out = null;	
	}

	static int getTotal() {
		return maxid;
	}
	
	public int getId() {
		return id;
	}
	
	public int getCount() {
		return counter;
	}
	
	public void incrementCount() {
		counter++;
	}
	
	public Object getItem() {
		return item;
	}

	public void setItem(Object item) {
		this.item = item;
	}

	private void addInputNode(Node node) {
		in = node;
	}
	
	public Node getInputNode() {
		return in;
	}

	private void removeInputNode(Node node) {
		in = null;
	}

	private void addOutputNode(Node node) {
		
		// init the output nodes map
		if (out == null) {
			out =  new HashMap<>(1, 16.0f);
		}
		
		out.put(node.getItem(), node);		
	}
	
	public Collection<Node> getOutputNodes() {
		if (out == null) 	return Collections.emptyList();
		else				return out.values();
	}
	
	public Node findNextNode(Object item) {
		if (out == null) 	return null;
		else				return out.get(item);
	}
	
	private void removeOutputNode(Node node) {
		
		if (out == null) {
			return;
		}
		
		out.remove(node.getItem());
		
		// remove the output nodes map
		if (out.isEmpty()) {
			out = null;
		}
	}

	public void addEdge(Node node) {
		this.addOutputNode(node);
		node.addInputNode(this);
	}
	
	public void removeEdge(Node node) {
		this.removeOutputNode(node);
		node.removeInputNode(this);
	}
		
	@Override
	public String toString() {
		return "(" + id + (item == null ? "" : "," + item.toString()) + ")";
	}
	
}
