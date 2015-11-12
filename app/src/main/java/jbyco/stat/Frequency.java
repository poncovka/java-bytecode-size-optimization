package jbyco.stat;

import java.util.HashMap;
import java.util.Map;

import jbyco.lib.Remapper;

public class Frequency<K> extends HashMap<K, Integer>{

	private static final long serialVersionUID = 1L;
	private int total;
	
	void increment(K key) {
		put(key, getOrDefault(key, 0) + 1);
		total++;
	}
	
	public int getTotal() {
		return total;
	}
	
	public Map<K, Double> getProportionMap() {

		// create remapper
		Remapper<K, Integer, K, Double> remapper = 
			new Remapper<K, Integer, K, Double>() {

			@Override
			public Double remapValue(K key, Integer value) {
				return value * 100.0 / total;
			}

			@Override
			public K remapKey(K key, Integer value) {
				return key;
			}
			
		};
		
		// remap and return
		return remapper.remap(this);
	}
	
}
