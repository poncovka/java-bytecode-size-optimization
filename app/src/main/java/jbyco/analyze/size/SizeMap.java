package jbyco.analyze.size;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import jbyco.lib.Utils;

public class SizeMap  {

	// item of the map
	private class Item {
		public int count = 0;
		public int size = 0;
	}

	// map string->item
	Map<String, Item> map;
	
	public SizeMap() {
		this.map = new HashMap<>();
	}
	
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
		
	public void write(PrintWriter out) {
		
		// ordering
		Map<String, Item> map = new TreeMap<>(this.map);
		
		// format
		String format = "%-50s %-20s %-20s %-20s\n";
		
		// header
		out.printf(format, "KEY", "SIZE", "SIZE/COUNT", "SIZE/TOTAL");
		
		// get file item
		Item file = this.map.get("FILE");
		
		for (String key : map.keySet()) {
			Item item = map.get(key);
			out.printf(format, 
				key, 
				item.size, 
				Utils.intDivToString(item.size, item.count),
				Utils.doubleDivToString(item.size, file.size)
			);	
		}
	}
}


