package jbyco.optimization.reductions;

import jbyco.optimization.Statistics;
import jbyco.optimization.jump.*;
import jbyco.optimization.transformation.MethodTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by vendy on 12.5.16.
 */
public class JumpReductions extends MethodTransformer {

    // TODO SWITCH - implement class for switch optimizations, the absolute label values are neccessary, POP!!!
    // TODO LABEL x, GOTO y - implement class for jump optimizations
    // TODO GOTO x, ...., LABEL x, iRETURN

    public static boolean removeUselessFrame(InsnList list, FrameNode frame, FrameNodeInfo info, Map<LabelNode, LabelNodeInfo> labels) {

        boolean change = false;

        // frame can be removed
        if (info.label == null && info.labels.isEmpty()) {
            list.remove(frame);
            change = true;
        }
        // labels in the frame are only in this frame
        else {
            boolean inFrame = true;
            for (LabelNode label : info.labels) {

                if (!labels.get(label).isOnlyInFrame(frame)) {
                    inFrame = false;
                    break;
                }
            }

            if (inFrame) {
                list.remove(frame);
                change = true;
            }
        }
        return change;
    }

    public static boolean removeUselessLabel(InsnList list, LabelNode label, LabelNodeInfo info) {
        if (!info.isUsefull()) {
            list.remove(label);
            return true;
        }

        return false;
    }

    public static boolean joinLabels(InsnList list, LabelNode label, LabelNodeInfo info) {
        boolean change = false;

        // label labels label
        if (info.insn != null && info.insn.getType() == AbstractInsnNode.LABEL) {

            // get target label
            LabelNode target = ((LabelNode)info.insn);

            // replace labels in jumps
            for (AbstractInsnNode node : info.jumps) {
                change = replaceLabel(node, label, target);
            }

            for (TryCatchBlockNode block : info.tryCatchBlocks) {
                change = replaceLabel(block, label, target);
            }

            for (LocalVariableAnnotationNode node : info.annotations) {
                change = replaceLabel(node, label, target);
            }

            for (FrameNode frame : info.frames) {
                change = replaceLabel(frame, label, target);
            }

        }

        return change;
    }

    public static boolean replaceGotoWithReturn(InsnList list, LabelNode label, LabelNodeInfo info) {

        // has labeled instruction?
        if (info.insn != null) {

            // is labeled instruction return?
            int opcode = info.insn.getOpcode();
            if (Opcodes.IRETURN <= opcode && opcode <= Opcodes.RETURN) {

                // find goto instruction that jumps to this label
                for (AbstractInsnNode node : info.jumps) {

                    // replace goto with return instruction
                    if (node.getOpcode() == Opcodes.GOTO) {
                        list.set(node, info.insn.clone(null));
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean removeDoubleJumps(InsnList list, LabelNode label, LabelNodeInfo info) {
        boolean change = false;

        // is labeled instruction goto?
        if (info.insn != null && info.insn.getOpcode() == Opcodes.GOTO) {

            // get target of this instruction
            LabelNode target = ((JumpInsnNode)info.insn).label;

            // replace labels in jumps
            for (AbstractInsnNode node : info.jumps) {
                change = replaceLabel(node, label, target);
            }
        }
        return change;
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
