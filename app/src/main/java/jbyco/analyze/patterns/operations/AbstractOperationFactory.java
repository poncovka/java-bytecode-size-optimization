package jbyco.analyze.patterns.operations;

public abstract class AbstractOperationFactory {
	
	// map opcode -> operation
	AbstractOperation[] operationMap = new AbstractOperation[256];
	
	// map handle kind -> operation
	AbstractOperation[] handleMap = new AbstractHandleOperation[10];		
	
	public AbstractOperationFactory() {
		init();
	}
	
	public void init() {
		init(operationMap, allOperations());
		init(handleMap, allHandleOperations());
	};
	
	private void init(AbstractOperation[] map, AbstractOperation[] operations) {
		
		// for all operations
		for (AbstractOperation operation : operations) {
			
			// for all opcodes
			for (int opcode : operation.getOpcodes()) {
				
				// add pairs opcode,operation 
				map[opcode] = operation;
			}
		}	
	}
		
	private AbstractOperation get(AbstractOperation[] map, int opcode) {
		
		// get operation
		AbstractOperation op = map[opcode];
		
		// check if exists
		if (op == null) {
			throw new NullPointerException("Unknown opcode " + opcode + ".");
		}
		
		// return operation
		return op;
	}
	
	public AbstractOperation getOperation(int opcode) {
		return get(operationMap, opcode);
	}
	
	public AbstractHandleOperation getHandleOperation(int tag) {
		return (AbstractHandleOperation) get(handleMap, tag);
	}
	
	// get all operations
	public abstract AbstractOperation[] allOperations();
	
	// get all handle operations
	public abstract AbstractOperation[] allHandleOperations();
	
}
