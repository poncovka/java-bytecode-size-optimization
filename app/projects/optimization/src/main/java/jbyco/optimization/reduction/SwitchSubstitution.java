package jbyco.optimization.reduction;

import jbyco.optimization.common.InsnUtils;
import jbyco.optimization.jump.LookupSwitchAction;
import jbyco.optimization.jump.TableSwitchAction;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A library of actions for switch substitution.
 */
public class SwitchSubstitution {

    public static class LookupSwitchSubstitution implements LookupSwitchAction {

        @Override
        public boolean replace(InsnList list, LookupSwitchInsnNode node, Map<AbstractInsnNode, Integer> addresses) {

            // calculate max jump
            long maxJump = getMaxJump(node, node.dflt, node.labels, addresses);

            // calculate max size of the code
            long maxSize = getMaxCodeSize(list, node.labels.size(), addresses);

            // if max jump is short
            if (maxJump <= Short.MAX_VALUE && maxSize <= InsnUtils.MAX_CODE_SIZE) {

                // create table of values and labels
                Map<Integer, LabelNode> table = new TreeMap<>();

                for (int i = 0; i < node.labels.size(); i++) {
                    table.put((Integer)node.keys.get(i), (LabelNode)node.labels.get(i));
                }

                // creta sublist of if instructions
                InsnList sublist;
                if (node.getPrevious().getOpcode() == Opcodes.ILOAD) {
                    sublist = generateIfSwitch(table, node.dflt, node.getPrevious());
                }
                else {
                    sublist = generateIfSwitch(table, node.dflt);
                }

                // replace node with sublist
                list.insert(node, sublist);
                list.remove(node);
                return true;
            }

            return false;
        }
    }

    public static class TableSwitchSubstitution implements TableSwitchAction {

        @Override
        public boolean replace(InsnList list, TableSwitchInsnNode node, Map<AbstractInsnNode, Integer> addresses) {

            // calculate max jump from the table switch
            long maxJump = getMaxJump(node, node.dflt, node.labels, addresses);

            // calculate max size of the code
            long maxSize = getMaxCodeSize(list, node.labels.size(), addresses);

            // if max jump is short
            if (maxJump <= Short.MAX_VALUE && maxSize <= InsnUtils.MAX_CODE_SIZE) {

                // create table of values and labels
                Map<Integer, LabelNode> table = new TreeMap<>();

                for (int i = 0; i < node.labels.size(); i++) {
                    table.put(node.min + i, (LabelNode)node.labels.get(i));
                }

                // creta sublist of if instructions
                InsnList sublist;
                if (node.getPrevious().getOpcode() == Opcodes.ILOAD) {
                    sublist = generateIfSwitch(table, node.dflt, node.getPrevious());
                }
                else {
                    sublist = generateIfSwitch(table, node.dflt);
                }

                // replace node with sublist
                list.insert(node, sublist);
                list.remove(node);
                return true;
            }

            return false;
        }
    }

    public static long getMaxJump(AbstractInsnNode node, LabelNode dflt, List<LabelNode> labels, Map<AbstractInsnNode, Integer> addresses) {

        long addr = addresses.get(node);
        long maxJump = Math.abs(addr - addresses.get(dflt));

        for (LabelNode label : labels) {
            maxJump = Math.max(maxJump, Math.abs(addr - addresses.get(label)));
        }

        return maxJump;
    }

    public static long getMaxCodeSize(InsnList list, int npairs, Map<AbstractInsnNode, Integer> addresses) {
        return addresses.get(list.getLast()) + 11 * npairs + 4;
    }

    public static InsnList generateIfSwitch(Map<Integer, LabelNode> table, LabelNode dflt, AbstractInsnNode iload) {

        InsnList list = new InsnList();

        // pop first iload
        list.add(new InsnNode(Opcodes.POP));

        // generate if for every pair
        for (int key : table.keySet()) {

            LabelNode label = table.get(key);
            LabelNode next = new LabelNode();

            list.add(iload.clone(null));
            list.add(new LdcInsnNode(new Integer(key)));
            list.add(new JumpInsnNode(Opcodes.IF_ICMPEQ, label));
        }

        // goto default label
        list.add(new JumpInsnNode(Opcodes.GOTO, dflt));

        return list;
    }

    public static InsnList generateIfSwitch(Map<Integer, LabelNode> table, LabelNode dflt) {

        InsnList list = new InsnList();

        // generate if for every pair
        for (int key : table.keySet()) {

            LabelNode label = table.get(key);
            LabelNode next = new LabelNode();

            list.add(new InsnNode(Opcodes.DUP));
            list.add(new LdcInsnNode(new Integer(key)));
            list.add(new JumpInsnNode(Opcodes.IF_ICMPNE, next));
            list.add(new InsnNode(Opcodes.POP));
            list.add(new JumpInsnNode(Opcodes.GOTO, label));
            list.add(next);
        }

        // pop duplicated value
        if (!table.isEmpty()) {
            list.add(new InsnNode(Opcodes.POP));
        }

        // goto default label
        list.add(new JumpInsnNode(Opcodes.GOTO, dflt));

        return list;
    }
}
