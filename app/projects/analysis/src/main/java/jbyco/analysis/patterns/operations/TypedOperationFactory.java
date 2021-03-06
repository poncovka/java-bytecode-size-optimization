package jbyco.analysis.patterns.operations;

/**
 * A factory for getting TypedOperation objects.
 */
public class TypedOperationFactory extends AbstractOperationFactory {

    /* (non-Javadoc)
     * @see jbyco.analysis.patterns.operations.AbstractOperationFactory#allOperations()
     */
    public AbstractOperation[] allOperations() {
        return TypedOperation.values();
    }

    /* (non-Javadoc)
     * @see jbyco.analysis.patterns.operations.AbstractOperationFactory#allHandleOperations()
     */
    @Override
    public AbstractOperation[] allHandleOperations() {
        return TypedHandleOperation.values();
    }
}
