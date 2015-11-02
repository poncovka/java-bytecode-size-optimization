package jbyco.stat;

import java.util.Map;

public abstract class Remapper<K1, V1, K2, V2> {
	
	// kalkulate new value from old key and his value
	abstract public V2 remapValue(K1 key1, V1 value1);

	// kalkulate new key from old key and his value
	abstract public K2 remapKey(K1 key1, V1 value1);
	
	
	public Map<K2, V2> remap(Map<K1, V1> map1, Map<K2, V2> map2) {
		
		// remap map1 to map2
		for(K1 key1 : map1.keySet()) {
			
			// remap every element
			V1 value1 = map1.get(key1);
			map2.put(remapKey(key1, value1), remapValue(key1, value1));
			
		}
		
		return map2;
	}
	
	
}
