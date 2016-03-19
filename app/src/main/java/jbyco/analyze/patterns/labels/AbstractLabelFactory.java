package jbyco.analyze.patterns.labels;

import org.objectweb.asm.Label;

public interface AbstractLabelFactory {

	public AbstractLabel getLabel(Label label);
	public void restart();
	
}
