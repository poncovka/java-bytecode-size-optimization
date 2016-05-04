package jbyco.analysis.patterns.operations;

/**
 * A factory for getting GeneralOperation objects.
 */
public class GeneralOperationFactory extends AbstractOperationFactory {

    /* (non-Javadoc)
     * @see jbyco.analysis.patterns.operations.AbstractOperationFactory#allOperations()
     */
    @Override
    public AbstractOperation[] allOperations() {
        return GeneralOperation.values();
    }

    /* (non-Javadoc)
     * @see jbyco.analysis.patterns.operations.AbstractOperationFactory#allHandleOperations()
     */
    @Override
    public AbstractOperation[] allHandleOperations() {
        return GeneralHandleOperation.values();
    }

}
