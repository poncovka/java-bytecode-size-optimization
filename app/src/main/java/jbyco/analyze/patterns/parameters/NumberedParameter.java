package jbyco.analyze.patterns.parameters;

public class NumberedParameter implements AbstractParameter {

	final ParameterType type;
	final int number;
	
	public NumberedParameter(ParameterType type, int number) {
		this.type = type;
		this.number = number;
	}

	@Override
	public String toString() {
		return type.toString() + "(" + number + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
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
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		NumberedParameter other = (NumberedParameter) obj;
		
		if (number != other.number) {
			return false;
		}
		
		if (type != other.type) {
			return false;
		}
		
		return true;
	}	
}
