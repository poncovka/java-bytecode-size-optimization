package jbyco.analyze.patterns.labels;

import jbyco.analyze.patterns.parameters.ParameterType;

/**
 * The representation of the numbered label.
 * <p>
 * Each label is distinguished by its number.
 * Numbered labels are created in {@link RelativeLabelFactory}.
 */
public class NumberedLabel implements AbstractLabel {

	/** The number of the label. */
	final int number;
	
	/**
	 * Instantiates a new numbered label.
	 *
	 * @param number the number of the label
	 */
	public NumberedLabel(int number) {
		this.number = number;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ParameterType.LABEL.toString() + "(" + number + ")";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
