package jbyco.optimization;

import jbyco.lib.Utils;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by vendy on 12.5.16.
 */
public class Statistics {

    Map<String, Integer> peephole;
    long sizeBefore;
    long sizeAfter;

    public Statistics() {
        this.peephole = new HashMap<>();
        int sizeBefore = 0;
        int sizeAfter = 0;
    }

    public void addSizeBefore(int size) {
        sizeBefore += size;
    }

    public void addSizeAfter(int size) {
        sizeAfter += size;
    }

    public void initPepphole(String key) {
        if (!peephole.containsKey(key)) {
            peephole.put(key, 0);
        }
    }

    public void addPepphole(String key) {
        peephole.put(key, peephole.getOrDefault(key, 0) + 1);
        System.err.println(key);
    }

    public void write(PrintWriter out) {
        writePeepholes(out);
        out.println();
        writeSizes(out);
    }

    public void writePeepholes(PrintWriter out) {

        // print peephole optimizations
        Map<String, Integer> peephole = new TreeMap<>(this.peephole);
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
