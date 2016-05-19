package jbyco.optimization.reductions;

import jbyco.optimization.jump.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * A library of frame and label actions.
 */
public class JumpReductions {

    public static class TableSwitchReplacement implements TableSwitchAction {

        @Override
        public boolean replace(InsnList list, TableSwitchInsnNode node, Map<AbstractInsnNode, Integer> addresses) {

            int keys = node.labels.size();
            boolean loadVar = node.getPrevious().getOpcode() == Opcodes.ILOAD;

            long addr = addresses.get(node);
            long maxLength = Math.abs(addr - addresses.get(node.dflt));

            for (LabelNode label : (List<LabelNode>)node.labels) {
                maxLength = Math.max(maxLength, Math.abs(addr - addresses.get(label)));
            }

            if (maxLength <= Short.MAX_VALUE) {
                System.err.println("Table");
            }

            return false;
        }
    }

    public static class LookupSwitchReplacement implements LookupSwitchAction {

        @Override
        public boolean replace(InsnList list, LookupSwitchInsnNode node, Map<AbstractInsnNode, Integer> addresses) {

            int keys = node.labels.size();
            boolean loadVar = node.getPrevious().getOpcode() == Opcodes.ILOAD;

            long addr = addresses.get(node);
            long maxLength = Math.abs(addr - addresses.get(node.dflt));

            for (LabelNode label : (List<LabelNode>)node.labels) {
                maxLength = Math.max(maxLength, Math.abs(addr - addresses.get(label)));
            }

            if (maxLength <= Short.MAX_VALUE) {
                System.err.println("Lookup");
            }

            return false;
        }
    }


    public static class UselessFrameRemoval implements FrameAction {

        @Override
        public boolean replace(InsnList list, FrameNodeInfo frameInfo, Map<LabelNode, LabelNodeInfo> labels) {

            boolean change = false;

            // frame can be removed
            if (frameInfo.label == null && frameInfo.labels.isEmpty()) {
                list.remove(frameInfo.frame);
                change = true;
            }
            // labels in the frame are only in this frame
            else {
                boolean inFrame = true;
                for (LabelNode label : frameInfo.labels) {

                    if (!labels.get(label).isOnlyInFrame(frameInfo.frame)) {
                        inFrame = false;
                        break;
                    }
                }

                if (inFrame) {
                    list.remove(frameInfo.frame);
                    change = true;
                }
            }
            return change;
        }
    }


    public static class UselessLabelRemoval implements LabelAction {

        @Override
        public boolean replace(InsnList list, LabelNodeInfo labelInfo, Map<AbstractInsnNode, Integer> addresses) {
            if (!labelInfo.isUsefull()) {
                list.remove(labelInfo.label);
                //list.resetLabels();
                return true;
            }

            return false;
        }
    }


    public static class LabelsUnion implements LabelAction {

        @Override
        public boolean replace(InsnList list, LabelNodeInfo labelInfo, Map<AbstractInsnNode, Integer> addresses) {
            boolean change = false;

            // label labels label
            if (labelInfo.insn != null && labelInfo.insn.getType() == AbstractInsnNode.LABEL) {

                // get target label
                LabelNode target = ((LabelNode) labelInfo.insn);

                // replace labels in jumps
                for (AbstractInsnNode node : labelInfo.jumps) {
                    change = replaceLabel(node, labelInfo.label, target);
                }

                for (TryCatchBlockNode block : labelInfo.tryCatchBlocks) {
                    change = replaceLabel(block, labelInfo.label, target);
                }

                for (LocalVariableAnnotationNode node : labelInfo.annotations) {
                    change = replaceLabel(node, labelInfo.label, target);
                }

                for (FrameNode frame : labelInfo.frames) {
                    change = replaceLabel(frame, labelInfo.label, target);
                }

                if (change) {
                    //list.remove(labelInfo.label);
                    //list.resetLabels();
                }
            }

            return change;
        }
    }

    public static class JumpWithReturnReplacement implements LabelAction {

        @Override
        public boolean replace(InsnList list, LabelNodeInfo labelInfo, Map<AbstractInsnNode, Integer> addresses) {

            // has labeled instruction?
            if (labelInfo.insn != null) {

                // is labeled instruction return?
                int opcode = labelInfo.insn.getOpcode();
                if (Opcodes.IRETURN <= opcode && opcode <= Opcodes.RETURN) {

                    // find goto instruction that jumps to this label
                    for (AbstractInsnNode node : labelInfo.jumps) {

                        // replace goto with return instruction
                        if (node.getOpcode() == Opcodes.GOTO) {
                            list.set(node, labelInfo.insn.clone(null));
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    public static class DoubleJumpRemoval implements LabelAction {

        @Override
        public boolean replace(InsnList list, LabelNodeInfo labelInfo, Map<AbstractInsnNode, Integer> addresses) {
            boolean change = false;

            // is labeled instruction goto?
            if (labelInfo.insn != null && labelInfo.insn.getOpcode() == Opcodes.GOTO) {

                // get target of this instruction
                LabelNode target = ((JumpInsnNode) labelInfo.insn).label;

                // replace labels in jumps
                for (AbstractInsnNode node : labelInfo.jumps) {

                    // calculate the length of the jump
                    long length = 0;//addresses.get(target) - addresses.get(node);

                    // replace the label in the jump
                    if (Short.MIN_VALUE <= length && length <= Short.MAX_VALUE) {
                        change = replaceLabel(node, labelInfo.label, target);
                    }
                }
            }
            return change;
        }
    }

    private static boolean replaceLabel(FrameNode frame, LabelNode label, LabelNode target) {

        boolean change = false;
        ListIterator<Object> i = frame.local.listIterator();
        while(i.hasNext()) {

            Object object = i.next();
            if (object instanceof LabelNode) {

                if (((LabelNode)object).equals(label)) {
                    i.set(target);
                    change = true;
                }
            }
        }

        return change;
    }

    private static boolean replaceLabel(LocalVariableAnnotationNode node, LabelNode label, LabelNode target) {

        boolean change = false;
        ListIterator<LabelNode> i;

        i = node.start.listIterator();
        while(i.hasNext()) {
            if (i.next().equals(label)) {
                i.set(target);
                change = true;
            }
        }

        i = node.end.listIterator();
        while(i.hasNext()) {
            if (i.next().equals(label)) {
                i.set(target);
                change = true;
            }
        }

        return change;
    }

    private static boolean replaceLabel(TryCatchBlockNode node, LabelNode label, LabelNode target) {

        boolean change = false;

        if (node.handler.equals(label)) {
            node.handler = target;
            change = true;
        }

        if (node.start.equals(label)) {
            node.start = target;
            change = true;
        }

        if (node.end.equals(label)) {
            node.end = target;
            change = true;
        }

        return change;
    }

    private static boolean replaceLabel(AbstractInsnNode node, LabelNode label, LabelNode target) {

        int type = node.getType();
        boolean change = false;

        if (type == AbstractInsnNode.JUMP_INSN) {

            JumpInsnNode jump = (JumpInsnNode) node;
            if (jump.label.equals(label)) {
                jump.label = target;
                change = true;
            }
        }
        else if (type == AbstractInsnNode.TABLESWITCH_INSN) {

            TableSwitchInsnNode table = (TableSwitchInsnNode) node;
            if (table.dflt.equals(label)) {
                table.dflt = target;
                change = true;
            }

            ListIterator<LabelNode> i = table.labels.listIterator();
            while (i.hasNext()) {
                if (i.next().equals(label)) {
                    i.set(target);
                    change = true;
                }
            }
        }
        else if (type == AbstractInsnNode.LOOKUPSWITCH_INSN) {

            LookupSwitchInsnNode table = (LookupSwitchInsnNode) node;
            if (table.dflt.equals(label)) {
                table.dflt = target;
                change = true;
            }

            ListIterator<LabelNode> i = table.labels.listIterator();
            while (i.hasNext()) {
                if (i.next().equals(label)) {
                    i.set(target);
                    change = true;
                }
            }
        }

        return change;
    }
}
