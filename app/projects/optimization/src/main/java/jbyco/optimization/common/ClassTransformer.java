package jbyco.optimization.common;

import org.objectweb.asm.tree.ClassNode;

/**
 * The Class ClassTransformer.
 * The code is inspired by the document ASM 4.0 A Java bytecode engineering library.

 */
public abstract class ClassTransformer {

    /** The next transformer in a chain. */
    protected ClassTransformer ct;


    /**
     * Instantiates a new class transformer.
     */
    public ClassTransformer() {
        // nothing
    }

    /**
     * Instantiates a new class transformer.
     *
     * @param ct the class transformer
     */
    public ClassTransformer(ClassTransformer ct) {
        this.ct = ct;
    }

    /**
     * Transform the given class node.
     *
     * @param cn the class node
     * @return is the class node transformed?
     */
    public boolean transform(ClassNode cn) {
        if (ct != null) {
            return ct.transform(cn);
        }

        return false;
    }

}
