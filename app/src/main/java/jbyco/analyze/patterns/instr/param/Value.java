package jbyco.analyze.patterns.instr.param;

public enum Value implements AbstractParameter {
	
	NULL		("null"),
	THIS		("this");
	
	private final String abbr;
	
	Value(String abbr) {
		this.abbr = abbr;
	}
	
	@Override
	public String toString() {
		return this.abbr;
	}
}
