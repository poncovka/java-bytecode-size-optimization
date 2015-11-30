package jbyco.pattern.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class GraphBuilder {

	int path;
	SuffixGraph graph;
	Set<SuffixNode> reachables;
	Map<Object,List<SuffixNode>> candidates;
	
	public GraphBuilder(SuffixGraph graph) {

		this.path = -1;
		this.graph = graph;
		this.reachables = new HashSet<>();
		this.candidates = new HashMap<>();
	}
	
	public SuffixNode addNextNode(SuffixNode node, Object item) {
				
		//  do we start a new path?
		if (node == graph.getRoot()) {
			path++;
			
			initReachables();
			initCandidates();
		}	
		
		// try to find node in neighbors
		SuffixNode next = node.findNext(item);
		
		if (next == null) {
			
			// or find find node elsewhere
			if (node != graph.getRoot()) {

				// update reachables
				updateReachables(node);
				
				// find a candidate
				next = findCandidate(item);							
			}
			
			// or create new node
			if (next == null) {
				next = new SuffixNode(item);
			}
			
			// create edge
			node.addEdge(next);
		}
		
		// add path
		node.addPath(next, path);
		
		// return next node
		return next;
	}
	
	public void initReachables() {
		reachables.clear();
	}
	
	public void initCandidates() {
		candidates.clear();
		
		Queue<SuffixNode> queue = new LinkedList<>();
		Set<SuffixNode> visited = new HashSet<SuffixNode>();
		
		//queue.add(graph.getRoot());
		
		// skip root and first level
		for(SuffixNode next:graph.getRoot().getOutputNodes()) {
			queue.addAll(next.getOutputNodes());
		}
		
		// breadth first search
		while(!queue.isEmpty()) {
			
			// get candidate
			SuffixNode candidate = queue.poll();
	
			// get list
			List<SuffixNode> list = candidates.get(candidate.getItem());
			
			// or create list
			if (list == null) {
				list = new LinkedList<>();
				candidates.put(candidate.getItem(), list);
			}
			
			// add candidate to the list
			list.add(candidate);
			
			// visit next nodes
			for (SuffixNode next:candidate.getOutputNodes()) {
				
				if (!visited.contains(next)) {
					queue.add(next);
					visited.add(next);
				}
			}
			
		}
	}
	
	public void updateReachables(SuffixNode node) {
		
		Stack<SuffixNode> stack = new Stack<>();
		stack.push(node);
		
		// search graph from node to root
		while(!stack.isEmpty()) {
			
			// get node
			SuffixNode reachable = stack.pop();
			
			// add it to reachables
			reachables.add(reachable);
			
			// visit previous nodes
			for(SuffixNode prev:reachable.getInputNodes()) {
				if(!reachables.contains(prev)) {
					stack.push(prev);
				}
			}
		}
	}
	
	public SuffixNode findCandidate(Object item) {
		
		SuffixNode candidate = null;
		
		// get list of candidates
		List<SuffixNode> list = candidates.get(item);
				
		// search the list
		if (list != null) {
			
			
			Iterator<SuffixNode> i = list.iterator();
			while(i.hasNext()) {
				
				// get next
				SuffixNode next = i.next();
				
				// next is reachable, remove it from list of candidates
				if (reachables.contains(next)) {
					i.remove();
				}
				// found the candidate
				else {
					candidate = next;
					break;
				}
			}
		}
		
		return candidate;
	}

}
