package jbyco.analysis.patterns.operations;

import org.objectweb.asm.Opcodes;

/**
 * The general representation of the handle tags.
 * <p>
 * General handle operations are managed by {@link GeneralOperationFactory}.
 */
public enum GeneralHandleOperation implements AbstractHandleOperation {

    /**
     * The h_getfield operation.
     */
    H_GETFIELD(Opcodes.H_GETFIELD),

    /**
     * The h_getstatic operation.
     */
    H_GETSTATIC(Opcodes.H_GETSTATIC),

    /**
     * The h_putfield operation.
     */
    H_PUTFIELD(Opcodes.H_PUTFIELD),

    /**
     * The h_putstatic operation.
     */
    H_PUTSTATIC(Opcodes.H_PUTSTATIC),

    /**
     * The h_invoke operation.
     */
    H_INVOKE(
            Opcodes.H_INVOKEVIRTUAL,
            Opcodes.H_INVOKESTATIC,
            Opcodes.H_INVOKESPECIAL,
            Opcodes.H_INVOKEINTERFACE
    ),

    /**
     * The h_newinvoke operation.
     */
    H_NEWINVOKE(Opcodes.H_NEWINVOKESPECIAL);

    /**
     * The tags.
     */
    private int[] tags;

    /**
     * Instantiates a new general handle operation.
     *
     * @param tags the tags
     */
    private GeneralHandleOperation(int... tags) {
        this.tags = tags;
    }

    public int[] getOpcodes() {
        return this.tags;
    }

}
