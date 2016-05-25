package jbyco.optimization.jump;

import jbyco.optimization.common.Action;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import java.util.Map;

/**
 * An interface for optimization action that uses lookupswitch.
 */
@FunctionalInterface
public interface LookupSwitchAction extends Action {

    /**
     * Replace instructions in a list based on lookupswitch instruction and addresses.
     * @param list      list of instructions
     * @param node      lookupswitch instruction
     * @param addresses addresses of instructions
     * @return is list changed?
     */
    boolean replace(InsnList list, LookupSwitchInsnNode node, Map<AbstractInsnNode, Integer> addresses);
}
