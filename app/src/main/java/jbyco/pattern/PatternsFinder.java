package jbyco.pattern;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class PatternsFinder {
	
	public void minimize(PrefixNode root) {
		
		PrefixNode n1, n2;
		PrefixNodeMap map = new PrefixNodeMap();
		PrefixNodeQueue queue = new PrefixNodeQueue();
		boolean restart = true;
		
		while(restart) {

			// init restart
			restart = false;

			// init search
			map.init();
			queue.init();
			
			// start search from root
			n1 = search(queue, root);
			
			// while node to process
			while(n1 != null) {
				
				//System.err.println(n1.getNumber());
							
				// find pair for n1			
				n2 = findPair(map, n1);

				// join nodes and restart
				if (n2 != null) {
					join(n2, n1);
					
					System.err.println("Found pair: " + n2.getNumber() + ", " + n1.getNumber());
					
					restart = true;
					break;
				}
				
				// add node to the map
				map.add(n1);
				
				// get next node
				n1 = search(queue,n1);
			}
		}
		
	}

	
	public PrefixNode search(PrefixNodeQueue queue, PrefixNode node) {
		
		// add all output nodes of the current node to queue
		for(PrefixNode n:node.getOutputNodes()) {
			queue.add(n);
		}
		
		// return next node in the queue
		return queue.get();
	}
	
	public PrefixNode findPair(PrefixNodeMap map, PrefixNode node) {
		
		PrefixNode winner = null;
		LinkedHashSet<PrefixNode> candidates = new LinkedHashSet<PrefixNode>(map.getList(node));
		
		// init
		Stack<PrefixNode> stack = new Stack<PrefixNode>();
		Set<PrefixNode> visited = new HashSet<PrefixNode>();
		
		stack.push(node);
		visited.add(node);
		
		// breadth first search
		while (candidates.size() > 0 && !stack.isEmpty() ) {
			
			// get node
			node = stack.pop();
						
			// try to remove the node from candidates
			if (candidates.remove(node)) {
				// don't search unsuccessful candidates
				continue;
			}
			
			// search inputs
			for(PrefixNode next:node.getInputNodes()) {
				
				// // add previous nodes
				if (!visited.contains(next)) {
					
					visited.add(next);
					stack.push(next);
					
				}
			}
		}
		
		// first candidate in a list is a winner
		if (!candidates.isEmpty()) {
			winner = candidates.iterator().next();
		}
		
		return winner;
	}
	
	public boolean isPair(PrefixNode n1, PrefixNode n2) {
		
		// same item types?
		if (!n1.getItem().equals(n2.getItem())) {
			return false;
		}
		
		// empty intersection of paths?
		Set<Integer> set1, set2;
		
		if (n1.getPaths().size() < n2.getPaths().size()) {
			set1 = n1.getPaths();
			set2 = n2.getPaths();
		}
		else {
			set1 = n2.getPaths();
			set2 = n1.getPaths();
		}
		
		for(int i:set1) {
			if (set2.contains(i)) {
				return false;
			}
		}
		
		return true;
	}
	
	
	public void join(PrefixNode n1, PrefixNode n2) {
	
		// join paths
		n1.getPaths().addAll(n2.getPaths());
		
		// edit input nodes of n2
		for(PrefixNode n:n2.getInputNodes()) {
			
			n.removeOutputNode(n2);
			n.addOutputNode(n1);
			
			n1.addInputNode(n);
		}
		
		// edit output nodes of n2
		for(PrefixNode n:n2.getOutputNodes()) {
			
			n.removeInputNode(n2);
			n.addInputNode(n1);
			
			n1.addOutputNode(n);
		}
	}

}
