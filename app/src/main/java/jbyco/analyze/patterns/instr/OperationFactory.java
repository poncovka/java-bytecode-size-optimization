package jbyco.analyze.patterns.instr;

public abstract class OperationFactory {
	
	// map opcode->operation
	private Operation[] map = new Operation[256];
			
	public OperationFactory() {
		
		// for all operations
		for (Operation operation : all()) {
			// for all opcodes
			for (int opcode : operation.getOpcodes()) {
				// add pairs opcode,operation 
				add(opcode, operation);
			}
		}
	}
	
	// add new pair, TODO: check opcode
	private void add(int opcode, Operation operation) {
		map[opcode] = operation;
	}
	
	// get operation for given opcode
	public Operation getOperation(int opcode) {
		return map[opcode];
	}
	
	// get all operations
	public abstract Operation[] all();
}
