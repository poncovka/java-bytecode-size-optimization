package jbyco.optimization.peephole;

import jbyco.optimization.common.Action;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

/**
 * An interface for label actions.
 */
@FunctionalInterface
public interface PeepholeAction extends Action {

    /**
     * Replaces matched instructions in a list.
     * @param list list of instructions
     * @param matched array of matched instructions
     * @return is list changed?
     */
    boolean replace(InsnList list, AbstractInsnNode[] matched);

    /**
     * Gets patterns of the action.
     * @return patterns
     */
    default Pattern[] getPatterns() {
        return this.getClass().getAnnotationsByType(Pattern.class);
    }

}
