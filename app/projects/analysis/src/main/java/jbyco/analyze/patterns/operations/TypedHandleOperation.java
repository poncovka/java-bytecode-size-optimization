package jbyco.analyze.patterns.operations;

import org.objectweb.asm.Opcodes;

/**
 * The representation of typed handle tags.
 * <p>
 * Typed handle operations are managed by {@link TypedOperationFactory}.
 */
public enum TypedHandleOperation implements AbstractHandleOperation {
	
	/** The h_getfield operation. */
	H_GETFIELD	(Opcodes.H_GETFIELD),
	
	/** The h_getstatic operation. */
	H_GETSTATIC	(Opcodes.H_GETSTATIC),
	
	/** The h_putfield operation. */
	H_PUTFIELD	(Opcodes.H_PUTFIELD),
	
	/** The h_putstatic operation. */
	H_PUTSTATIC	(Opcodes.H_PUTSTATIC),	
	
	/** The h_invokevirtual operation. */
	H_INVOKEVIRTUAL (Opcodes.H_INVOKEVIRTUAL),
	
	/** The h_invokestatic operation. */
	H_INVOKESTATIC (Opcodes.H_INVOKESTATIC),
	
	/** The h_invokespecial operation. */
	H_INVOKESPECIAL (Opcodes.H_INVOKESPECIAL),
	
	/** The h_newinvokespecial operation. */
	H_NEWINVOKESPECIAL (Opcodes.H_NEWINVOKESPECIAL),
	
	/** The h_invokeinterface operation. */
	H_INVOKEINTERFACE (Opcodes.H_INVOKEINTERFACE);

	/** The tags. */
	private int[] tags;
	
	/**
	 * Instantiates a new typed handle operation.
	 *
	 * @param tags the tags
	 */
	private TypedHandleOperation(int ...tags) {
		this.tags = tags;
	}
	
	public int[] getOpcodes() {
		return this.tags;
	}
}
