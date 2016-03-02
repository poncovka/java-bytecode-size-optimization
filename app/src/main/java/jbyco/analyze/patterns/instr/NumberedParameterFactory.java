package jbyco.analyze.patterns.instr;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;

public class NumberedParameterFactory extends GeneralParameterFactory {

	// dictionary objects -> parameters
	Map<Object, Parameter> map = new HashMap<>();
	
	// dictionary of max numbers used in numbered parameters
	Map<Type, Integer> numbers = new EnumMap<>(Type.class);
	
	class NumberedParameter implements Parameter {
		
		public final Type type;
		public int number;
		
		public NumberedParameter(Type type, int number) {
			this.type = type;
			this.number = number;
		}
	}
	
	private Parameter getParameter(Object object, Type type) {
		
		// try to find parameter in a map
		Parameter parameter = map.get(object);
		
		// create new parameter of the given type
		if (parameter == null) {
			
			// get number of the type
			int number = numbers.getOrDefault(type, 0);
			// create new parameter
			parameter = new NumberedParameter(type, number);
			
			// update maps
			numbers.put(type, number + 1);
			map.put(object, parameter);
		}
		
		// return the parameter
		return parameter;
	}
	
	@Override
	public Parameter getLabel(Label label) {
		return getParameter(label, Type.LABEL);
	}

	@Override
	public Parameter getVariable(int index) {
		// TODO does not work
		return getParameter(index, Type.VARIABLE);
	}

	@Override
	public Parameter getInt(int i) {
		return getParameter(i, Type.INT);
	}

	@Override
	public Parameter getFloat(float f) {
		return getParameter(f, Type.FLOAT);
	}

	@Override
	public Parameter getLong(long l) {
		return getParameter(l, Type.LONG);
	}

	@Override
	public Parameter getDouble(double d) {
		return getParameter(d, Type.DOUBLE);
	}

	@Override
	public Parameter getString(String s) {
		return getParameter(s, Type.STRING);
	}

	@Override
	public Parameter getClass(String internalName) {
		// TODO Auto-generated method stub
		return super.getClass(internalName);
	}

	@Override
	public Parameter getField(String name, String desc) {
		// TODO Auto-generated method stub
		return super.getField(name, desc);
	}

	@Override
	public Parameter getMethod(String name, String desc) {
		// TODO Auto-generated method stub
		return super.getMethod(name, desc);
	}

	@Override
	public Parameter getHandle(Handle handle) {
		// TODO Auto-generated method stub
		return super.getHandle(handle);
	}
	
	
	
		
}
