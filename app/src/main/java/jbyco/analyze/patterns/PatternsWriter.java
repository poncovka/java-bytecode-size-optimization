package jbyco.analyze.patterns;

import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import jbyco.analyze.patterns.instructions.AbstractInstruction;
import jbyco.analyze.patterns.tree.Node;
import jbyco.analyze.patterns.tree.Tree;

public class PatternsWriter {

	static final String NULL_TO_STRING = "*";
	
	Tree<AbstractInstruction> graph;
	String delimiter;
	int min;
	int wildcards;
	
	Deque<StackItem> stack;
	PrintWriter out;
	
	
	public PatternsWriter(PrintWriter out) {
		this.out = out;
	}
	
	private void init(Tree<AbstractInstruction> graph, String delimiter, int min, int wildcards) {
		
		this.graph = graph;
		this.delimiter = delimiter;
		this.min = min;
		this.wildcards = wildcards;
		
		this.stack = new ArrayDeque<>();
		this.stack.push(new StackItem(this.graph.getRoot(), 0, 0));
	}
	
	public void write(Tree<AbstractInstruction> graph, String delimiter, int min, int wildcards) {
		
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
		out.printf("%s\t", stack.getFirst().node.getCount());
		
		// print depth
		out.printf("%s\t", stack.getFirst().depth);
		
		// for all nodes in a stack
		Iterator<StackItem> iterator = stack.descendingIterator();
		while(iterator.hasNext()) {
			
			// print items in nodes
			StackItem item = iterator.next(); 
			
			if (!isRoot(item)) {	
				Node<AbstractInstruction> node = item.node;
				out.printf("%s%s", getString(node), delimiter);
			}
		}
		
		// new line
		out.println();
	}
	
	private String getString(Node<AbstractInstruction> node) {
		
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
		return !isRoot(item) && item.node.getItem() == null;
	}
	
	private class StackItem {
		
		public int depth;
		public int wildcards;
		public final Node<AbstractInstruction> node;
		public final Iterator<Node<AbstractInstruction>> iterator;
		
		public StackItem(Node<AbstractInstruction> node, int depth, int wildcards) {		
			this.node = node;
			this.depth = depth;
			this.wildcards = wildcards + (isWildCard(this) ? 1 : 0);	
			this.iterator = node.getOutputNodes().iterator();
		}
		
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		public StackItem next() {
			Node<AbstractInstruction> next = this.iterator.next();
			return new StackItem(next, depth + 1, wildcards); 
		}
	}
}
