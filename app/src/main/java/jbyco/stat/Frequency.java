package jbyco.stat;

import java.util.HashMap;

public class Frequency<K> extends HashMap<K, Integer>{

	private static final long serialVersionUID = 1L;
	
	void increment(K key) {
		put(key, getOrDefault(key, 0) + 1);
	}
	
}
