package jbyco.analyze.patterns.labels;

import org.objectweb.asm.Label;

public interface AbstractLabelFactory {

	public void init();
	public AbstractLabel getLabel(Label label);
	
}
