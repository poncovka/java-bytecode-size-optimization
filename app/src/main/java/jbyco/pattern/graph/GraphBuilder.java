package jbyco.pattern.graph;

import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
	BitSet reachables;
	
	// set of candidate nodes
	Map<Object, Candidates> candidates;
	
	// stack with nodes to seach
	Stack<SuffixNode> stack;
	
	// set of duplicate path
	Set<Path> duplicates;
	
	// current depth
	int depth;
	
	public GraphBuilder(SuffixGraph graph) {

		this.graph = graph;
		this.candidates = new HashMap<>();		
		this.duplicates = new HashSet<>();
	}
	
	public void startPath() {
		
		depth = 0;
		node = graph.getRoot();
		
		if (duplicates.isEmpty()) {
			path = new Path();
		}
		
		duplicates.clear();
		duplicates.addAll(node.getNodePaths());
		
		reachables = new BitSet(SuffixNode.getCount());
		stack = new Stack<>();
		
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
				
				// find a candidate
				next = findCandidate(item);							
			}
			
			// or create new node
			if (next == null) {
				
				// init node
				next = new SuffixNode(item);
				
				// set depth
				next.setDepth(depth);
				System.out.printf("%s, depth %s\n", next, depth);
				
				// update candidates
				if (depth > 1) {
					updateCandidates(next);
				}
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
		
		// set reachables
		//reachables.set(node.getNumber());
	}
	
	public boolean isReachable(SuffixNode node2) {
		
		// is n2 in reachables?
		if (reachables.get(node2.getNumber())) {
			System.out.printf("%s, %s in reachables\n", node, node2);
			return true;
		}
		
		// have same paths
		Set<Path> paths = node.getNodePaths();
		paths.retainAll(node2.getNodePaths());
		
		if(!paths.isEmpty()) {
			System.out.printf("%s, %s same paths %s\n", node, node2, paths);
			return true;
		}
		
		// init stack
		stack.push(node);
		
		// search graph from node n1 to root
		while(!stack.isEmpty()) {
			
			// visit previous nodes
			for(SuffixNode prev : stack.pop().getInputNodes()) {
								
				// get id
				int num = prev.getNumber();
				
				// add it to reachables
				if (!reachables.get(num)) {
					reachables.set(num);
					stack.push(prev);
				}
				
				// is it node n2?
				if(prev == node2) {
					System.out.printf("%s, %s found\n", node, node2);
					return true;
				}
			}
		}
		
		System.out.printf("%s, %s not rechable\n", node, node2);
		return false;
	}
	
	public SuffixNode findCandidate(Object item) {
		
		// get candidates for item
		Candidates candidates = this.candidates.get(item);
		
		// no candidates
		if (candidates == null) {
			return null;
		}
		
		// search candidates
		while(candidates.hasNext()) {
			
			SuffixNode candidate = candidates.next();
			System.out.printf("%s, %s is candiate?\n", item, candidate);
						
			// check reachables
			if(isReachable(candidate)) {
				continue;
			}
			
			// found candidate
			System.out.printf("%s, %s depth\n", depth, candidate.getDepth());
			if(depth - candidate.getDepth() >= 0) {
				System.out.printf("%s, %s is candiate\n", item, candidate);
				return candidate;
			}
			// depth exceeded
			else {
				candidates.back();
				return null;
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
		
		// push node to the waiting list
		candidates.add(node);
	}

	class Candidates {
		
		// queue of available candidates
		TreeSet<SuffixNode> available;
		
		// queue of candidates to add
		Stack<SuffixNode> waiting;
		
		// iterator over candidates
		Iterator<SuffixNode> iterator;
		
		// last visited node
		SuffixNode node;
		
		// can iterator iterate?
		boolean iterate;
		
		public Candidates() {
			this.available = new TreeSet<>(DepthComparator.getInstance());
			this.waiting = new Stack<>();
			init();
		}

		public void init() {
			
			// add new candidates
			while(!waiting.isEmpty()) {
				available.add(waiting.pop());
			}
			
			// get iterator
			this.iterator = this.available.iterator();
			this.iterate = true;
		}
		
		public void add(SuffixNode node) {
			waiting.push(node);
		}
		
		public boolean hasNext() {
			return (!iterate) || iterator.hasNext();
		}
		
		public SuffixNode next() {
			
			// return next node
			if (iterate) {
				node = iterator.next();
			}
			
			// return last visited node
			else {
				iterate = true;
			}
			
			return node;
		}
		
		public void back() {
			iterate = false;
		}	
	}
}
