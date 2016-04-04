package jbyco.analyze.patterns.tree;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

public class TreeBuilder {
	
	Tree graph;
		
	public TreeBuilder(Tree graph) {
		this.graph = graph;
	}
	
	public void addPath(Collection<?> items) {
		
		// init
		Node node = graph.getRoot();
		node.incrementCount();
		
		// add all items
		for (Object item : items) {
			node = addNextNode(node, item);
		}
	}
	
	public Node addNextNode(Node node, Object item) {
		
		// try to find node in neighbors of previous node
		Node next = node.findNextNode(item);
		
		if (next == null) {
			
			// create node
			next = new Node(item);
						
			// create edge
			node.addEdge(next);
		}
		
		// update counter
		next.incrementCount();
		
		// return next node
		return next;
	}
	
	public void pruneTree(int threshold) {
		
		// init stack, the root cannot be removed
		Stack<Node> stack = new Stack<>();
		stack.push(graph.getRoot());
		
		// walk the tree
		while(!stack.isEmpty()) {
			
			// get the node
			Node node = stack.pop();			
						
			Iterator<Node> i = node.getOutputNodes().iterator();
			
			// process next nodes
			while (i.hasNext()) {
				
				Node next = i.next();
				
				// should we prune the next node?
				if (next.getCount() <= threshold) {
					node.removeOutputNode(i);
					next.removeInputNode(node);
				}
				// or add node to the stack
				else {
					stack.push(next);	
				}
			}			
		}		
	}
}
