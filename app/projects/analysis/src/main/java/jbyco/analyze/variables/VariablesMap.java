package jbyco.analyze.variables;

import java.io.PrintWriter;
import java.util.HashMap;

import jbyco.lib.Utils;

/**
 * A results of {@link VariablesAnalyzer}.
 */
public class VariablesMap {

    /**
     * The item of the map.
     */
    private class Item {

        /** The counter. */
        public int counter = 0;

        /** The number of load instructions. */
        public int load = 0;

        /** The number of store instructions. */
        public int store = 0;

        /** The number of inc instructions. */
        public int other = 0;
    }

    /** The number of variables. */
    protected int nvars = 0;

    /** The number of parameters. */
    protected int nparams = 0;

    /** The map of variables. */
    protected HashMap<Integer, Item> variables = new HashMap<>();

    /** The map of parameters. */
    protected HashMap<Integer, Item> parameters = new HashMap<>();

    /**
     * Adds the method.
     *
     * @param nparams the number of parameters
     * @param nvars the number of variables
     */
    public void addMethod(int nparams, int nvars) {

        this.nvars = nvars;
        this.nparams = nparams;

        for (int var = 0; var < nparams; var++) {

            Item item = getItem(parameters, var);
            item.counter++;
        }

        for (int var = nparams; var < nvars; var++) {

            Item item = getItem(variables, var);
            item.counter++;
        }
    }

    /**
     * Adds the variable.
     *
     * @param key the index
     * @param op the opcode
     */
    public void add(int key, String op) {

        HashMap<Integer, Item> map = (key < nparams) ? parameters : variables;
        Item item = getItem(map, key);

        // update item
        switch(op) {
            case "LOAD" : item.load++; break;
            case "STORE": item.store++; break;
            default     : item.other++; break;
        }
    }

    /**
     * Gets the item.
     *
     * @param map the map
     * @param key the index
     * @return the item
     */
    public Item getItem(HashMap<Integer, Item> map, int key) {

        // get item
        Item item = map.get(key);

        // or create one
        if (item == null) {
            item = new Item();
            map.put(key, item);
        }

        return item;
    }

    /**
     * Write results to the output.
     *
     * @param out the output
     */
    public void write(PrintWriter out) {

        out.println("Parameters:");
        writeMap(out, parameters);

        out.println();

        out.println("Variables:");
        writeMap(out, variables);
    }

    /**
     * Write map to the output.
     *
     * @param out the output
     * @param map the map
     */
    public void writeMap(PrintWriter out, HashMap<Integer, Item> map) {

        // set format
        String format = "%-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n";

        // print header
        out.printf(format,
                "INDEX",
                "COUNT",
                "LOAD",
                "STORE",
                "INC",
                "ALL",
                "AVG_LOAD",
                "AVG_STORE",
                "AVG_INC",
                "AVG_ALL"
        );

        // print items
        for (int key : map.keySet()) {

            // get item
            Item item = map.get(key);

            // calculate sum
            int sum = item.load + item.store + item.other;

            // total variables or parameters
            int total = item.counter;

            // print item
            out.printf(format,
                key,
                item.counter,
                item.load,
                item.store,
                item.other,
                sum,
                Utils.doubleDivToString(item.load, total),
                Utils.doubleDivToString(item.store, total),
                Utils.doubleDivToString(item.other, total),
                Utils.doubleDivToString(sum, total)
            );
        }
    }
}
