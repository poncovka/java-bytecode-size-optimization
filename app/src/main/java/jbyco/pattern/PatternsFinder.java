package jbyco.pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import jbyco.pattern.graph.PrefixNode;
import jbyco.pattern.graph.WildCard;

public class PatternsFinder {
	
	public Map<String, Integer> getPatterns(PrefixNode root, String delimiter, int threshold) {

		class StackItem {
			
			public final PrefixNode node;
			public final String pattern;
			public final Set<Integer> paths;
			
			public StackItem(PrefixNode node, String pattern, Set<Integer> paths) {
				this.node = node;
				this.pattern = pattern;
				this.paths = paths;
			}
		}
		
		Stack<StackItem> stack = new Stack<>();
		Map<String, Integer> patterns = new HashMap<>();
		
		// skip root and firstlevel wildcards
		for(PrefixNode next:root.getOutputNodes()) {
			if(!(next.getItem() instanceof WildCard)) {
				stack.push(new StackItem(next, "", root.getPaths()));
			}
		}
		
		// preorder
		while(!stack.isEmpty()) {
			
			// pop
			StackItem item = stack.pop();
			
			PrefixNode node = item.node;
			String pattern = new String(item.pattern);
			Set<Integer> paths = new HashSet<>(item.paths);
			
			// update path
			paths.retainAll(node.getPaths());
			
			// check threshold
			if (paths.size() <= threshold) {
				continue;
			}
			
			// update pattern
			if(pattern.length() > 0) {
				pattern += delimiter;
			}
			
			pattern += node.getItem();
			
			// add output nodes
			for(PrefixNode next:node.getOutputNodes()) {
				
				stack.push(new StackItem(next, pattern, paths));
				
			}
			
			// save the pattern if it does not end with wild card
			if (!(node.getItem() instanceof WildCard)) {
				patterns.put(pattern, paths.size());
			}
			
			System.out.println("PATTERN: " + node + "\t" +  pattern);
		}
		
		return patterns;
	}
	
	public void simplify(PrefixNode root, int threshold) {
		
		Stack<PrefixNode> stack = new Stack<>();
		Map<PrefixNode, Integer> visited = new HashMap<>();
		
		final int SEEN = 1;
		final int DOWN = 2;
		final int UP = 3;
		
		stack.push(root);
		visited.put(root, SEEN);
		
		// postorder
		while(!stack.isEmpty()) {
			
			PrefixNode node = stack.pop();
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
				for(PrefixNode next:node.getOutputNodes()) {
					
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
				
				System.err.println("DOWN, " + node);
				
				// list of output nodes with wild cards
				List<PrefixNode> wnodes = new ArrayList<>();
				
				// find all wild cards
				for(PrefixNode next:node.getOutputNodes()) {
					
					if(next.getItem() instanceof WildCard) {
						wnodes.add(next);
					}
				}
				
				// is the node a wild card?
				int isWildCard = (node.getItem() instanceof WildCard) ? 1:0;
				
				// have we found at least two wild cards?
				if (wnodes.size() + isWildCard >= 2) {
					
					// prepare final node
					PrefixNode finalNode = null;
					
					if (isWildCard == 1) {
						finalNode = node;
					}
					else {
						finalNode = new PrefixNode(new WildCard(0,0));
						finalNode.addInputNode(node);
						node.addOutputNode(finalNode);
					}
					
					// get min, max, update edges
					int min = Integer.MAX_VALUE;
					int max = 0;
					
					for (PrefixNode wnode:wnodes) {
						
						WildCard w = (WildCard) wnode.getItem();
						
						// update min, max
						min = Integer.min(min, w.getMin());
						max = Integer.max(max, w.getMax());
						
						// remove edge from node to wnode
						node.getOutputNodes().remove(wnode);
						
						// copy all output edges from wnode
						finalNode.getOutputNodes().addAll(wnode.getOutputNodes());
						
						// copy paths
						finalNode.getPaths().addAll(wnode.getPaths());
					}
					
					// set min, max in final wild card
					WildCard finalWildCard = (WildCard)finalNode.getItem();
					
					finalWildCard.setMin(min + finalWildCard.getMin());
					finalWildCard.setMax(max + finalWildCard.getMax());		
					
					System.err.println("JOINT TO " + finalNode);
				}
				
				// node was visited on the way up
				visited.put(node, UP);
			}			
		}
	}
	
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
			
			// skip root and first level
			for (PrefixNode n:root.getOutputNodes()) {
				enqueue(queue, n);
			}
			
			// get first node in queue
			n1 = dequeue(queue);
			
			// while node to process
			while(n1 != null) {
											
				// find pair for n1			
				n2 = findPair(map, n1);

				// join nodes and restart
				if (n2 != null) {
					joinPair(n2, n1);
					
					System.err.println("Found pair: " + n2 + ", " + n1);
					
					restart = true;
					break;
				}
				
				// add node to the map
				map.add(n1);
				
				// get next node
				enqueue(queue,n1);
				
				// get next node
				n1 = dequeue(queue);
			}
		}
		
	}

	
	private void enqueue(PrefixNodeQueue queue, PrefixNode node) {
		
		// add all output nodes of the current node to queue
		for(PrefixNode n:node.getOutputNodes()) {
			queue.add(n);
		}
	}
	
	private PrefixNode dequeue(PrefixNodeQueue queue) {	
		// return next node in the queue
		return queue.get();
	}
	
	private PrefixNode findPair(PrefixNodeMap map, PrefixNode node) {
		
		PrefixNode winner = null;
		LinkedHashSet<PrefixNode> candidates = new LinkedHashSet<PrefixNode>(map.getList(node));
		
		// init
		Stack<PrefixNode> stack = new Stack<PrefixNode>();
		Set<PrefixNode> visited = new HashSet<PrefixNode>();
		
		stack.push(node);
		visited.add(node);
		
		// depth first search from node
		while (candidates.size() > 0 && !stack.isEmpty() ) {
			
			// get node
			node = stack.pop();
						
			// node is a candidate
			if (candidates.contains(node)) {
				
				// remove the node and all previous candidates
				Iterator<PrefixNode> i = candidates.iterator();
				while(i.hasNext()) {
					
					if (i.next() != node) {
						i.remove();
					}
					else {
						i.remove();
						break;
					}
				}
				
				// do not visit previous nodes
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
	
	
	private void joinPair(PrefixNode n1, PrefixNode n2) {
	
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
