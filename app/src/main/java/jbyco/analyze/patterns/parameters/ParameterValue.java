package jbyco.analyze.patterns.parameters;

public enum ParameterValue implements AbstractParameter {
	
	NULL		("null"),
	THIS		("this");
	
	private final String abbr;
	
	ParameterValue(String abbr) {
		this.abbr = abbr;
	}
	
	@Override
	public String toString() {
		return this.abbr;
	}
}
