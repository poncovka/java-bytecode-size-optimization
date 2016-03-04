package jbyco.analyze.patterns.instr.param;

import java.util.Arrays;

public class MulticomponentParameter implements AbstractParameter {
	
	final Type type;
	final Object[] components;
		
	public MulticomponentParameter(Type type, Object ...components) {
		this.type = type;
		this.components = components;
	}

	@Override
	public String toString() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(type.toString());
		buffer.append('(');
		
		boolean first = true;
		for(Object component : components) {
			
			if (!first) {
				buffer.append(',');
			}
			else {
				first = false;
			}
			
			buffer.append(component);
		}
		
		buffer.append(')');
		return super.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(components);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MulticomponentParameter)) {
			return false;
		}
		
		MulticomponentParameter other = (MulticomponentParameter) obj;
		
		if (type != other.type) {
			return false;
		}
		if (!Arrays.equals(components, other.components)) {
			return false;
		}
		
		return true;
	}

}
