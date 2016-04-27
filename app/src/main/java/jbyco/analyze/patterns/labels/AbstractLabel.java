package jbyco.analyze.patterns.labels;

import jbyco.analyze.patterns.instructions.AbstractInstruction;
import jbyco.analyze.patterns.parameters.AbstractParameter;

/**
 * The interface for abstractions of labels.
 * <p>
 * Each label represents the address of the following instruction.
 */
public interface AbstractLabel extends AbstractInstruction, AbstractParameter {

}
