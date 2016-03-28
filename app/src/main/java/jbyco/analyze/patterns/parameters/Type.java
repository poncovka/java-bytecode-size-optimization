package jbyco.analyze.patterns.parameters;

public enum Type implements AbstractParameter {
	
	LABEL 		("LABEL"),
	VARIABLE 	("VAR"),
	PARAMETER	("PAR"),
	INT 		("I"),
	FLOAT 		("F"),
	LONG 		("L"),
	DOUBLE 		("D"),
	STRING 		("S"),
	CLASS		("OBJECT"),
	ARRAY		("ARRAY"),
	FIELD		("FIELD"),
	METHOD		("METHOD");
	
	private final String abbr;
	
	Type(String abbr) {
		this.abbr = abbr;
	}
	
	@Override
	public String toString() {
		return this.abbr;
	}
	
}
