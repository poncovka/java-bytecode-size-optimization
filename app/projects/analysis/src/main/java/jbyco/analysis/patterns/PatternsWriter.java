package jbyco.analysis.patterns;

import jbyco.analysis.patterns.instructions.AbstractInstruction;
import jbyco.analysis.patterns.tree.Node;
import jbyco.analysis.patterns.tree.Tree;
import jbyco.lib.Utils;

import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * The class for printing the results of {@link PatternsAnalyzer}.
 * <p>
 * The graph is searched depth-first using a stack. The nodes on the stack
 * create a path from the root node. If the current path is printable,
 * the class prints the frequency and the content of the nodes on the path
 * separated by the given delimiter.
 */
public class PatternsWriter {

    /**
     * The string representing wild cards.
     */
    static final String NULL_TO_STRING = "*";

    /**
     * The graph of instruction sequences.
     */
    Tree<AbstractInstruction> graph;

    /**
     * The number of processed instructions.
     */
    long total;

    /**
     * The minimal frequency of the pattern.
     */
    int min;

    /**
     * The number of wild cards in a pattern.
     */
    int wildcards;


    /**
     * The delimiter between instructions.
     */
    String delimiter;

    /**
     * The stack of {@link StackItem}.
     */
    Deque<StackItem> stack;

    /**
     * The output.
     */
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
     * @param graph     the graph of instruction sequences
     * @param total     the number of processed instructions
     * @param min       the minimal frequency of a pattern
     * @param wildcards the number of wild cards in a pattern
     * @param delimiter the delimiter between instructions
     */
    private void init(Tree<AbstractInstruction> graph, long total, int min, int wildcards, String delimiter) {

        this.graph = graph;
        this.total = total;
        this.min = min;
        this.wildcards = wildcards;
        this.delimiter = delimiter;

        this.stack = new ArrayDeque<>();
        this.stack.push(new StackItem(this.graph.getRoot(), 0, 0));
    }

    /**
     * Initializes the patterns writer and write the patterns from a graph to the output.
     *
     * @param graph     the graph of instruction sequences
     * @param total     the number of processed instructions
     * @param min       the minimal frequency of a pattern
     * @param wildcards the number of wild cards in a pattern
     * @param delimiter the delimiter between instructions
     */
    public void write(Tree<AbstractInstruction> graph, long total, int min, int wildcards, String delimiter) {

        // init
        init(graph, total, min, wildcards, delimiter);

        out.printf("%s\t%s\t%s\t%s\n", "LEN", "FREQ", "REL", "PATTERN");

        // process stack
        while (!stack.isEmpty()) {

            // get head
            StackItem item = stack.getFirst();

            // get next item
            StackItem next = null;
            while (item.hasNext() && (next = item.next()) == null) ;

            if (next != null) {

                // push item on stack
                stack.push(next);

                // if printable, print stack
                if (isPrintable(next)) {
                    writePath();
                }

            } else {
                // or pop head of stack
                stack.pop();
            }
        }
    }

    /**
     * Write the current path.
     */
    private void writePath() {

        StackItem item = stack.getFirst();

        // print depth
        out.printf("%s\t", item.depth);

        // print frequency
        out.printf("%s\t", item.node.getCount());

        // print relative frequency
        out.printf("%s\t", Utils.doubleDivToString(item.node.getCount() * item.depth * 100, total, "#0.00000"));

        // for all nodes in a stack
        Iterator<StackItem> iterator = stack.descendingIterator();
        while (iterator.hasNext()) {

            // print items in nodes
            StackItem next = iterator.next();

            if (!isRoot(next)) {
                out.printf("%s%s", getString(next.node), delimiter);
            }
        }

        // new line
        out.println();
    }

    /**
     * Gets the string representation of the graph node.
     *
     * @param node the graph node
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
                        && item.wildcards == wildcards
                        && !isRoot(item)
                        && !isWildCard(item);
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

        /**
         * The node of the graph.
         */
        public final Node<AbstractInstruction> node;
        /**
         * The iterator over neighbours.
         */
        public final Iterator<Node<AbstractInstruction>> iterator;
        /**
         * The depth of the node.
         */
        public int depth;
        /**
         * The number of wild cards between the root and the node.
         */
        public int wildcards;

        /**
         * Instantiates a new stack item.
         *
         * @param node      the node of the graphs
         * @param depth     the depth of the node
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
