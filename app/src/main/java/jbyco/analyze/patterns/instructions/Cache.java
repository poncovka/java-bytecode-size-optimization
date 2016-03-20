package jbyco.analyze.patterns.instructions;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public class Cache {
	
	// map instruction -> cached instruction
	WeakHashMap<AbstractInstruction, WeakReference<CachedInstruction>> map;
	
	public Cache() {
		map = new WeakHashMap<>();
	}
	
	public AbstractInstruction getCachedInstruction(AbstractInstruction i) {
				
		// get a reference to cached item
		WeakReference<CachedInstruction> ref = map.get(i);
		
		// get item
		CachedInstruction i2 = (ref != null) ? ref.get() : null;
		
		// no reference
		if (ref == null || i2 == null) {
			
			// create new item
			i2 = new CachedInstruction(i);
			
			// put a new reference
			map.put(i, new WeakReference<CachedInstruction>(i2));
		}

		// return cached item
		return i2;
	}
	
}
