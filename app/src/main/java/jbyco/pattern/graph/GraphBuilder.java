package jbyco.pattern.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class GraphBuilder {

	// actual path
	Path path;
	
	// built graph
	SuffixGraph graph;
	
	// last added node
	SuffixNode node;
	
	// set of reachable nodes
	Set<SuffixNode> reachables;
	Stack<SuffixNode> stack;
	
	// set of candidate nodes
	Map<Object, Candidates> candidates;
	
	// set of duplicate path
	Set<Path> duplicates;
	
	// current depth
	int depth;
	
	public GraphBuilder(SuffixGraph graph) {

		this.graph = graph;
		this.candidates = new HashMap<>();		
		this.duplicates = new HashSet<>();
		this.stack = new Stack<>();
	}
	
	public void startPath() {
		
		depth = 0;
		node = graph.getRoot();
		
		if (duplicates.isEmpty()) {
			path = new Path();
		}
		
		duplicates.clear();
		duplicates.addAll(node.getNodePaths());
		
		reachables = new HashSet<>();
		
		for(Candidates c:candidates.values()) {
			c.init();
		}
	}
	
	public void finishPath() {
		
		if (!duplicates.isEmpty()) {
			
			// calculate intersection with all output paths
			for(Set<Path> set:node.getPaths().values()) {
				duplicates.removeAll(set);
			}
			
			if(!duplicates.isEmpty()) {
			
				// get duplicate path
				Path duplicate = duplicates.iterator().next();
				duplicate.increment();
				
				// remove path from nodes
				SuffixNode current = node;
				while(current != graph.getRoot()) {
				
					for (SuffixNode prev:current.getInputNodes()) {
						if (prev.getEdgePaths(current).remove(path)) {
							current = prev;
							break;
						}
					}
				}
			}
		}
	}
	
	public void addNextNode(Object item) {
		
		// update current dept of path
		depth++;
		
		// try to find node in neighbors of previous node
		SuffixNode next = node.findNext(item);
		
		if (next == null) {
			
			// or find node in candidates
			if (node != graph.getRoot()) {

				// update reachables
				updateReachables(node);
				
				// find a candidate
				next = findCandidate(item);							
			}
			
			// or create new node
			if (next == null) {
				
				// init node
				next = new SuffixNode(item);
				
				// set depth
				next.setDepth(depth);
				
				// update candidates
				updateCandidates(next);
			}
			
			// create edge
			node.addEdge(next);
			duplicates.clear();
		}
		else {
			// compute intersection with edge paths
			duplicates.retainAll(node.getEdgePaths(next));
		}
		
		// add path
		node.addPath(next, path);
		node = next;
	}
	
	public void updateReachables(SuffixNode node) {
		
		// init stack
		stack.clear();
		
		// add node
		reachables.add(node);
		stack.push(node);
		
		// search graph from node to root
		while(!stack.isEmpty()) {
			
			// visit previous nodes
			for(SuffixNode prev : stack.pop().getInputNodes()) {
				
				// add it to reachables
				if(reachables.add(prev)) {
					stack.push(prev);
				}
			}
		}
	}
	
	public SuffixNode findCandidate(Object item) {
		
		// get candidates for item
		Candidates candidates = this.candidates.get(node.getItem());
		
		Iterator<SuffixNode> i = candidates.available.iterator();
		while(i.hasNext()) {
			
			SuffixNode candidate = i.next();
						
			// check reachables
			if(reachables.contains(candidate)) {
				candidates.removed.push(candidate);
				i.remove();
				continue;
			}
				
			// check depth
			if(depth - candidate.getDepth() >= -10) {
					
				// found candidate
				return candidate;
			}
		}
		
		return null;
	}
	
	public void updateCandidates(SuffixNode node) {
		
		// find candidates for node item
		Object item = node.getItem();
		Candidates candidates = this.candidates.get(item);
		
		if (candidates == null) {
			candidates = new Candidates();
			this.candidates.put(item, candidates);
		}
		
		// push node to reachable nodes
		candidates.remove(node);
		
	}

	class Candidates {
		
		// queue of available candidates
		TreeSet<SuffixNode> available;
		
		// list of unavailable candidates
		Stack<SuffixNode> removed;
		
		public Candidates() {
			this.available = new TreeSet<>(DepthComparator.getInstance());
			this.removed = new Stack<>();
		}

		public void init() {
			
			// make all nodes available
			while(!removed.isEmpty()) {
				available.add(removed.pop());
			}
		}
		
		public void add(SuffixNode node) {
			available.add(node);
		}
		
		public void remove(SuffixNode node) {
			available.remove(node);
			removed.push(node);
		}	
	}
}
