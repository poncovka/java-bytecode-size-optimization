package jbyco.analyze.patterns.tree;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Node<T> {
	
	// identifier
	static int maxid = 0;
	int id;
	
	// item
	T item;
	
	// how many sequences are represented?
	int counter;
	
	// the input node
	Node<T> in;
	
	// set of output nodes
	Map<T, Node<T>> out;

	public Node(T item) {
		
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
	
	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}

	public void addInputNode(Node<T> node) {
		in = node;
	}
	
	public Node<T> getInputNode() {
		return in;
	}

	public void removeInputNode(Node<T> node) {
		in = null;
	}

	public void addOutputNode(Node<T> node) {
		
		// init the output nodes map
		if (out == null) {
			out =  new HashMap<>(1, 16.0f);
		}
		
		out.put(node.getItem(), node);		
	}
	
	public Collection<Node<T>> getOutputNodes() {
		if (out == null) 	return Collections.emptyList();
		else				return out.values();
	}
	
	public Node<T> findNextNode(T item) {
		if (out == null) 	return null;
		else				return out.get(item);
	}
	
	public void removeOutputNode(Node<T> node) {
		
		if (out == null) {
			return;
		}
		
		out.remove(node.getItem());
		
		// remove the output nodes map
		if (out.isEmpty()) {
			out = null;
		}
	}
	
	public void removeOutputNode(Iterator<Node<T>> i) {
		
		// remove the current node
		i.remove();
		
		// remove the output nodes map
		if (out.isEmpty()) {
			out = null;
		}
	}

	public void addEdge(Node<T> node) {
		this.addOutputNode(node);
		node.addInputNode(this);
	}
	
	public void removeEdge(Node<T> node) {
		this.removeOutputNode(node);
		node.removeInputNode(this);
	}
		
	@Override
	public String toString() {
		return "(" + id + (item == null ? "" : "," + item.toString()) + ")";
	}
	
}
