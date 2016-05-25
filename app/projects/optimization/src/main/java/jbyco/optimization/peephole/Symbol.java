package jbyco.optimization.peephole;

import org.objectweb.asm.tree.AbstractInsnNode;

/**
 * An interface for symbol of the pattern.
 */
public interface Symbol {

    /**
     * Try to match the symbol.
     * @param i instruction
     * @return does the instruction matches the symbol?
     */
    public boolean match(AbstractInsnNode i);

}
