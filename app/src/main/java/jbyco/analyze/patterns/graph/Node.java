package jbyco.analyze.patterns.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Node implements Comparable<Node> {
	
	// default set
	static final Set<Path> EMPTYSET = new LinkedHashSet<>(0);
	
	// identifier
	static int maxid = 0;
	int id;
	
	// item
	Object item;
	
	// sets of paths on edges to output nodes
	Map<Node, Set<Path>> paths;
	
	// list of input nodes
	Collection<Node> in;
	
	// set of output nodes
	Collection<Node> out;
	
	// depth of node
	int depth;

	public Node(Object item) {
		
		this.id = maxid++;
		this.item = item;
		this.depth = -1;
		
		paths = new HashMap<>();
		in = new LinkedList<>();
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
	
	public int getDepth() {
		return depth;
	}
	
	public Collection<Node> getInputNodes() {
		return in;
	}

	public Collection<Node> getOutputNodes() {
		return out;
	}
	
	public Map<Node, Set<Path>> getPaths() {
		return paths;
	}
	
	public Set<Path> getPaths(Node node) {
		
		// get set of paths on the edge to node
		Set<Path> set = paths.get(node);
				
		// or create new set
		if(set == null) {
			set = new TreeSet<>();
			paths.put(node, set);
		}
		
		return set;
	}
	
	public boolean doSharePath(Node node) {
		
		for(Node prev : in) {
			for (Path p: prev.getEdgePaths(this)) {
				
				for (Node prev2 : node.in) {
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
	
	public Set<Path> getEdgePaths(Node node) {
		return paths.getOrDefault(node, EMPTYSET);
	}
	
	public Node findNext(Object item) {
		
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
	
	public void copyEdges(Node node) {
		
		// copy edges
		for(Node next:node.getOutputNodes()) {
			this.addEdge(next);
			
			// copy paths
			for(Path path:node.getEdgePaths(next)) {
				this.addPath(next, path);
			}
		}
	}
	
	private void addInputNode(Node node) {
		in.add(node);
	}
	
	private void addOutputNode(Node node) {
		out.add(node);		
	}	
	
	public void addPath(Node node, Path path) {
		// add path to set
		getPaths(node).add(path);
	}

	public void addPaths(Node node, Set<Path> paths) {
		
		Set<Path> set = getPaths(node);
		
		for(Path path:paths) {		
			set.add(path);
		}
	}
	
	public void addPaths(Map<Node,Set<Path>> paths2) {
		
		// for every edge
		for(Node node:paths2.keySet()) {
			
			// merge set of paths
			addPaths(node, paths2.get(node));
		}
	}
	
	
	public void setItem(Object item) {
		this.item = item;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public void removeEdge(Node node) {
		
		// remove input and output nodes
		this.removeOutputNode(node);
		node.removeInputNode(this);
		
		// remove paths
		this.paths.remove(node);
	}
	
	private void removeInputNode(Node node) {
		in.remove(node);
	}
	
	private void removeOutputNode(Node node) {
		out.remove(node);
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
