package jbyco.analyze.patterns.parameters;

/**
 * The parameter value.
 */
public enum ParameterValue implements AbstractParameter {
	
	/** The representation of null. */
	NULL		("null"),
	
	/** The representation of this. */
	THIS		("this");
	
	/** The abbreviation. */
	private final String abbr;
	
	/**
	 * Instantiates a new parameter value.
	 *
	 * @param abbr the abbreviation
	 */
	ParameterValue(String abbr) {
		this.abbr = abbr;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.abbr;
	}
}
