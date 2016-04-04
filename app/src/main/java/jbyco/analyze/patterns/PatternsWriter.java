package jbyco.analyze.patterns;

import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import jbyco.analyze.patterns.tree.Node;
import jbyco.analyze.patterns.tree.Tree;

public class PatternsWriter {

	static final String NULL_TO_STRING = "*";
	
	Tree graph;
	String delimiter;
	int min;
	int wildcards;
	
	Deque<StackItem> stack;
	PrintWriter out;
	
	
	public PatternsWriter(PrintWriter out) {
		this.out = out;
	}
	
	private void init(Tree graph, String delimiter, int min, int wildcards) {
		
		this.graph = graph;
		this.delimiter = delimiter;
		this.min = min;
		this.wildcards = wildcards;
		
		Node root = this.graph.getRoot();
		
		this.stack = new ArrayDeque<>();
		this.stack.push(new StackItem(root, 0));
		
	}
	
	public void write(Tree graph, String delimiter, int min, int wildcards) {
		
		// init
		init(graph, delimiter, min, wildcards);
		
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
					writePath();
				}

			}
			else {
				
				// or pop head of stack
				stack.pop();
			}
		}
	}
		
	private void writePath() {
		
		// print frequency
		out.printf("%-15s", stack.getFirst().node.getCount());
		
		// for all nodes in a stack
		Iterator<StackItem> iterator = stack.descendingIterator();
		while(iterator.hasNext()) {
			
			// print items in nodes
			StackItem item = iterator.next();
			Node node = item.node; 
			
			if (node != graph.getRoot()) {	
				out.printf("%s%s", getString(node), delimiter);
			}
		}
		
		// new line
		out.println();
	}
	
	private String getString(Node node) {
		
		// get item of the node
		Object obj = node.getItem();
		
		// return string
		return (obj == null) ? NULL_TO_STRING : obj.toString();
	}
	
	private boolean isPrintable(StackItem item) {
		return 
				item.node.getCount() >= min
			&&  item.wildcards == wildcards
			&&  !isRoot(item)
			&&  !isWildCard(item);
	}
	
	private boolean isRoot(StackItem item) {
		return item.node == graph.getRoot();
	}
	
	private boolean isWildCard(StackItem item) {
		return item.node.getItem() == null;
	}
	
	private class StackItem {
		
		public final Node node;
		public final Iterator<Node> iterator;
		public int wildcards;
		
		public StackItem(Node node, int wildcards) {		
			this.node = node;
			this.wildcards = wildcards + (isWildCard(this) ? 1 : 0);	
			this.iterator = node.getOutputNodes().iterator();
		}
		
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		public StackItem next() {
			Node next = this.iterator.next();
			return new StackItem(next, wildcards); 
		}
	}
}
