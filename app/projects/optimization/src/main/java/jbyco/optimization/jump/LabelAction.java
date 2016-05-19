package jbyco.optimization.jump;

import jbyco.optimization.common.Action;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by vendy on 17.5.16.
 */
@FunctionalInterface
public interface LabelAction extends Action {
    boolean replace(InsnList list, LabelNodeInfo labelInfo, Map<AbstractInsnNode, Integer> addresses);
}
