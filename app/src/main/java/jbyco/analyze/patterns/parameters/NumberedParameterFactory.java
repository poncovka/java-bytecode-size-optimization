package jbyco.analyze.patterns.parameters;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class NumberedParameterFactory extends FullParameterFactory {

	// dictionary of created parameters
	Map<AbstractParameter, AbstractParameter> map;
	
	// dictionary of max numbers used in numbered parameters
	Map<Type, Integer> numbers;
	
	public NumberedParameterFactory() {
		init();
	}
	
	@Override
	public void init() {
		map = new HashMap<>();
		numbers = new EnumMap<>(Type.class);
	}
	
	@Override
	public AbstractParameter createParameter(Type type, Object... components) {
		
		// get full parameter
		AbstractParameter p1 = super.createParameter(type, components);
			
		// try to find parameter in a map
		AbstractParameter p2 = map.get(p1);
		
		// create new parameter of the given type
		if (p2 == null) {
			
			// get number of the type
			int number = numbers.getOrDefault(type, 0);
			
			// create new parameter
			p2 = new NumberedParameter(type, number);
			
			// update maps
			numbers.put(type, number + 1);
			map.put(p1, p2);
		}
		
		// return the parameter
		return p2;
	}
	
}
