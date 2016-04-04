package jbyco.analyze.patterns.labels;

import jbyco.analyze.patterns.parameters.ParameterType;

public class NumberedLabel implements AbstractLabel {

	final int number;
	
	public NumberedLabel(int number) {
		this.number = number;
	}
	
	@Override
	public String toString() {
		return ParameterType.LABEL.toString() + "(" + number + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
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
		NumberedLabel other = (NumberedLabel) obj;
		if (number != other.number) {
			return false;
		}
		return true;
	}
	
	
}
