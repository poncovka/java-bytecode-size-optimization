package jbyco.optimization.common;

import org.objectweb.asm.tree.MethodNode;

/**
 * The Class MethodTransformer.
 * The code is inspired by the document ASM 4.0 A Java bytecode engineering library.

 */
public abstract class MethodTransformer {

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
     * Transform the given method frame.
     *
     * @param mn the method frame
     */
    public boolean transform(MethodNode mn) {
        if (mt != null) {
            return mt.transform(mn);
        }

        return false;
    }

}
