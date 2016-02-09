package jbyco.io;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import jbyco.analyze.patterns.graph.Node;
import jbyco.analyze.patterns.graph.SuffixTree;
import jbyco.analyze.patterns.graph.WildCard;

public class PatternsPrinter {

	SuffixTree graph;
	String delimiter;
	int min;
	
	Deque<StackItem> stack;
	
	public void print(SuffixTree graph, String delimiter, int min) {
		
		// init
		init(graph, delimiter, min);
		
		// process stack
		while(!stack.isEmpty()) {
			
			// get head
			StackItem item = stack.getFirst();			
						
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
	
	private void init(SuffixTree graph, String delimiter, int min) {
		
		this.graph = graph;
		this.delimiter = delimiter;
		this.min = min;
		
		Node root = this.graph.getRoot();
		
		this.stack = new ArrayDeque<>();
		this.stack.push(new StackItem(root));
		
	}
		
	private void printPattern() {
		
		// print frequency
		System.out.printf("%-15s", stack.getFirst().node.getCount());
		
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
				   item.node.getCount() < min
				|| item.node == graph.getRoot()
				|| item.node.getItem() instanceof WildCard
				);
	}
	
	private class StackItem {
		
		public final Node node;
		public final Iterator<Node> iterator;
		
		public StackItem(Node node) {		
			this.node = node;
			this.iterator = node.getOutputNodes().iterator();
		}
		
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		public StackItem next() {
			Node next = this.iterator.next();
			return new StackItem(next); 
		}
	}
}
