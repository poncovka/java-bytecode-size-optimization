package jbyco.analyze.statistics;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import jbyco.lib.Utils;

/**
 * The data collected by {@link StatisticsCollector}.
 */
public class StatisticsMap {

	/**
	 * The item of the map.
	 */
	private class Item {
		
		/** The count of items. */
		public int count = 0;
		
		/** The total of items. */
		public int total = 0;
	}
	
	/** The map of names of items mapped to counters. */
	Map<String, Item> map;
	
	/**
	 * Instantiates a new statistics map.
	 */
	public StatisticsMap() {
		this.map = new HashMap<>();
	}
	
	/**
	 * Adds the item to the map.
	 *
	 * @param key the name of the item
	 * @param value the value of the item
	 */
	public void add(String key, int value) {
		
		Item item = map.get(key);
		
		if (item == null) {
			item = new Item();
		}
		
		item.count++;
		item.total+= value;
		
		map.put(key, item);
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
		String format = "%-40s%-20s%-20s\n";
				
		// header
		out.printf(format, "ITEM", "COUNT", "COUNT/FILES");
				
		for (String key : map.keySet()) {
			Item item = map.get(key);
			out.printf(format, 
				key, 
				item.total,
				Utils.intDivToString(item.total, item.count)
			);	
		}
	}
}
