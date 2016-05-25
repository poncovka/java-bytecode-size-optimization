package jbyco.optimization.jump;

import jbyco.optimization.common.Action;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;

import java.util.Arrays;
import java.util.Map;

/**
 * An action for optimizations based on labels.
 */
@FunctionalInterface
public interface LabelAction extends Action {

    /**
     * Replaces instructions in a list based od label info and addresses.
     * @param list          list of instructions
     * @param labelInfo     information about a label
     * @param addresses     adresses of instructions
     * @return  is the list changed?
     */
    boolean replace(InsnList list, LabelNodeInfo labelInfo, Map<AbstractInsnNode, Integer> addresses);
}
