package jbyco.analyze.patterns.operations;

/**
 * An abstract factory for getting AbstractOperation objects.
 */
public abstract class AbstractOperationFactory {

    /** The map of operation codes mapped to operations. */
    AbstractOperation[] operationMap = new AbstractOperation[256];

    /** The map of handle tags mapped to handle operations. */
    AbstractOperation[] handleMap = new AbstractHandleOperation[10];

    /**
     * Instantiates a new abstract operation factory.
     */
    public AbstractOperationFactory() {
        init();
    }

    /**
     * Initiates the factory.
     */
    public void init() {
        init(operationMap, allOperations());
        init(handleMap, allHandleOperations());
    };

    /**
     * Initiates the map with the given operations.
     *
     * @param map the map
     * @param operations the operations
     */
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

    /**
     * Gets the operation.
     *
     * @param map the map
     * @param opcode the opcode
     * @return the abstract operation
     */
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

    /**
     * Gets the operation.
     *
     * @param opcode the opcode
     * @return the operation
     */
    public AbstractOperation getOperation(int opcode) {
        return get(operationMap, opcode);
    }

    /**
     * Gets the handle operation.
     *
     * @param tag the tag
     * @return the handle operation
     */
    public AbstractHandleOperation getHandleOperation(int tag) {
        return (AbstractHandleOperation) get(handleMap, tag);
    }

    /**
     * Gets all operations.
     *
     * @return the abstract operation[]
     */
    // get all operations
    public abstract AbstractOperation[] allOperations();

    /**
     * Gets all handle operations.
     *
     * @return the abstract operation[]
     */
    // get all handle operations
    public abstract AbstractOperation[] allHandleOperations();

}
