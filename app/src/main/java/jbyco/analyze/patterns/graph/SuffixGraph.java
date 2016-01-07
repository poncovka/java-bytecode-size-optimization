package jbyco.analyze.patterns.graph;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import jbyco.io.GmlExporter;

public class SuffixGraph {

	// root node of the graph with no item
	Node root;
	
	public SuffixGraph() {
		root = new Node(new Root());
	}
	
	public Node getRoot() {
		return root;
	}	
	
	public void print(PrintStream out) {
		
		out.printf("%-15s%-30s%-30s\n", "[node]", "[output]", "[paths]");
		
		Stack<Node> stack = new Stack<Node>();
		Set<Node> visited = new HashSet<Node>();
		
		stack.push(root);
		visited.add(root);
		
		while(!stack.isEmpty()) {
			
			// get node
			Node node = stack.pop();
			
			// print it
			out.printf("%-15s%-30s%-30s\n", node, node.getOutputNodes(), node.getPaths());
			
			// add next nodes
			for(Node next:node.getOutputNodes()) {
				if (!visited.contains(next)) {
					stack.push(next);
					visited.add(next);
				}
			}
		}
		
	}
	
	public void printGml(PrintWriter out) {
		
		GmlExporter exporter = new GmlExporter(out);
		
		exporter.printDocumentHead();
		exporter.printGraphStart();
		
		for (int cycle = 0; cycle < 2; cycle++) {
			
			Stack<Node> stack = new Stack<Node>();
			Set<Node> visited = new HashSet<Node>();
			
			stack.push(root);
			visited.add(root);
			
			while(!stack.isEmpty()) {
				
				// get node
				Node node = stack.pop();
				
				// print nodes in the 1st cycle
				if (cycle == 0) {
					//exporter.printNode(node.getNumber(), node.getItem());
					exporter.printNode(node.getId(), node);
				}
				
				for(Node next:node.getOutputNodes()) {
					
					// print edges in the 2nd cycle
					if (cycle == 1) {
						
						// get paths on the edge
						Set<Path> epaths = node.getEdgePaths(next);
						
						// print edge
						exporter.printEdge(node.getId(), next.getId(), epaths);
					}
					
					// add next nodes
					if (!visited.contains(next)) {
						stack.push(next);
						visited.add(next);
					}
				}
			} // while end
		} // for end
		
		exporter.printGraphEnd();
	}
	
}
