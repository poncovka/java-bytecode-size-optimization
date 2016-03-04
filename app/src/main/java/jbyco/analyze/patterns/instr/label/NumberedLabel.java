package jbyco.analyze.patterns.instr.label;

import jbyco.analyze.patterns.instr.param.Type;

public class NumberedLabel implements AbstractLabel {

	final int number;
	
	public NumberedLabel(int number) {
		this.number = number;
	}
	
	@Override
	public String toString() {
		return Type.LABEL.toString() + "(" + number + ")";
	}
}
