package jbyco.analyze.patterns.tree;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The representation of the node in a graph.
 * Each node represents item of the type T.
 *
 * @param <T> the generic type of the node
 */
public class Node<T> {

    /** The maximal identifier. */
    static int maxid = 0;

    /** The identifier. */
    int id;

    /** The item represented by the node. */
    T item;

    /** The count of represented items. */
    int counter;

    /** The input node. */
    Node<T> in;

    /** The output nodes. */
    Map<T, Node<T>> out;

    /**
     * Instantiates a new node.
     *
     * @param item the item
     */
    public Node(T item) {

        this.id = maxid++;
        this.item = item;

        if (this.id > maxid) {
            throw new ArithmeticException("Integer overflow.");
        }

        this.counter = 0;
        this.in = null;
        this.out = null;
    }

    static int getTotal() {
        return maxid;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return counter;
    }

    /**
     * Increment count.
     */
    public void incrementCount() {
        counter++;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    /**
     * Adds the input node.
     *
     * @param node the node
     */
    public void addInputNode(Node<T> node) {
        in = node;
    }

    public Node<T> getInputNode() {
        return in;
    }

    /**
     * Removes the input node.
     *
     * @param node the node
     */
    public void removeInputNode(Node<T> node) {
        in = null;
    }

    /**
     * Adds the output node.
     *
     * @param node the node
     */
    public void addOutputNode(Node<T> node) {

        // init the output nodes map
        if (out == null) {
            out =  new HashMap<>(1, 16.0f);
        }

        out.put(node.getItem(), node);
    }

    public Collection<Node<T>> getOutputNodes() {
        if (out == null)     return Collections.emptyList();
        else                return out.values();
    }

    /**
     * Find next node.
     *
     * @param item the item
     * @return the node with the given item
     */
    public Node<T> findNextNode(T item) {
        if (out == null)     return null;
        else                return out.get(item);
    }

    /**
     * Removes the output node.
     *
     * @param node the node
     */
    public void removeOutputNode(Node<T> node) {

        if (out == null) {
            return;
        }

        out.remove(node.getItem());

        // remove the output nodes map
        if (out.isEmpty()) {
            out = null;
        }
    }

    /**
     * Removes the output node.
     *
     * @param i the iterator over output nodes
     */
    public void removeOutputNode(Iterator<Node<T>> i) {

        // remove the current node
        i.remove();

        // remove the output nodes map
        if (out.isEmpty()) {
            out = null;
        }
    }

    /**
     * Adds the edge.
     *
     * @param node the node
     */
    public void addEdge(Node<T> node) {
        this.addOutputNode(node);
        node.addInputNode(this);
    }

    /**
     * Removes the edge.
     *
     * @param node the node
     */
    public void removeEdge(Node<T> node) {
        this.removeOutputNode(node);
        node.removeInputNode(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "(" + id + (item == null ? "" : "," + item.toString()) + ")";
    }

}
