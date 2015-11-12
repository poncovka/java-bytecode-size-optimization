package jbyco.pattern.graph;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import jbyco.io.graph.GmlExporter;

public class PrefixGraph {

	PrefixNode root;
	
	public PrefixGraph() {
		root = new PrefixNode(new Root());
	}
	
	public PrefixNode getRoot() {
		return root;
	}	
	
	public PrefixNode addNextNode(PrefixNode node, Object item, int path) {
		
		// find the next node
		PrefixNode next = node.findNext(item);
		
		// or create it
		if (next == null) {
			
			next = new PrefixNode(item);
			node.addEdge(next);			
		}
		
		// add path
		next.addPath(path);
		
		// return next node
		return next;
	}

	public void print(PrintStream out) {
		
		out.printf("%-15s%-30s%-30s\n", "[node]", "[output]", "[paths]");
		
		Stack<PrefixNode> stack = new Stack<PrefixNode>();
		Set<PrefixNode> visited = new HashSet<PrefixNode>();
		
		stack.push(root);
		visited.add(root);
		
		while(!stack.isEmpty()) {
			
			// get node
			PrefixNode node = stack.pop();
			
			// print it
			out.printf("%-15s%-30s%-30s\n", node, node.getOutputNodes(), node.getPaths());
			
			// add next nodes
			for(PrefixNode next:node.getOutputNodes()) {
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
			
			Stack<PrefixNode> stack = new Stack<PrefixNode>();
			Set<PrefixNode> visited = new HashSet<PrefixNode>();
			
			stack.push(root);
			visited.add(root);
			
			while(!stack.isEmpty()) {
				
				// get node
				PrefixNode node = stack.pop();
				
				// print nodes in the 1st cycle
				if (cycle == 0) {
					//exporter.printNode(node.getNumber(), node.getItem());
					exporter.printNode(node.getNumber(), node);
				}
				
				for(PrefixNode next:node.getOutputNodes()) {
					
					// print edges in the 2nd cycle
					if (cycle == 1) {
						
						// get intersection of paths
						Set<Integer> intersection = new HashSet<>(node.getPaths());
						intersection.retainAll(next.getPaths());
						// print edge
						exporter.printEdge(node.getNumber(), next.getNumber(), intersection);
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
