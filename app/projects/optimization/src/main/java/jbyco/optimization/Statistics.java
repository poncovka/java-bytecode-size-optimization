package jbyco.optimization;

import jbyco.lib.Utils;

import java.io.PrintWriter;
import java.util.*;

/**
 * A class for collecting statistics about optimizations.
 */
public class Statistics {

    long sizeBefore;
    long sizeAfter;

    Map<String, Integer> optimizations;

    public Statistics() {
        int sizeBefore = 0;
        int sizeAfter = 0;
        this.optimizations = new HashMap<>();
    }

    public void addSizeBefore(int size) {
        sizeBefore += size;
    }

    public void addSizeAfter(int size) {
        sizeAfter += size;
    }

    public void initOptimization(String key) {
        if (!optimizations.containsKey(key)) {
            optimizations.put(key, 0);
        }
    }

    public void addOptimization(String key) {
        optimizations.put(key, optimizations.getOrDefault(key, 0) + 1);
    }

    public void write(PrintWriter out) {
        writeOptimizations(out);
        out.println();
        writeSizes(out);
    }

    public void writeOptimizations(PrintWriter out) {

        // print actions optimizations
        Map<String, Integer> peephole = new TreeMap<>(this.optimizations);
        String format = "%-20s%-40s\n";
        out.printf(format, "COUNT", "METHOD");

        for (String key : peephole.keySet()) {
            int count = peephole.get(key);
            out.printf(format, Integer.toString(count), key);
        }
    }

    public void writeSizes(PrintWriter out) {

        // print sizes
        String format = "%-20s%-20s\n";
        out.printf(format, "Size before:", Long.toString(sizeBefore));
        out.printf(format, "Size after:", Long.toString(sizeAfter));
        out.printf(format, "Diff in bytes:", Long.toString(sizeBefore - sizeAfter));
        out.printf(format, "Diff in percent:", Utils.doubleDivToString((sizeBefore - sizeAfter) * 100L, sizeBefore) + "%");

    }
}
