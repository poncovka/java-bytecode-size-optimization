package jbyco.analyze.patterns.graph;

import java.util.Stack;

public class GraphBuilder {
	
	// the built graph
	SuffixTree graph;
	
	// the last added node
	Node node;
		
	// threshold for pruning
	int threshold;
	
	// maximal memory usage in percents
	int maxMemoryUsage = 50;
		
	public GraphBuilder(SuffixTree graph) {
		this.graph = graph;
		this.threshold = 1;
	}
	
	public void startPath() {
		node = graph.getRoot();
		node.incrementCount();
	}
	
	public void addNextNode(Object item) {
		
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
		
		// remember node
		node = next;
	}
	
	public void finishPath() {	
		
		// get runtime
		Runtime runtime = Runtime.getRuntime();
		
		// check available memory
		int memoryUsage = (int)((runtime.totalMemory() - runtime.freeMemory()) * 100.0 / runtime.maxMemory());
			
		// prune?
		if (memoryUsage > maxMemoryUsage) {

			System.err.printf("Pruning... memory_usage=%d pruning_threshold=%d\n", memoryUsage, threshold);
			
			// prune
			pruneGraph();
			
			// increment threshold
			threshold++;
			
			// try to run gc
			runtime.gc();
		}
	}
	
	public void pruneGraph() {
				
		// init stack
		Stack<Node> stack = new Stack<>();
		stack.push(graph.getRoot());
		
		// walk the tree
		while(!stack.isEmpty()) {
			
			// get the node
			Node node = stack.pop();
			
			// prune?
			if (node.getCount() <= threshold) {
								
				Node prev = node.getInputNode();
				
				if (prev != null) {
					prev.removeEdge(node);
				}
			}
			// get output nodes
			else {
				for (Node next : node.getOutputNodes()) {
					stack.push(next);
				}	
			}			
		}		
	}
}
