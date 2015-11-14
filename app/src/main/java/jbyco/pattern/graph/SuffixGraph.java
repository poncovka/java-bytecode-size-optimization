package jbyco.pattern.graph;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import jbyco.io.graph.GmlExporter;

public class SuffixGraph {

	SuffixNode root;
	
	public SuffixGraph() {
		root = new SuffixNode(new Root());
	}
	
	public SuffixNode getRoot() {
		return root;
	}	

	public void print(PrintStream out) {
		
		out.printf("%-15s%-30s%-30s\n", "[node]", "[output]", "[paths]");
		
		Stack<SuffixNode> stack = new Stack<SuffixNode>();
		Set<SuffixNode> visited = new HashSet<SuffixNode>();
		
		stack.push(root);
		visited.add(root);
		
		while(!stack.isEmpty()) {
			
			// get node
			SuffixNode node = stack.pop();
			
			// print it
			out.printf("%-15s%-30s%-30s\n", node, node.getOutputNodes(), node.getPaths());
			
			// add next nodes
			for(SuffixNode next:node.getOutputNodes()) {
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
			
			Stack<SuffixNode> stack = new Stack<SuffixNode>();
			Set<SuffixNode> visited = new HashSet<SuffixNode>();
			
			stack.push(root);
			visited.add(root);
			
			while(!stack.isEmpty()) {
				
				// get node
				SuffixNode node = stack.pop();
				
				// print nodes in the 1st cycle
				if (cycle == 0) {
					//exporter.printNode(node.getNumber(), node.getItem());
					exporter.printNode(node.getNumber(), node);
				}
				
				for(SuffixNode next:node.getOutputNodes()) {
					
					// print edges in the 2nd cycle
					if (cycle == 1) {
						
						// get paths on the edge
						Set<Integer> epaths = node.getEdgePaths(next);
						
						// print edge
						exporter.printEdge(node.getNumber(), next.getNumber(), epaths);
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
