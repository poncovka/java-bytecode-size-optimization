package jbyco.optimization.jump;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vendy on 16.5.16.
 */
public class LabelNodesCollector {

    Map<LabelNode, LabelNodeInfo> labels;

    LabelNodeInfo getInfo(LabelNode node) {

        LabelNodeInfo info = labels.get(node);

        if (info == null) {
            info = new LabelNodeInfo(node);
            labels.put(node, info);
        }

        return info;
    }

    public Map<LabelNode, LabelNodeInfo> collect(MethodNode method) {

        labels = new HashMap<>();

        // process instructions
        InsnList list = method.instructions;
        AbstractInsnNode node = list.getFirst();
        LabelNodeInfo info = null;

        while(node != null) {

            info = updateInfo(info, node);
            updateSources(node);
            node = node.getNext();
        }

        // process try blocks
        for(Object object : method.tryCatchBlocks) {
            updateTryCatchInfo((TryCatchBlockNode)object);
        }

        return labels;
    }

    LabelNodeInfo updateInfo(LabelNodeInfo info, AbstractInsnNode node) {

        int type = node.getType();

        // add label info
        if (type == AbstractInsnNode.LABEL) {

            // add labeled instruction info
            if (info != null && info.insn == null) {
                info.insn = node;
                info = null;
            }

            // set info
            info = getInfo((LabelNode)node);
        }
        // add frame info
        else if (type == AbstractInsnNode.FRAME) {
            if (info != null && info.frame == null) {
                info.frame = (FrameNode) node;
            }
        }
        // skip the line number
        else if (type == AbstractInsnNode.LINE) {
            // nothing
        }
        else {

            // add labeled instruction info
            if (info != null && info.insn == null) {
                info.insn = node;
                info = null;
            }
        }

        return info;
    }

    void updateSources(AbstractInsnNode node) {

        int type = node.getType();

        // add a jump instruction as a source of jump
        if (type == AbstractInsnNode.JUMP_INSN) {
            getInfo(((JumpInsnNode)node).label).addJump(node);
        }

        // add a lookupswitch instruction as a source of jump
        else if (type == AbstractInsnNode.LOOKUPSWITCH_INSN) {
            LookupSwitchInsnNode lookup = (LookupSwitchInsnNode) node;

            // add source to the default label node
            getInfo(lookup.dflt).addJump(lookup);

            // add source to the other label nodes
            for (Object label : lookup.labels) {
                getInfo((LabelNode)label).addJump(lookup);
            }
        }

        // add a tableswitch instruction as a source of jump
        else if (type == AbstractInsnNode.TABLESWITCH_INSN) {
            TableSwitchInsnNode table = (TableSwitchInsnNode) node;

            // add source to the default label node
            getInfo(table.dflt).addJump(table);

            // add source to the other label nodes
            for (Object label : table.labels) {
                getInfo((LabelNode)label).addJump(table);
            }
        }
    }

    void updateTryCatchInfo(TryCatchBlockNode block) {
        getInfo(block.start).addStartedTryCatchBlock(block);
        getInfo(block.end).addEndedTryCatchBlock(block);
        getInfo(block.handler).addHandledTryCatchBlock(block);
    }
}
