package jbyco.analysis.patterns.tree;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

/**
 * A class for building the graph of type T.
 *
 * @param <T> the generic type
 */
public class TreeBuilder<T> {

    /**
     * The built graph.
     */
    Tree<T> graph;

    /**
     * Instantiates a new tree builder.
     *
     * @param graph the graph
     */
    public TreeBuilder(Tree<T> graph) {
        this.graph = graph;
    }

    /**
     * Adds the collection of items.
     *
     * @param items the items
     */
    public void addPath(Collection<T> items) {

        // init
        Node<T> node = graph.getRoot();
        node.incrementCount();

        // add all items
        for (T item : items) {
            node = addNextNode(node, item);
        }
    }

    /**
     * Adds the next node.
     *
     * @param node the parent node
     * @param item the item of the next node
     * @return the next node
     */
    public Node<T> addNextNode(Node<T> node, T item) {

        // try to find node in neighbors of previous node
        Node<T> next = node.findNextNode(item);

        if (next == null) {

            // create node
            next = new Node<T>(item);

            // create edge
            node.addEdge(next);
        }

        // update counter
        next.incrementCount();

        // return next node
        return next;
    }

    /**
     * Prune the tree.
     *
     * @param threshold the threshold
     */
    public void pruneTree(int threshold) {

        // init stack, the root cannot be removed
        Stack<Node<T>> stack = new Stack<>();
        stack.push(graph.getRoot());

        // walk the tree
        while (!stack.isEmpty()) {

            // get the node
            Node<T> node = stack.pop();

            Iterator<Node<T>> i = node.getOutputNodes().iterator();

            // process next nodes
            while (i.hasNext()) {

                Node<T> next = i.next();

                // should we prune the next node?
                if (next.getCount() <= threshold) {
                    node.removeOutputNode(i);
                    next.removeInputNode(node);
                }
                // or add node to the stack
                else {
                    stack.push(next);
                }
            }
        }
    }
}
