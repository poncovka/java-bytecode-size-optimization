package jbyco.analyze.patterns.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class GraphSimplifier {

	SuffixTree graph;
	
	public GraphSimplifier(SuffixTree graph) { 
		this.graph = graph;
	}
	
	public void simplify(int threshold) {
		
		Queue<Node> queue = new LinkedList<>();
		Set<Node> visited = new HashSet<>();
		
		// init queue
		queue.add(graph.getRoot());
		visited.add(graph.getRoot());
		
		// breadth first search
		while(!queue.isEmpty()) {
			
			// get node
			Node node = queue.poll();			
			
			// find wild cards
			Map<Node, WildCard> wildcards = findWildCards(node, threshold);
			
			// change graph
			if (!wildcards.isEmpty()) {
				simplifyWildCards(node, wildcards);
			}
			
			// add next nodes to queue
			for(Node next : node.getOutputNodes()) {
				if (!visited.contains(next)) {
					queue.add(next);
					visited.add(next);
				}
			}
		}	
	}
	
	private Map<Node, WildCard> findWildCards(Node node, int threshold) {
		
		// init
		Map<Node, WildCard> wildcards = new HashMap<>();
		
		// find wildcards
		for(Node next : node.getOutputNodes()) {
			
			// get number of paths on the edge to next
			int npaths = 0; //node.getEdgePaths(next).size();
			
			// remember the node with wildcard
			if(npaths <= threshold) {
				wildcards.put(next, new WildCard(npaths, npaths));
			}
		}
		
		return wildcards;
	}

	private void simplifyWildCards(Node node, Map<Node, WildCard> wildcards) {
		
		// do we need to create a new node with a wild card?
		boolean create = !(node.getItem() instanceof WildCard); 
		
		// create new wild card and node
		WildCard w = create ? new WildCard(0,0) : (WildCard) node.getItem();
		Node next2 = create ? new Node(w) : node;
		
		// update wild cards
		wildcards.put(node, w);
		updateWildcards(node, wildcards);
		wildcards.remove(node);

		// update edges
		for(Node next : wildcards.keySet()) {
			
			if(create) {
				//node.addPaths(next2, node.getEdgePaths(next));
			}
			
			//node.removeEdge(next);
			//next2.copyEdges(next);
			//next2.addPaths(next.getPaths());
		}
		
		// add edge to next2
		if(create) {
			node.addEdge(next2);
		}

	}
	
	private void updateWildcards(Node root, Map<Node, WildCard> wildcards) {
		
		final int SEEN = 1;
		final int DOWN = 2;
		final int UP = 3;
		
		Stack<Node> stack = new Stack<>();
		Map<Node, Integer> visited = new HashMap<>();
		
		stack.add(root);
		visited.put(root, SEEN);
		
		// post order
		while(!stack.isEmpty()) {
			
			Node node = stack.pop();
			int flag = visited.get(node);
			
			// top to bottom
			if (flag == SEEN) {
				
				// node will be visited on the way up
				stack.push(node);
				
				// add next nodes to stack
				for (Node next : node.getOutputNodes()) {
					
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
				for (Node next : node.getOutputNodes()) {
					
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
