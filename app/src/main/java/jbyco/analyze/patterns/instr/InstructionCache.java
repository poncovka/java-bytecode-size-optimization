package jbyco.analyze.patterns.instr;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public class InstructionCache {

	WeakHashMap<AbstractInstruction, WeakReference<AbstractInstruction>> map;
	
	public InstructionCache() {
		map = new WeakHashMap<>();
	}
	
	public AbstractInstruction get(AbstractInstruction i) {
		
		// get a reference to cached instruction
		WeakReference<AbstractInstruction> ref = map.get(i);
		
		// get instruction
		AbstractInstruction i2 = (ref != null) ? ref.get() : null;
		
		// no reference
		if (ref == null || i2 == null) {
			
			// create new instruction
			i2 = new CachedInstruction(i);
			
			// put a new reference
			map.put(i, new WeakReference<AbstractInstruction>(i2));
		}

		// return cached instruction
		return i2;
	}
	
}
