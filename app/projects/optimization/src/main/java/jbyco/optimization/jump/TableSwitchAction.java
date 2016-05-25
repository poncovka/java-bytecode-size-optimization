package jbyco.optimization.jump;

import jbyco.optimization.common.Action;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import java.util.Map;

/**
 * An interface for tableswitch optimization actions.
 */
@FunctionalInterface
public interface TableSwitchAction extends Action{

    /**
     * Replaces the instruction in a list base on the tableswitch node and addresses.
     * @param list      list of instructions
     * @param node      tableswitch node
     * @param addresses addresses of instructions
     * @return is list changed?
     */
    boolean replace(InsnList list, TableSwitchInsnNode node, Map<AbstractInsnNode, Integer> addresses);
}
