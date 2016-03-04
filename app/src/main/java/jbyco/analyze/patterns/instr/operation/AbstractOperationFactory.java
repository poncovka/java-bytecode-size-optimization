package jbyco.analyze.patterns.instr.operation;

public abstract class AbstractOperationFactory {
	
	// map opcode->operation
	AbstractOperation[] map = new AbstractOperation[256];
			
	public AbstractOperationFactory() {
		
		// for all operations
		for (AbstractOperation operation : all()) {
			// for all opcodes
			for (int opcode : operation.getOpcodes()) {
				// add pairs opcode,operation 
				add(opcode, operation);
			}
		}
	}
	
	// add new pair, TODO: check opcode
	private void add(int opcode, AbstractOperation operation) {
		map[opcode] = operation;
	}
	
	// get operation for given opcode
	public AbstractOperation getOperation(int opcode) {
		return map[opcode];
	}
	
	// get all operations
	public abstract AbstractOperation[] all();
}
