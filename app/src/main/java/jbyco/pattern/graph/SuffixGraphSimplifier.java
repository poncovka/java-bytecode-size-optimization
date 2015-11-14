package jbyco.pattern.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class SuffixGraphSimplifier {
	
	SuffixGraph graph;

	final int SEEN = 1;
	final int DOWN = 2;
	final int UP = 3;
	
	public SuffixGraphSimplifier(SuffixGraph graph) { 
		this.graph = graph;
	}
	
	public void simplify(int threshold) {
		
		// init
		Stack<SuffixNode> stack = new Stack<>();
		Map<SuffixNode, Integer> visited = new HashMap<>();
		
		// add root
		stack.push(graph.getRoot());
		visited.put(graph.getRoot(), SEEN);
		
		// postorder
		while(!stack.isEmpty()) {
			
			SuffixNode node = stack.pop();
			int flag = visited.get(node);
			
			// top to bottom visit
			if (flag == SEEN) {
				
				// check threshold
				if(node.getPaths().size() <= threshold) {
					// create wild card
					node.setItem(new WildCard());
				}
				
				// node will be visited on the way up
				stack.push(node);
				
				// add next nodes to stack
				for(SuffixNode next:node.getOutputNodes()) {
					
					// add only nodes that were not seen
					if (!visited.containsKey(next)) {
						stack.add(next);
						visited.put(next, SEEN);
					}
					
				}
				
				// node was visited on the way down
				visited.put(node, DOWN);
			}
			// bottom to top visit
			else if (flag == DOWN) {	
				
				// get list of output nodes with wild cards
				List<SuffixNode> wnodes = findWildCards(node);
				
				// how many wild cards do we have?
				int numberOfWildCards = wnodes.size() + ((node.getItem() instanceof WildCard) ? 1:0);  
				
				// have we found at least two wild cards?
				if (numberOfWildCards >= 2) {
					
					// join all found nodes with wild cards
					joinWildCards(node, wnodes);
				}
				
				// node was visited on the way up
				visited.put(node, UP);
			}			
		}
	}

	public List<SuffixNode> findWildCards(SuffixNode node) {
		
		// list of output nodes with wild cards
		List<SuffixNode> wnodes = new ArrayList<>();
		
		// find all wild cards
		for(SuffixNode next:node.getOutputNodes()) {
			
			if(next.getItem() instanceof WildCard) {
				wnodes.add(next);
			}
		}
		
		return wnodes;
	}
	
	public void joinWildCards(SuffixNode node, List<SuffixNode> wnodes) {
		
		SuffixNode finalNode = null;
		
		int min = Integer.MAX_VALUE;
		int max = 0;
		
		// join all wild cards to node
		if (node.getItem() instanceof WildCard) {
			finalNode = node;
		}
		// join all wild cards to new node
		else {
			finalNode = new SuffixNode(new WildCard(0,0));
			node.addEdge(finalNode);
		}
		
		// for every node with a wild card
		for (SuffixNode wnode:wnodes) {
			
			WildCard w = (WildCard) wnode.getItem();
			
			// update min, max
			min = Integer.min(min, w.getMin());
			max = Integer.max(max, w.getMax());
			
			// remove edge from node to wnode
			node.removeEdge(wnode);
			
			// copy all output edges from wnode
			for(SuffixNode next:wnode.getOutputNodes()) {
				finalNode.addEdge(next);
			}
			
			// copy paths
			for(int path:wnode.getPaths()) {
				finalNode.addPath(path);
			}
		}
		
		// create new wild card
		WildCard finalWildCard = (WildCard)finalNode.getItem();
		
		// set min, max
		finalWildCard.setMin(min + finalWildCard.getMin());
		finalWildCard.setMax(max + finalWildCard.getMax());
	}
}
