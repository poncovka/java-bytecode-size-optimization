package jbyco.analyze.patterns;

import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import jbyco.analyze.patterns.instructions.AbstractInstruction;
import jbyco.analyze.patterns.tree.Node;
import jbyco.analyze.patterns.tree.Tree;

/**
 * The class for printing the results of {@link PatternsAnalyzer}.
 * <p>
 * The graph is searched depth-first using a stack. The nodes on the stack 
 * create a path from the root node. If the current path is printable, 
 * the class prints the frequency and the content of the nodes on the path
 * separated by the given delimiter.
 */
public class PatternsWriter {

	/** The string representing wild cards. */
	static final String NULL_TO_STRING = "*";
	
	/** The graph of instruction sequences. */
	Tree<AbstractInstruction> graph;
	
	/** The delimiter between instructions. */
	String delimiter;
	
	/** The minimal frequency of the pattern. */
	int min;
	
	/** The number of wild cards in a pattern. */
	int wildcards;
	
	/** The stack of {@link StackItem}. */
	Deque<StackItem> stack;
	
	/** The output. */
	PrintWriter out;
	
	/**
	 * Instantiates a new patterns writer.
	 *
	 * @param out the output
	 */
	public PatternsWriter(PrintWriter out) {
		this.out = out;
	}
	
	/**
	 * Initializes the patterns writes.
	 *
	 * @param graph 		the graph of instruction sequences
	 * @param delimiter 	the delimiter between instructions
	 * @param min 			the minimal frequency of a pattern
	 * @param wildcards 	the number of wild cards in a pattern
	 */
	private void init(Tree<AbstractInstruction> graph, String delimiter, int min, int wildcards) {
		
		this.graph = graph;
		this.delimiter = delimiter;
		this.min = min;
		this.wildcards = wildcards;
		
		this.stack = new ArrayDeque<>();
		this.stack.push(new StackItem(this.graph.getRoot(), 0, 0));
	}
	
	/**
	 * Initializes the patterns writer and write the patterns from a graph to the output.
	 *
	 * @param graph 		the graph of instruction sequences
	 * @param delimiter 	the delimiter between instructions
	 * @param min 			the minimal frequency of a pattern
	 * @param wildcards 	the number of wild cards in a pattern
	 */
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
		
	/**
	 * Write the current path.
	 */
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
	
	/**
	 * Gets the string representation of the graph node.
	 *
	 * @param node 	the graph node
	 * @return the string representation of the graph
	 */
	private String getString(Node<AbstractInstruction> node) {
		
		// get item of the node
		Object obj = node.getItem();
		
		// return string
		return (obj == null) ? NULL_TO_STRING : obj.toString();
	}
	
	/**
	 * Checks if the item is printable.
	 *
	 * @param item the item on the stack
	 * @return true, if it is printable
	 */
	private boolean isPrintable(StackItem item) {
		return 
				item.node.getCount() >= min
			&&  item.wildcards == wildcards
			&&  !isRoot(item)
			&&  !isWildCard(item);
	}
	
	/**
	 * Checks if the item has the root node.
	 *
	 * @param item the item on the stack
	 * @return true, if it is root
	 */
	private boolean isRoot(StackItem item) {
		return item.node == graph.getRoot();
	}
	
	/**
	 * Checks if the item has a wild card.
	 *
	 * @param item the item on the stack
	 * @return true, if it is wild card
	 */
	private boolean isWildCard(StackItem item) {
		return !isRoot(item) && item.node.getItem() == null;
	}
	
	/**
	 * The class for walking the graph.
	 */
	private class StackItem {
		
		/** The depth of the node. */
		public int depth;
		
		/** The number of wild cards between the root and the node. */
		public int wildcards;
		
		/** The node of the graph. */
		public final Node<AbstractInstruction> node;
		
		/** The iterator over neighbours. */
		public final Iterator<Node<AbstractInstruction>> iterator;
		
		/**
		 * Instantiates a new stack item.
		 *
		 * @param node the node of the graphs
		 * @param depth the depth of the node
		 * @param wildcards number of wild cards on the path to the root
		 */
		public StackItem(Node<AbstractInstruction> node, int depth, int wildcards) {		
			this.node = node;
			this.depth = depth;
			this.wildcards = wildcards + (isWildCard(this) ? 1 : 0);	
			this.iterator = node.getOutputNodes().iterator();
		}
		
		/**
		 * Checks for next node.
		 *
		 * @return true, if successful
		 */
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		/**
		 * Returns a new stack item with the next node.
		 *
		 * @return the stack item
		 */
		public StackItem next() {
			Node<AbstractInstruction> next = this.iterator.next();
			return new StackItem(next, depth + 1, wildcards); 
		}
	}
}
