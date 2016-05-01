package jbyco.optimize;

import org.objectweb.asm.tree.MethodNode;

/**
 * The Class MethodTransformer.
 * The code is inspired by the document ASM 4.0 A Java bytecode engineering library.

 */
public class MethodTransformer {

	/** The next transformer in a chain. */
	protected MethodTransformer mt;
	
	
	/**
	 * Instantiates a new method transformer.
	 */
	public MethodTransformer() {
		// nothing
	}
	
	/**
	 * Instantiates a new method transformer.
	 *
	 * @param mt the method transformer
	 */
	public MethodTransformer(MethodTransformer mt) {
		this.mt = mt;
	}
	
	/**
	 * Transform the given method node.
	 *
	 * @param mn the method node
	 */
	public void transform(MethodNode mn) {
		if (mn != null) {
			mt.transform(mn);
		}
	}
	
}
