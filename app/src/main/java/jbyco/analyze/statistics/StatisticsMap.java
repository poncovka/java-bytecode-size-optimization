package jbyco.analyze.statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import jbyco.lib.Utils;

public class StatisticsMap {

	// item of the map
	private class Item {
		public int count = 0;
		public int total = 0;
	}
	
	// map
	Map<String, Item> map;
	
	public StatisticsMap() {
		this.map = new HashMap<>();
	}
	
	public void add(String key, int value) {
		
		Item item = map.get(key);
		
		if (item == null) {
			item = new Item();
		}
		
		item.count++;
		item.total+= value;
		
		map.put(key, item);
	}
	
	public void print() {
		
		// ordering
		Map<String, Item> map = new TreeMap<>(this.map);
		
		// format
		String format = "%-50s %-20s %-20s\n";
				
		// header
		System.out.printf(format, "KEY", "TOTAL", "AVERAGE");
				
		for (String key : map.keySet()) {
			Item item = map.get(key);
			System.out.printf(format, 
				key, 
				item.total,
				Utils.intDivToString(item.total, item.count)
			);	
		}
	}
}
