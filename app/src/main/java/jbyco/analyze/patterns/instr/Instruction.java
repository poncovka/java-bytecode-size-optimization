package jbyco.analyze.patterns.instr;

import java.util.Arrays;

import jbyco.analyze.patterns.instr.operation.AbstractOperation;
import jbyco.analyze.patterns.instr.param.AbstractParameter;

public class Instruction implements AbstractInstruction {
	
	// abstraction of opcode
	final AbstractOperation operation;
	
	// parameters of the instruction
	final AbstractParameter[] parameters;
	
	public Instruction(AbstractOperation operation, AbstractParameter[] params) {
		this.operation = operation;
		this.parameters = params;
	}
	
	public AbstractOperation getOperation() {
		return operation;
	}
	
	public Object[] getParameters() {
		return parameters;
	}
	
	@Override
	public String toString() {
		
		StringBuffer buffer = new StringBuffer(operation.toString());
		
		for (Object parameter : parameters) {
			buffer.append(' ');
			buffer.append(parameter);
		}
		
		return buffer.toString();
	}

	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((operation == null) ? 0 : operation.hashCode());
		result = prime * result + Arrays.hashCode(parameters);
		
		return result;
	}

	
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
