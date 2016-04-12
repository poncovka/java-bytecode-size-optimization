package jbyco.analyze.patterns.labels;

import org.objectweb.asm.Label;

public interface AbstractLabelFactory {

	public void init(Label begin, Label end);
	public AbstractLabel getLabel(Label label);
	
}
