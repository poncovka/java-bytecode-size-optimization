package jbyco.analyze.patterns.instr.label;

import org.objectweb.asm.Label;

public interface AbstractLabelFactory {

	public AbstractLabel getLabel(Label label);
	
}
