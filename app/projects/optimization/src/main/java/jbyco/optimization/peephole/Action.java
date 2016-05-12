package jbyco.optimization.peephole;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

/**
 * Created by vendy on 11.5.16.
 */
@FunctionalInterface
public interface Action {
    public boolean replace(InsnList list, AbstractInsnNode[] matched);
}
