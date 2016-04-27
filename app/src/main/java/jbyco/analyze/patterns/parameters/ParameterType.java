package jbyco.analyze.patterns.parameters;

// TODO: Auto-generated Javadoc
/**
 * The parameter type.
 */
public enum ParameterType implements AbstractParameter {
	
	/** The label. */
	LABEL 		("LABEL"),
	
	/** The variable. */
	VARIABLE 	("VAR"),
	
	/** The parameter. */
	PARAMETER	("PAR"),
	
	/** The integer. */
	INT 		("I"),
	
	/** The float. */
	FLOAT 		("F"),
	
	/** The long. */
	LONG 		("L"),
	
	/** The double. */
	DOUBLE 		("D"),
	
	/** The string. */
	STRING 		("S"),
	
	/** The class. */
	CLASS		("OBJECT"),
	
	/** The array. */
	ARRAY		("ARRAY"),
	
	/** The field. */
	FIELD		("FIELD"),
	
	/** The method. */
	METHOD		("METHOD");
	
	/** The abbreviation of the type. */
	private final String abbr;
	
	/**
	 * Instantiates a new parameter type.
	 *
	 * @param abbr the abbreviation
	 */
	ParameterType(String abbr) {
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
