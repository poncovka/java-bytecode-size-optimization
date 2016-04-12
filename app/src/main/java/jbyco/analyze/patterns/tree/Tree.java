package jbyco.analyze.patterns.tree;

import java.io.PrintWriter;
import java.util.Stack;

public class Tree<T> {

	// root node of the graph with no item
	Node<T> root;
	
	public Tree() {
		root = new Node<T>(null);
	}
	
	public Node<T> getRoot() {
		return root;
	}	
	
	public void write(PrintWriter out) {
		
		out.printf("%-15s%-30s%-30s\n", "[node]", "[output]", "[count]");
		
		Stack<Node<T>> stack = new Stack<Node<T>>();
		stack.push(root);
		
		while(!stack.isEmpty()) {
			
			// get node
			Node<T> node = stack.pop();
			
			// print it
			out.printf("%-15s%-30s%-30s\n", node, node.getOutputNodes(), node.getCount());
			
			// add next nodes
			for(Node<T> next:node.getOutputNodes()) {
				stack.push(next);
			}
		}
		
	}
	
	public void export(PrintWriter out) {
		
		TreeExporter exporter = new TreeExporter(out);
		
		exporter.printDocumentHead();
		exporter.printGraphStart();
		
		for (int cycle = 0; cycle < 2; cycle++) {
			
			Stack<Node<T>> stack = new Stack<Node<T>>();
			stack.push(root);
			
			while(!stack.isEmpty()) {
				
				// get node
				Node<T> node = stack.pop();
				
				// print nodes in the 1st cycle
				if (cycle == 0) {
					exporter.printNode(node.getId(), node);
				}
				
				for(Node<T> next:node.getOutputNodes()) {
					
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
