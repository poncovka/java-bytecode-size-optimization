package jbyco.analyze.patterns.instr.param;

public enum Type implements AbstractParameter {
	
	LABEL 		("LABEL"),
	VARIABLE 	("V"),
	PARAMETER	("P"),
	INT 		("I"),
	FLOAT 		("F"),
	LONG 		("L"),
	DOUBLE 		("D"),
	STRING 		("S"),
	CLASS		("CLASS"),
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
