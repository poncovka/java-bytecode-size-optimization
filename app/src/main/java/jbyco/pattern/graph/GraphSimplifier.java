package jbyco.pattern.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class GraphSimplifier {

	SuffixGraph graph;
	
	public GraphSimplifier(SuffixGraph graph) { 
		this.graph = graph;
	}
	
	public void simplify(int threshold) {
		
		Queue<SuffixNode> queue = new LinkedList<>();
		Set<SuffixNode> visited = new HashSet<>();
		
		// init queue
		queue.add(graph.getRoot());
		visited.add(graph.getRoot());
		
		// breadth first search
		while(!queue.isEmpty()) {
			
			// get node
			SuffixNode node = queue.poll();			
			
			// find wild cards
			Map<SuffixNode, WildCard> wildcards = findWildCards(node, threshold);
			
			// change graph
			if (!wildcards.isEmpty()) {
				simplifyWildCards(node, wildcards);
			}
			
			// add next nodes to queue
			for(SuffixNode next : node.getOutputNodes()) {
				if (!visited.contains(next)) {
					queue.add(next);
					visited.add(next);
				}
			}
		}	
	}
	
	private Map<SuffixNode, WildCard> findWildCards(SuffixNode node, int threshold) {
		
		// init
		Map<SuffixNode, WildCard> wildcards = new HashMap<>();
		
		// find wildcards
		for(SuffixNode next : node.getOutputNodes()) {
			
			// get number of paths on the edge to next
			int npaths = node.getEdgePaths(next).size();
			
			// remember the node with wildcard
			if(npaths <= threshold) {
				wildcards.put(next, new WildCard(npaths, npaths));
			}
		}
		
		return wildcards;
	}

	private void simplifyWildCards(SuffixNode node, Map<SuffixNode, WildCard> wildcards) {
		
		// do we need to create a new node with a wild card?
		boolean create = !(node.getItem() instanceof WildCard); 
		
		// create new wild card and node
		WildCard w = create ? new WildCard(0,0) : (WildCard) node.getItem();
		SuffixNode next2 = create ? new SuffixNode(w) : node;
		
		// update wild cards
		wildcards.put(node, w);
		updateWildcards(node, wildcards);
		wildcards.remove(node);

		// update edges
		for(SuffixNode next : wildcards.keySet()) {
			
			if(create) {
				node.addPaths(next2, node.getEdgePaths(next));
			}
			
			node.removeEdge(next);
			next2.copyEdges(next);
			next2.addPaths(next.getPaths());
		}
		
		// add edge to next2
		if(create) {
			node.addEdge(next2);
		}

	}
	
	private void updateWildcards(SuffixNode root, Map<SuffixNode, WildCard> wildcards) {
		
		final int SEEN = 1;
		final int DOWN = 2;
		final int UP = 3;
		
		Stack<SuffixNode> stack = new Stack<>();
		Map<SuffixNode, Integer> visited = new HashMap<>();
		
		stack.add(root);
		visited.put(root, SEEN);
		
		// post order
		while(!stack.isEmpty()) {
			
			SuffixNode node = stack.pop();
			int flag = visited.get(node);
			
			// top to bottom
			if (flag == SEEN) {
				
				// node will be visited on the way up
				stack.push(node);
				
				// add next nodes to stack
				for (SuffixNode next : node.getOutputNodes()) {
					
					// add only wildcards that were not seen
					if(wildcards.containsKey(next) && !visited.containsKey(next)) {
						stack.add(next);
						visited.put(next, SEEN);
					}
				}
				
				// the node was visited on the way down
				visited.put(node, DOWN);
			}
			// bottom to top
			else if (flag == DOWN) {
				
				boolean found = false;
				WildCard w1 = wildcards.get(node);
				WildCard w2 = new WildCard(Integer.MAX_VALUE, 0);
				
				// compute parallel min, max
				for (SuffixNode next : node.getOutputNodes()) {
					
					if(wildcards.containsKey(next)) {
						w2.addParallel(wildcards.get(next));
						found = true;
					}
				}
				
				// compute sequential min, max
				if (found) {
					w1.addSequencial(w2);
				}
				
				// node was visited on the way up
				visited.put(node, UP);
			}
		}
	}
	
}
