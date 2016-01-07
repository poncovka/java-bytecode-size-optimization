package jbyco.io;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import jbyco.analyze.patterns.graph.Node;
import jbyco.analyze.patterns.graph.Path;
import jbyco.analyze.patterns.graph.SuffixGraph;
import jbyco.analyze.patterns.graph.WildCard;

public class PatternsPrinter {

	SuffixGraph graph;
	String delimiter;
	int min;
	
	Deque<StackItem> stack;
	
	public void print(SuffixGraph graph, String delimiter, int min) {
		
		// init
		init(graph, delimiter, min);
		
		// process stack
		while(!stack.isEmpty()) {
			
			// get head
			StackItem item = stack.getFirst();			
			//System.out.println(item.node + " " + item.paths);
						
			// get next item
			StackItem next = null;
			while (item.hasNext() && (next = item.next()) == null);
						
			if (next != null) {
				
				// push item on stack
				stack.push(next);
				
				// if printable, print stack
				if (isPrintable(next)) {
					printPattern();
				}

			}
			else {
				
				// or pop head of stack
				stack.pop();
			}
		}
	}
	
	private void init(SuffixGraph graph, String delimiter, int min) {
		
		this.graph = graph;
		this.delimiter = delimiter;
		this.min = min;
		
		Node root = this.graph.getRoot();
		Set<Path> paths = new TreeSet<>();
		
		for (Node node:root.getOutputNodes()) {
			paths.addAll(root.getEdgePaths(node));
		}
		
		this.stack = new ArrayDeque<>();
		this.stack.push(new StackItem(root, paths));
		
	}
		
	private void printPattern() {
		
		// print frequency
		System.out.printf("%-15s", stack.getFirst().count);
		
		// for all nodes in a stack
		Iterator<StackItem> iterator = stack.descendingIterator();
		while(iterator.hasNext()) {
			
			// print items in nodes
			StackItem item = iterator.next();
			
			if (item.node != graph.getRoot()) {
				System.out.printf("%s%s", item.node.getItem(), delimiter);
			}
		}
		
		// new line
		System.out.println();
	}
	
	private boolean isPrintable(StackItem item) {
		return !(
				   item.node == graph.getRoot() 
				|| item.node.getItem() instanceof WildCard
				|| item.count < min
				);
	}
	
	private class StackItem {
		
		public final Node node;
		public final Set<Path> paths;
		public final Iterator<Node> iterator;
		public final int count;
		
		public StackItem(Node node, Set<Path> paths) {		
			this.node = node;
			this.paths = paths;
			this.iterator = node.getOutputNodes().iterator();
			
			int count = 0;
			for (Path path:paths) {
				count += path.getCount();
			}
			this.count = count; 
		}
		
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		public StackItem next() {
			
			// get node
			Node next = this.iterator.next();
			
			// calculate paths
			Set<Path> paths = new TreeSet<>(this.paths);
			paths.retainAll(this.node.getEdgePaths(next));
			
			// return new stack item
			if (!paths.isEmpty()) {
				return new StackItem(next, paths); 
			}
			
			return null;
		}
	}
}
