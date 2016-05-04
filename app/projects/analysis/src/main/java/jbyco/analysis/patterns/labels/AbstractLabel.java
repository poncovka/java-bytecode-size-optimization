package jbyco.analysis.patterns.labels;

import jbyco.analysis.patterns.instructions.AbstractInstruction;
import jbyco.analysis.patterns.parameters.AbstractParameter;

/**
 * The interface for abstractions of labels.
 * <p>
 * Each label represents the address of the following instruction.
 */
public interface AbstractLabel extends AbstractInstruction, AbstractParameter {

}
