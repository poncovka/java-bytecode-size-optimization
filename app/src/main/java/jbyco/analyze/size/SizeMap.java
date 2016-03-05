package jbyco.analyze.size;

import java.util.HashMap;

import jbyco.lib.Utils;

public class SizeMap  {

	// item of the map
	private class Item {
		
		public int count;
		public int realSize;
		
		public Item() {
			init();
		}
		
		public void init () {
			count = 0;
			realSize = 0;
		}
	}

	// map string->item
	HashMap<String, Item> map;
	
	public SizeMap() {
		this.map = new HashMap<>();
	}
	
	public void init() {
		
		for (Item item : map.values()) {
			item.init();
		}
	}
	
	public void add(String key, int realSize) {
		
		// get item
		Item item = map.get(key);
		
		// or create one
		if (item == null) {
			item = new Item();			
			map.put(key, item);
		}
		
		// update item
		item.count++;
		item.realSize += realSize;
	}
		
	public void print() {
		
		String format = "%-50s %-20s %-20s %-20s\n";
		System.out.printf(format, "KEY", "COUNT", "REAL SIZE", "AVG REAL");		
		for (String key : map.keySet()) {
			Item item = map.get(key);
			System.out.printf(format, 
				key, 
				item.count, 
				item.realSize, 
				Utils.intDivToString(item.realSize, item.count)
			);	
		}
	}
}


