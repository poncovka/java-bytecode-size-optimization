package jbyco.pattern.graph;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class PrefixNode {
	
	// number
	static int maxNumber = 0;
	int number;
	
	// item
	Object item;
	
	// set of paths
	Set<Integer> paths;
	
	// list of edges, used when we create the graph
	Map<Object, PrefixNode> edges;
	
	// list of input nodes
	Set<PrefixNode> in;
	
	// set of output nodes
	Set<PrefixNode> out;

	public PrefixNode(Object item) {
		
		this.number = maxNumber++;
		this.item = item;
		
		paths = new HashSet<>();
		in = new LinkedHashSet<>();
		out = new LinkedHashSet<>();
		edges = new LinkedHashMap<>();
	}

	
	public int getNumber() {
		return number;
	}
	
	public Object getItem() {
		return item;
	}
	
	public Set<PrefixNode> getInputNodes() {
		return in;
	}

	public Set<PrefixNode> getOutputNodes() {
		return out;
	}
	
	public Set<Integer> getPaths() {
		return paths;
	}
	
	public void setItem(Object item) {
		this.item = item;
	}
	
	public void addEdge(PrefixNode node) {
		
		// add edge
		edges.put(node.getItem(), node);
		
		// add input and output nodes
		this.addOutputNode(node);
		node.addInputNode(this);
	}
	
	public void addInputNode(PrefixNode node) {
		in.add(node);
	}
	
	public void addOutputNode(PrefixNode node) {
		out.add(node);		
	}
	
	public void addPath(int path) {
		paths.add(path);
	}
	
	public void removeInputNode(PrefixNode node) {
		in.remove(node);
	}

	public void removeOutputNode(PrefixNode node) {
		out.remove(node);
	}
	
	
	public PrefixNode findNext(Object item) {
		return edges.get(item);
	}


	@Override
	public String toString() {
		return "(" + number + (item == null ? "" : "," + item.toString()) + ")";
	}

	
}
