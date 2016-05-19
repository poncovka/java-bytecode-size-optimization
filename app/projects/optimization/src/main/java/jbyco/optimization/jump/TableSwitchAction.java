package jbyco.optimization.jump;

import jbyco.optimization.common.Action;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import java.util.Map;

/**
 * Created by vendy on 20.5.16.
 */
@FunctionalInterface
public interface TableSwitchAction extends Action{
    boolean replace(InsnList list, TableSwitchInsnNode node, Map<AbstractInsnNode, Integer> addresses);
}
