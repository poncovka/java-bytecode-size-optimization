package jbyco.analyze.patterns.parameters;

import java.util.Arrays;

import jbyco.lib.Utils;

public class FullParameter implements AbstractParameter {
	
	final ParameterType type;
	final Object[] components;
		
	public FullParameter(ParameterType type, Object ...components) {
		this.type = type;
		this.components = components;
	}
	
	public String componentToString(Object component) {
		
		if (type == ParameterType.STRING) {
			return Utils.getEscapedString(component.toString(), "\"");
		}
		else {
			return component.toString();
		}
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
					
			buffer.append(componentToString(component));
		}
		
		buffer.append(')');
		return buffer.toString();
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
		if (!(obj instanceof FullParameter)) {
			return false;
		}
		
		FullParameter other = (FullParameter) obj;
		
		if (type != other.type) {
			return false;
		}
		if (!Arrays.equals(components, other.components)) {
			return false;
		}
		
		return true;
	}

}
