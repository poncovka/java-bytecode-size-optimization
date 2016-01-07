package jbyco.pattern.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class SuffixNode implements Comparable<SuffixNode> {
	
	// default set
	static final Set<Path> EMPTYSET = new LinkedHashSet<>(0);
	
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
		
		paths = new HashMap<>();
		in = new TreeSet<>();
		out = new TreeSet<>();
		edges = new HashMap<>();
	}

	static int getCount() {
		return maxid;
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
	
	public Collection<SuffixNode> getInputNodes() {
		return in;
	}

	public Collection<SuffixNode> getOutputNodes() {
		return out;
	}
	
	public Map<SuffixNode, Set<Path>> getPaths() {
		return paths;
	}
	
	public Set<Path> getPaths(SuffixNode node) {
		
		// get set of paths on the edge to node
		Set<Path> set = paths.get(node);
				
		// or create new set
		if(set == null) {
			set = new TreeSet<>();
			paths.put(node, set);
		}
		
		return set;
	}
	
	public boolean doSharePath(SuffixNode node) {
		
		for(SuffixNode prev : in) {
			for (Path p: prev.getEdgePaths(this)) {
				
				for (SuffixNode prev2 : node.in) {
					for (Path p2 : prev2.getEdgePaths(node)) {
						
						if (p == p2) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public Set<Path> getEdgePaths(SuffixNode node) {
		return paths.getOrDefault(node, EMPTYSET);
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
		// add path to set
		getPaths(node).add(path);
	}

	public void addPaths(SuffixNode node, Set<Path> paths) {
		
		Set<Path> set = getPaths(node);
		
		for(Path path:paths) {		
			set.add(path);
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

	@Override
	public int compareTo(SuffixNode node) {
		return Integer.compare(id, node.id);
	}

	
}
