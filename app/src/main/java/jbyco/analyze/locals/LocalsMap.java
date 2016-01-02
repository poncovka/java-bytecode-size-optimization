package jbyco.analyze.locals;

import java.util.HashMap;

/* TODO
 * analýza počtu proměných a počtu parametrů
 * 
 */

public class LocalsMap {
	
	// item of the map
	private class Item {
		
		public int load;
		public int store;
		public int other;
		
		public Item() {
			init();
		}
		
		public void init () {
			load = 0;
			store = 0;
			other = 0;
		}
	}

	// map variable->item
	HashMap<Integer, Item> map;
	
	// number of methods
	private int methods;
	
	public LocalsMap() {
		this.map = new HashMap<>();
		this.methods = 0;
	}
	
	public void init() {
		
		for (Item item : map.values()) {
			item.init();
		}
	}
	
	public void addMethod() {
		this.methods++;
	}
	
	public void add(int key, String op) {
		
		// get item
		Item item = map.get(key);
		
		// or create one
		if (item == null) {
			item = new Item();			
			map.put(key, item);
		}
		
		// update item
		switch(op) {
			case "LOAD" : item.load++; break;
			case "STORE": item.store++; break;
			default 	: item.other++; break;
		}		
	}
		
	public void print() {
		
		String format = "%-15s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n";
		System.out.printf(format, 
				"VARIABLE", 
				"LOAD", 
				"STORE", 
				"OTHER", 
				"TOTAL", 
				"AVG LOAD", 
				"AVG STORE", 
				"AVG OTHER", 
				"AVG TOTAL"
		);
		
		for (int key : map.keySet()) {
			Item item = map.get(key);
			System.out.printf(format, 
				key, 
				item.load, 
				item.store, 
				item.other, 
				item.load + item.store + item.other,
				item.load * 1.0 / methods, 
				item.store * 1.0 / methods, 
				item.other * 1.0 / methods, 
				(item.load + item.store + item.other) * 1.0 / methods
			);	
		}
	}
}
