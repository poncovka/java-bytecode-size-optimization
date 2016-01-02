package jbyco.analyze.locals;

import java.util.HashMap;

import jbyco.lib.Utils;

/* TODO
 * analýza počtu proměných a počtu parametrů
 * 
 */

public class LocalsMap {
	
	// item of the map
	private class Item {
		public int variables = 0;
		public int parameters = 0;
		public int load = 0;
		public int store = 0;
		public int other = 0;
	}
	
	// map int->item
	protected HashMap<Integer, Item> map = new HashMap<>();
	
	// initialize map
	public void init() {
		
		for (Item item : map.values()) {
			item.variables = 0;
			item.parameters = 0;
			item.load = 0;
			item.store = 0;
			item.other = 0;
		}
	}
	
	public void addMethod(int parameters, int locals) {
		
		for (int var = 0; var < parameters; var++) {
			
			Item item = getItem(var);
			item.parameters++;
		}
		
		for (int var = parameters; var < locals; var++) {
			
			Item item = getItem(var);
			item.variables++;
		}
	}
	
	public void add(int key, String op) {
		
		Item item = getItem(key);
		
		// update item
		switch(op) {
			case "LOAD" : item.load++; break;
			case "STORE": item.store++; break;
			default 	: item.other++; break;
		}		
	}
	
	public Item getItem(int key) {
		
		// get item
		Item item = map.get(key);
				
		// or create one
		if (item == null) {
			item = new Item();			
			map.put(key, item);
		}
		
		return item;
	}
		
	public void print() {
		
		String format = "%-15s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n";
		System.out.printf(format, 
				"VARIABLE",
				"IS PARAM",
				"IS LOCAL",
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
			
			// get item
			Item item = map.get(key);
			
			// calculate sum
			int sum = item.load + item.store + item.other;
			
			int total = item.variables + item.parameters;
			
			// print item
			System.out.printf(format, 
				key, 
				item.parameters,
				item.variables,
				item.load, 
				item.store, 
				item.other, 
				sum,
				Utils.intDivToString(item.load, total),
				Utils.intDivToString(item.store, total),
				Utils.intDivToString(item.other, total),
				Utils.intDivToString(sum, total)
			);	
		}
	}
}
