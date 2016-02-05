package jbyco.analyze.patterns.graph;

public class GraphBuilder {
	
	// the built graph
	SuffixTree graph;
	
	// the last added node
	Node node;
		
	public GraphBuilder(SuffixTree graph) {
		this.graph = graph;
	}
	
	public void startPath() {
		node = graph.getRoot();
	}
	
	public void finishPath() {		
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
}
