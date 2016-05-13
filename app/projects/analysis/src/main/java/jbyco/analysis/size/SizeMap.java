package jbyco.analysis.size;

import jbyco.lib.Utils;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * The result of analysis in {@link SizeAnalyzer}.
 */
public class SizeMap {

    /**
     * The map of item names mapped to counters.
     */
    Map<String, Item> map;

    /**
     * Instantiates a new size map.
     */
    public SizeMap() {
        this.map = new HashMap<>();
    }

    /**
     * Adds the item.
     *
     * @param key  the name of item
     * @param size the size of item
     */
    public void add(String key, int size) {

        // get item
        Item item = map.get(key);

        // or create one
        if (item == null) {
            item = new Item();
            map.put(key, item);
        }

        // update item
        item.count++;
        item.size += size;
    }

    /**
     * Write the results to the output.
     *
     * @param out the output
     */
    public void write(PrintWriter out) {

        // ordering
        Map<String, Item> map = new TreeMap<>(this.map);

        // format
        String format = "%-40s%-20s%-20s%-20s\n";

        // header
        out.printf(format, "ITEM", "SIZE", "SIZE/COUNT", "SIZE/TOTAL");

        // get file item
        Item file = this.map.get("FILE");

        for (String key : map.keySet()) {
            Item item = map.get(key);
            out.printf(format,
                    key,
                    item.size,
                    Utils.longDivToString(item.size, item.count),
                    Utils.doubleDivToString(item.size, file.size)
            );
        }
    }

    /**
     * The item of the map.
     */
    private class Item {

        /**
         * The count of items.
         */
        public int count = 0;

        /**
         * The size of items.
         */
        public int size = 0;
    }
}


