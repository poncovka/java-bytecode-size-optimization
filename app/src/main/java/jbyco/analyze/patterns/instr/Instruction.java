package jbyco.analyze.patterns.instr;

import java.util.Arrays;

public class Instruction {
	
	// abstraction of opcode
	final Operation operation;
	
	// parameters of the instruction
	final Object[] parameters;
	
	public Instruction(Operation operation, Object[] params) {
		this.operation = operation;
		this.parameters = params;
	}
	
	public Operation getOperation() {
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
			buffer.append(parameter.toString());
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
