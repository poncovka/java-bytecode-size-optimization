package jbyco.optimization.peephole;

import jbyco.optimization.common.Action;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

/**
 * Created by vendy on 11.5.16.
 */
@FunctionalInterface
public interface PeepholeAction extends Action {

    boolean replace(InsnList list, AbstractInsnNode[] matched);

    default Pattern[] getPatterns() {
        return this.getClass().getAnnotationsByType(Pattern.class);
    }

}
