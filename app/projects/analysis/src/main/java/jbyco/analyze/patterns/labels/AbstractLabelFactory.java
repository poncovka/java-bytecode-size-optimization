package jbyco.analyze.patterns.labels;

import org.objectweb.asm.Label;

/**
 * A abstract factory for creating AbstractLabel objects.
 */
public interface AbstractLabelFactory {

	/**
	 * Initializes the factory.
	 *
	 * @param begin 	the label indicating the begin of the method
	 * @param end 		the label indicating the end of the method
	 */
	public void init(Label begin, Label end);
	
	/**
	 * Gets the label.
	 *
	 * @param label the label from ASM library
	 * @return the abstracted label
	 */
	public AbstractLabel getLabel(Label label);
	
}
