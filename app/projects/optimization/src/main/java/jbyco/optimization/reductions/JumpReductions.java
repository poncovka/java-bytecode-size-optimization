package jbyco.optimization.reductions;

import jbyco.optimization.Statistics;
import jbyco.optimization.jump.LabelNodeInfo;
import jbyco.optimization.jump.LabelNodesCollector;
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

    // TODO join LABELS: LABEL, LABEL -> LABEL
    // remove unused labels
    // remove frames withount labels

    // TODO SWITCH - implement class for switch optimizations, the absolute label values are neccessary, POP!!!
    // TODO LABEL x, GOTO y - implement class for jump optimizations
    // TODO GOTO x, ...., LABEL x, iRETURN


    Statistics stats;

    public JumpReductions(Statistics stats) {
        this.stats = stats;
    }

    public JumpReductions(MethodTransformer mt, Statistics stats) {
        super(mt);
        this.stats = stats;
    }

    private Map<LabelNode, LabelNodeInfo> collectLabelNodes(MethodNode mn) {
        LabelNodesCollector collector = new LabelNodesCollector();
        return collector.collect(mn);
    }

    @Override
    public void transform(MethodNode mn) {

        InsnList list = mn.instructions;

        //replaceLookupswitch(list, node, labels);
        //replaceTableswitch(list, node, labels);
        //removeUselessFrames(method, labels);

        boolean change = true;
        while(change) {

            // every optimization can compromise the validity of info
            Map<LabelNode, LabelNodeInfo> labels = collectLabelNodes(mn);
            Iterator<LabelNodeInfo> i = labels.values().iterator();
            change = false;

            while (!change && i.hasNext()) {

                LabelNodeInfo info = i.next();

                change = replaceGotoWithReturn(list, info.label, info);
                if (change) {
                    if (stats != null) {
                        stats.addPepphole("replaceGotoWithReturn");
                    }
                    break;
                }

                change = removeDoubleJumps(list, info.label, info);
                if (change) {
                    if (stats != null) {
                        stats.addPepphole("removeDoubleJumps");
                    }
                    break;
                }

                change = joinLabels(list, info.label, info);
                if (change) {
                    if (stats != null) {
                        stats.addPepphole("joinLabels");
                    }
                    break;
                }
            }
        }

        super.transform(mn);
    }

    public boolean joinLabels(InsnList list, LabelNode label, LabelNodeInfo info) {
        boolean change = false;

        // label labels label
        if (info.insn.getType() == AbstractInsnNode.LABEL) {

            // get target label
            LabelNode target = ((LabelNode)info.insn);

            // replace labels in jumps
            for (AbstractInsnNode node : info.jumps) {
                change = replaceLabel(node, label, target);
            }

            for (TryCatchBlockNode block : info.handledTryCatchBlocks) {
                block.handler = target;
                change = true;
            }

            for (TryCatchBlockNode block : info.startedTryCatchBlocks) {
                block.start = target;
                change = true;
            }

            for (TryCatchBlockNode block : info.endedTryCatchBlocks) {
                block.end = target;
                change = true;
            }

            if (info.frame != null) {
                list.remove(info.frame);
                change = true;
            }

        }

        return change;
    }

    public boolean removeDoubleJumps(InsnList list, LabelNode label, LabelNodeInfo info) {
        boolean change = false;

        // is labeled instruction goto?
        if (info.insn.getOpcode() == Opcodes.GOTO) {

            // get target of this instruction
            LabelNode target = ((JumpInsnNode)info.insn).label;

            // replace labels in jumps
            for (AbstractInsnNode node : info.jumps) {
                change = replaceLabel(node, label, target);
            }
        }
        return change;
    }

    private boolean replaceLabel(AbstractInsnNode node, LabelNode label, LabelNode target) {

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

    public boolean replaceGotoWithReturn(InsnList list, LabelNode label, LabelNodeInfo info) {

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

        return false;
    }

}
