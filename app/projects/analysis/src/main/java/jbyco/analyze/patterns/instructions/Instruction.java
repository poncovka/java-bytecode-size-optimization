package jbyco.analyze.patterns.instructions;

import java.util.Arrays;

import jbyco.analyze.patterns.operations.AbstractOperation;
import jbyco.analyze.patterns.parameters.AbstractParameter;

/**
 * The representation of abstracted instruction. 
 * Abstract instructions are created by Abstractor.
 */
public class Instruction implements AbstractInstruction {

    /** The abstraction of the operation code. */
    final AbstractOperation operation;

    /** The abstraction of parameters. */
    final AbstractParameter[] parameters;

    /**
     * Instantiates a new instruction.
     *
     * @param operation     the operation
     * @param params         the parameters
     */
    public Instruction(AbstractOperation operation, AbstractParameter[] params) {
        this.operation = operation;
        this.parameters = params;
    }

    public AbstractOperation getOperation() {
        return operation;
    }

    public AbstractParameter[] getParameters() {
        return parameters;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer(operation.toString());

        for (Object parameter : parameters) {
            buffer.append(' ');
            buffer.append(parameter);
        }

        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;

        result = prime * result + ((operation == null) ? 0 : operation.hashCode());
        result = prime * result + Arrays.hashCode(parameters);

        return result;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        // this?
        if (this == obj) {
            return true;
        }

        // null?
        if (obj == null) {
            return false;
        }

        // same class?
        if (getClass() != obj.getClass()) {
            return false;
        }

        Instruction other = (Instruction) obj;

        // same operation?
        if (operation == null) {
            if (other.operation != null) {
                return false;
            }
        } else if (!operation.equals(other.operation)) {
            return false;
        }

        // same parameters?
        if (!Arrays.equals(parameters, other.parameters)) {
            return false;
        }

        // ok
        return true;
    }

}
