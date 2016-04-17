package jbyco.analyze.variables;

import java.io.PrintWriter;
import java.util.HashMap;

import jbyco.lib.Utils;

/* TODO
 * analýza počtu proměných a počtu parametrů
 * 
 */

public class VariablesMap {
	
	// item of the map
	private class Item {
		public int counter = 0;
		public int load = 0;
		public int store = 0;
		public int other = 0;
	}
	
	protected int nvars = 0;
	protected int nparams = 0;
	
	// map int->item
	protected HashMap<Integer, Item> variables = new HashMap<>();
	protected HashMap<Integer, Item> parameters = new HashMap<>();
	
	public void addMethod(int nparams, int nvars) {
		
		this.nvars = nvars;
		this.nparams = nparams;
		
		for (int var = 0; var < nparams; var++) {
			
			Item item = getItem(parameters, var);
			item.counter++;
		}
		
		for (int var = nparams; var < nvars; var++) {
			
			Item item = getItem(variables, var);
			item.counter++;
		}
	}
	
	public void add(int key, String op) {
		
		HashMap<Integer, Item> map = (key < nparams) ? parameters : variables;
		Item item = getItem(map, key);
		
		// update item
		switch(op) {
			case "LOAD" : item.load++; break;
			case "STORE": item.store++; break;
			default 	: item.other++; break;
		}		
	}
	
	public Item getItem(HashMap<Integer, Item> map, int key) {
		
		// get item
		Item item = map.get(key);
				
		// or create one
		if (item == null) {
			item = new Item();			
			map.put(key, item);
		}
		
		return item;
	}
		
	public void write(PrintWriter out) {
		
		out.println("Parameters:");
		writeMap(out, parameters);
		
		out.println();
		
		out.println("Variables:");
		writeMap(out, variables);
	}
	
	public void writeMap(PrintWriter out, HashMap<Integer, Item> map) {
		
		// set format
		String format = "%-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n";
		
		// print header
		out.printf(format, 
				"INDEX",
				"COUNT",
				"LOAD", 
				"STORE", 
				"INC", 
				"ALL", 
				"AVG_LOAD", 
				"AVG_STORE", 
				"AVG_INC", 
				"AVG_ALL"
		);
		
		// print items
		for (int key : map.keySet()) {
			
			// get item
			Item item = map.get(key);
			
			// calculate sum
			int sum = item.load + item.store + item.other;
			
			// total variables or parameters
			int total = item.counter;
			
			// print item
			out.printf(format, 
				key, 
				item.counter,
				item.load, 
				item.store, 
				item.other, 
				sum,
				Utils.doubleDivToString(item.load, total),
				Utils.doubleDivToString(item.store, total),
				Utils.doubleDivToString(item.other, total),
				Utils.doubleDivToString(sum, total)
			);	
		}
	}
}
