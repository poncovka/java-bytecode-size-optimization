package jbyco.optimization;

import jbyco.lib.Utils;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by vendy on 12.5.16.
 */
public class Statistics {

    Map<String, Integer> peephole;
    int sizeBefore;
    int sizeAfter;

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

    public void addPepphole(String key) {
        peephole.put(key, peephole.getOrDefault(key, 0) + 1);
    }

    public void write(PrintWriter out) {
        writePeepholes(out);
        out.println();
        writeSizes(out);
    }

    public void writePeepholes(PrintWriter out) {

        // print peephole optimizations
        Map<String, Integer> peephole = new TreeMap<>(this.peephole);
        String format = "%-40s%-20s%\n";
        out.printf(format, "METHOD", "COUNT");

        for (String key : peephole.keySet()) {
            int count = peephole.get(key);
            out.printf(format, key, count);
        }
    }

    public void writeSizes(PrintWriter out) {

        // print sizes
        String format = "%-20s%-20s\n";
        out.printf(format, "Size before:", sizeBefore);
        out.printf(format, "Size after:", sizeAfter);
        out.printf(format, "Difference in bytes:", sizeBefore - sizeAfter);
        out.printf(format, "Difference in percent:", Utils.intDivToString((sizeBefore - sizeAfter) * 100, sizeBefore));

    }
}
