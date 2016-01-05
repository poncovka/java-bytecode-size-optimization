package jbyco.pattern.graph;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SuffixNode {
	
	// identifier
	static int maxid = 0;
	int id;
	
	// item
	Object item;
	
	// set of paths
	Map<SuffixNode, Set<Path>> paths;
	
	// list of edges, used when we create the graph
	Map<Object, SuffixNode> edges;
	
	// list of input nodes
	Set<SuffixNode> in;
	
	// set of output nodes
	Set<SuffixNode> out;
	
	// depth of node
	int depth;

	public SuffixNode(Object item) {
		
		this.id = maxid++;
		this.item = item;
		this.depth = -1;
		
		paths = new LinkedHashMap<>();
		in = new LinkedHashSet<>();
		out = new LinkedHashSet<>();
		edges = new LinkedHashMap<>();
	}

	public int getNumber() {
		return id;
	}
	
	public Object getItem() {
		return item;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public Set<SuffixNode> getInputNodes() {
		return in;
	}

	public Set<SuffixNode> getOutputNodes() {
		return out;
	}
	
	public Map<SuffixNode, Set<Path>> getPaths() {
		return paths;
	}
	
	public Set<Path> getNodePaths() {
		
		Set<Path> nodePaths = new LinkedHashSet<>();
		
		// join all input edge paths
		for(SuffixNode node:in) {
			nodePaths.addAll(node.getEdgePaths(this));
		}

		return nodePaths;
	}
	public Set<Path> getEdgePaths(SuffixNode node) {
		return paths.getOrDefault(node, new LinkedHashSet<>());
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
	
	public void copyEdges(SuffixNode node) {
		
		// copy edges
		for(SuffixNode next:node.getOutputNodes()) {
			this.addEdge(next);
			
			// copy paths
			for(Path path:node.getEdgePaths(next)) {
				this.addPath(next, path);
			}
		}
	}
	
	private void addInputNode(SuffixNode node) {
		in.add(node);
	}
	
	private void addOutputNode(SuffixNode node) {
		out.add(node);		
	}	
	
	public void addPath(SuffixNode node, Path path) {
		
		// get set of paths on the edge to node
		Set<Path> set = paths.get(node);
		
		// or create new set
		if(set == null) {
			set = new LinkedHashSet<>();
			paths.put(node, set);
		}
		
		// add path to set
		set.add(path);
	}

	public void addPaths(SuffixNode node, Set<Path> paths) {
		
		for(Path path:paths) {		
			addPath(node, path);
		}
	}
	
	public void addPaths(Map<SuffixNode,Set<Path>> paths2) {
		
		// for every edge
		for(SuffixNode node:paths2.keySet()) {
			
			// merge set of paths
			addPaths(node, paths2.get(node));
		}
	}
	
	
	public void setItem(Object item) {
		// this operation destroys edges!!
		this.item = item;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public void removeEdge(SuffixNode node) {
		
		// remove edge
		edges.remove(node.getItem());
		
		// remove input and output nodes
		this.removeOutputNode(node);
		node.removeInputNode(this);
		
		// remove paths
		this.paths.remove(node);
	}
	
	private void removeInputNode(SuffixNode node) {
		in.remove(node);
	}
	
	private void removeOutputNode(SuffixNode node) {
		out.remove(node);
	}
	
	@Override
	public String toString() {
		return "(" + id + (item == null ? "" : "," + item.toString()) + ")";
	}

	
}
