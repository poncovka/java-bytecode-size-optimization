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
	Deque<StackItem> stack;
	
	public void print(SuffixGraph graph, String delimiter) {
		
		// init
		init(graph, delimiter);
		
		// process stack
		while(!stack.isEmpty()) {
			
			// get head
			StackItem item = stack.getFirst();
			
			// if printable, print stack
			if (isPrintable(item)) {
				printPattern();
			}
			
			// get next item
			StackItem next = null;
			while (item.hasNext() && (next = item.next()) == null);
			
			// push item on stack
			if (next != null) {
				stack.push(next);
			}
			// or pop head of stack
			else {
				stack.pop();
			}
		}
	}
	
	private void init(SuffixGraph graph, String delimiter) {
		
		this.graph = graph;
		this.delimiter = delimiter;
		
		Node root = this.graph.getRoot();
		Set<Path> paths = new TreeSet<>();
		
		for (Node node:root.getInputNodes()) {
			paths.addAll(root.getEdgePaths(node));
		}
		
		this.stack = new ArrayDeque<>();
		this.stack.push(new StackItem(root, paths));
		
	}
		
	private void printPattern() {
		
		// print frequency
		int count = getCount(stack.getFirst().paths);
		System.out.printf("%-15s", count);
		
		// for all nodes in a stack
		Iterator<StackItem> iterator = stack.descendingIterator();
		while(iterator.hasNext()) {
			
			// print items in nodes
			StackItem item = iterator.next();
			System.out.printf("%s%s", item.node.getItem(), delimiter);
		}
		
		// new line
		System.out.println();
	}
	
	private int getCount(Set<Path> paths) {
		
		int count = 0;
		for (Path path:paths) {
			count += path.getCount();
		}
		
		return count;
	}
	
	private boolean isPrintable(StackItem item) {
		return !(item.node == graph.getRoot() || item.node.getItem() instanceof WildCard);
	}
	
	private class StackItem {
		
		public final Node node;
		public final Set<Path> paths;
		public final Iterator<Node> iterator;
		
		public StackItem(Node node, Set<Path> paths) {		
			this.node = node;
			this.paths = paths;
			this.iterator = node.getOutputNodes().iterator();
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
