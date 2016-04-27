package jbyco.analyze.patterns.instructions;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * The class to cache abstracted instructions.
 */
public class Cache {
	
	/** 
	 * The map of abstracted instructions mapped to cached instructions. 
	 * The map uses only weak references, therefore objects can be collected
	 * by the garbage collector.
	 */
	WeakHashMap<AbstractInstruction, WeakReference<CachedInstruction>> map;
	
	/**
	 * Instantiates a new cache.
	 */
	public Cache() {
		map = new WeakHashMap<>();
	}
	
	/**
	 * Gets the cached instruction.
	 *
	 * @param i 	the abstracted instruction
	 * @return the cached instruction
	 */
	public AbstractInstruction getCachedInstruction(AbstractInstruction i) {
			
		// check null
		if (i == null) {
			return null;
		}
		
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
