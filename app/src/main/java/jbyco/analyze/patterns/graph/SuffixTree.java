package jbyco.analyze.patterns.graph;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Stack;

import jbyco.io.GmlExporter;

public class SuffixTree {

	// root node of the graph with no item
	Node root;
	
	public SuffixTree() {
		root = new Node(new Root());
	}
	
	public Node getRoot() {
		return root;
	}	
	
	public void print(PrintStream out) {
		
		out.printf("%-15s%-30s%-30s\n", "[node]", "[output]", "[count]");
		
		Stack<Node> stack = new Stack<Node>();
		stack.push(root);
		
		while(!stack.isEmpty()) {
			
			// get node
			Node node = stack.pop();
			
			// print it
			out.printf("%-15s%-30s%-30s\n", node, node.getOutputNodes(), node.getCount());
			
			// add next nodes
			for(Node next:node.getOutputNodes()) {
				stack.push(next);
			}
		}
		
	}
	
	public void printGml(PrintWriter out) {
		
		GmlExporter exporter = new GmlExporter(out);
		
		exporter.printDocumentHead();
		exporter.printGraphStart();
		
		for (int cycle = 0; cycle < 2; cycle++) {
			
			Stack<Node> stack = new Stack<Node>();
			stack.push(root);
			
			while(!stack.isEmpty()) {
				
				// get node
				Node node = stack.pop();
				
				// print nodes in the 1st cycle
				if (cycle == 0) {
					exporter.printNode(node.getId(), node);
				}
				
				for(Node next:node.getOutputNodes()) {
					
					// print edges in the 2nd cycle
					if (cycle == 1) {
						
						// get paths on the edge
						int count = node.getCount();
						
						// print edge
						exporter.printEdge(node.getId(), next.getId(), count);
					}
					
					// add next nodes
					stack.push(next);
				}
			} // while end
		} // for end
		
		exporter.printGraphEnd();
	}
	
}
