package jbyco.pattern.graph;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SuffixNode {
	
	// number
	static int maxNumber = 0;
	int number;
	
	// item
	Object item;
	
	// set of paths
	Set<Integer> paths;
	
	// list of edges, used when we create the graph
	Map<Object, SuffixNode> edges;
	
	// list of input nodes
	Set<SuffixNode> in;
	
	// set of output nodes
	Set<SuffixNode> out;

	public SuffixNode(Object item) {
		
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
	
	public Set<SuffixNode> getInputNodes() {
		return in;
	}

	public Set<SuffixNode> getOutputNodes() {
		return out;
	}
	
	public Set<Integer> getPaths() {
		return paths;
	}
	
	public Set<Integer> getEdgePaths(SuffixNode node) {
		
		// get paths of node where the edge goes
		Set<Integer> epaths = new HashSet<>(node.getPaths());
		
		// remove all paths that come from other nodes
		/*
		for(SuffixNode prev:node.getInputNodes()) {
			if (prev != this) {
				epaths.removeAll(prev.paths);
			}
		}
		*/
		
		// find intersection of paths
		epaths.retainAll(this.paths);
		
		return epaths;
	}
	
	public SuffixNode findNext(Object item) {
		return edges.get(item);
	}

	public void addEdge(SuffixNode node) {
		
		// add edge
		edges.put(node.getItem(), node);
		
		// add input and output nodes
		this.addOutputNode(node);
		node.addInputNode(this);
	}
	
	private void addInputNode(SuffixNode node) {
		in.add(node);
	}
	
	private void addOutputNode(SuffixNode node) {
		out.add(node);		
	}	
	
	public void addPath(int path) {
		paths.add(path);
	}
	
	public void setItem(Object item) {
		// this operation destroyes edges!!
		this.item = item;
	}
	
	public void removeEdge(SuffixNode node) {
		
		// remove edge
		edges.remove(node.getItem());
		
		// remove input and output nodes
		this.removeOutputNode(node);
		node.removeInputNode(this);
		
	}
	
	private void removeInputNode(SuffixNode node) {
		in.remove(node);
	}
	
	private void removeOutputNode(SuffixNode node) {
		out.remove(node);
	}

	@Override
	public String toString() {
		return "(" + number + (item == null ? "" : "," + item.toString()) + ")";
	}

	
}
