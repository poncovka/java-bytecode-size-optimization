package jbyco.pattern.graph;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class GraphBuilder {

	// actual path
	Path path;
	
	// built graph
	SuffixGraph graph;
	
	// last added node
	SuffixNode node;
	
	// nodes of the path
	Deque<SuffixNode> nodes;
	
	// set of reachable nodes
	BitSet reachables;
	
	// set of candidate nodes
	Map<Object, Candidates> candidates;
	
	// stack with nodes to seach
	Deque<SuffixNode> stack;
	
	// should we create new Path? 
	boolean createPath = true;
	
	// set of same paths
	Set<Path> paths = new HashSet<>();
	
	// current depth
	int depth = 0;
	
	// maximal difference of depths for candidates
	int DEPTHDIFF = 0;
	
	public GraphBuilder(SuffixGraph graph) {

		this.graph = graph;
		this.candidates = new HashMap<>();		
		this.nodes = new ArrayDeque<>();
		
		stack = new ArrayDeque<>();
		paths = new HashSet<>();
	}
	
	public void startPath() {
		
		depth = 0;
		node = graph.getRoot();
		nodes.clear();
		nodes.push(node);
		
		createPath = false;
		paths.clear();
		
		reachables = new BitSet(SuffixNode.getTotal());		
		stack.clear();		
	}
	
	public void finishPath() {
		
		// update candidates
		for (SuffixNode node:nodes) {
			if (node.getDepth() > 1) {
				updateCandidates(node);
			}
		}
		
		// try to recycle path
		if (!createPath) {
					
			// remove paths that does not end in the node
			for (SuffixNode next:node.getOutputNodes()) {
				paths.removeAll(node.getEdgePaths(next));
			}
				
			// increment path
			if(!paths.isEmpty()) {
				paths.iterator().next().increment();
			}
			// or create new path
			else {
				createPath = true;
			}
		}	
		
		// create new path
		if (createPath) {
			
			SuffixNode n1, n2 = null;
			Path path = new Path();
			
			while(!nodes.isEmpty()) {
				
				// get node
				n1 = nodes.pop();
				
				// add path from n1 to n2
				if (n2 != null) {
					n1.addPath(n2, path);
				}
				
				// remember node
				n2 = n1;
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
			if (depth > 1) {
				
				// find a candidate
				next = findCandidate(item);							
			}
			
			// or create new node
			if (next == null) {
				
				// init node
				next = new SuffixNode(item);
				
				// set depth
				next.setDepth(depth);
				//System.out.printf("%s, depth %s\n", next, depth);
			}
			
			// create edge
			node.addEdge(next);
			
			// we need to create new path
			createPath = true;
		}
		else {
			// searching for same paths
			updatePaths(next);
		}
		
		// remember node
		node = next;
			
		// add to sequence of nodes
		nodes.push(node);
	}
	
	public void updatePaths(SuffixNode next) {
		
		if (!createPath) {
			
			// init
			if (depth == 1) {
				paths.addAll(node.getEdgePaths(next));
				
			}
			// calculate intersection
			else {
				paths.retainAll(node.getEdgePaths(next));
			}
		}	
	}
	public boolean isReachable(SuffixNode node2) {
				
		// is n2 in reachables?
		if (reachables.get(node2.getId())) {
			//System.out.printf("%s, %s in reachables\n", node, node2);
			return true;
		}
		
		if(node.doSharePath(node2)) {
			return true;
		}
		
		// init stack
		stack.push(node);
		
		// search graph from node n1 to root
		while(!stack.isEmpty()) {
			
			// visit previous nodes
			for(SuffixNode prev : stack.pop().getInputNodes()) {
								
				// get id
				int num = prev.getId();
				
				// add it to reachables
				if (!reachables.get(num)) {
					reachables.set(num);
					stack.push(prev);
				}
				
				// is it node n2?
				if(prev == node2) {
					//System.out.printf("%s, %s found\n", node, node2);
					return true;
				}
			}
		}
		
		//System.out.printf("%s, %s not rechable\n", node, node2);
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
			//System.out.printf("%s, %s is candiate?\n", item, candidate);
						
			// check reachables
			if(isReachable(candidate)) {
				continue;
			}
			
			// found candidate
			//System.out.printf("%s, %s depth\n", depth, candidate.getDepth());
			if(depth - candidate.getDepth() >= DEPTHDIFF) {
				//System.out.printf("%s, %s is candiate\n", item, candidate);
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
	
		// iterator over candidates
		Iterator<SuffixNode> iterator;
		
		// last visited node
		SuffixNode node;
		
		// can iterator iterate?
		boolean iterate;
		
		public Candidates() {
			this.available = new TreeSet<>(DepthComparator.getInstance());
			init();
		}

		public void init() {		
			this.iterator = this.available.iterator();
			this.iterate = true;
		}
		
		public void add(SuffixNode node) {
			this.available.add(node);
			init();
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
