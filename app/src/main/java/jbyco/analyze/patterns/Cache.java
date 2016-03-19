package jbyco.analyze.patterns;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import java.util.function.Function;

import jbyco.analyze.patterns.instructions.AbstractInstruction;
import jbyco.analyze.patterns.instructions.Instruction;
import jbyco.analyze.patterns.operations.AbstractOperation;
import jbyco.analyze.patterns.parameters.AbstractParameter;

public class Cache {
	
	WeakHashMap<AbstractInstruction, WeakReference<CachedInstruction>> instructions;
	WeakHashMap<AbstractParameter, WeakReference<CachedParameter>> parameters;
	
	public Cache() {
		instructions = new WeakHashMap<>();
		parameters = new WeakHashMap<>();
	}

	public CachedInstruction getCachedInstruction(AbstractInstruction i) {
		return getCachedItem(i, instructions, (x) -> new CachedInstruction(x));
	}
	
	public CachedParameter getCachedParameter(AbstractParameter p) {
		return getCachedItem(p, parameters, (x) -> new CachedParameter(x));
	}
	
	public CachedInstruction createCachedInstruction(AbstractInstruction instruction) {
		
		// create instruction with cached items
		if (instruction instanceof Instruction) {
			
			// get instruction
			Instruction i = (Instruction) instruction;
			AbstractParameter[] p = i.getParameters();
			
			// get operation, no need to cache it, because it's Enum
			AbstractOperation o2 = i.getOperation();
			
			// get cached parameters
			AbstractParameter[] p2 = new AbstractParameter[p.length];
			
			for (int j = 0; j < p.length; j++) {
				p2[j] = getCachedParameter(p[j]);
			}
			
			// create new instruction
			Instruction i2 = new Instruction(o2, p2);
			instruction = i2;
		}
		
		// create cached instruction
		return new CachedInstruction(instruction);
	}

	
	public <T1, T2> T2 getCachedItem(T1 item, WeakHashMap<T1, WeakReference<T2>> map, Function<T1, T2> f) {
				
		// get a reference to cached item
		WeakReference<T2> ref = map.get(item);
		
		// get item
		T2 item2 = (ref != null) ? ref.get() : null;
		
		// no reference
		if (ref == null || item2 == null) {
			
			// create new item
			item2 = f.apply(item);
			
			// put a new reference
			map.put(item, new WeakReference<T2>(item2));
		}

		// return cached item
		return item2;
	}
	
	private class CachedInstruction extends Cached<AbstractInstruction> implements AbstractInstruction {

		public CachedInstruction(AbstractInstruction item) {
			super(item);
		}
	};
	
	private class CachedParameter extends Cached<AbstractParameter> implements AbstractParameter {

		public CachedParameter(AbstractParameter item) {
			super(item);
		}	
	}	
}
