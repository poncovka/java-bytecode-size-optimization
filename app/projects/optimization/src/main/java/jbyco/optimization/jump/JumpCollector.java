package jbyco.optimization.jump;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.*;

/**
 * Created by vendy on 16.5.16.
 */
public class JumpCollector {

    public Map<LabelNode, LabelNodeInfo> labels;
    public Map<FrameNode, FrameNodeInfo> frames;
    public Collection<LookupSwitchInsnNode> lookupSwitches;
    public Collection<TableSwitchInsnNode> tableSwitches;

    public JumpCollector() {
        init();
    }

    public void init() {
        labels = new HashMap<>();
        frames = new HashMap<>();
        lookupSwitches = new ArrayList<>();
        tableSwitches = new ArrayList<>();
    }

    LabelNodeInfo getInfo(LabelNode node) {

        LabelNodeInfo info = labels.get(node);

        if (info == null) {
            info = new LabelNodeInfo(node);
            labels.put(node, info);
        }

        return info;
    }

    FrameNodeInfo getInfo(FrameNode node) {

        FrameNodeInfo info = frames.get(node);

        if (info == null) {
            info = new FrameNodeInfo(node);
            frames.put(node, info);
        }

        return info;
    }


    public void collect(MethodNode method) {

        // init
        init();

        LabelNodeInfo labelInfo = null;
        FrameNodeInfo frameInfo = null;

        // process instructions
        InsnList list = method.instructions;
        AbstractInsnNode node = list.getFirst();

        while(node != null) {

            // update current info
            labelInfo = updateLabelInfo(labelInfo, node);
            frameInfo = updateFrameInfo(labelInfo, frameInfo, node);

            // process jumps
            processJump(node);

            //process frames
            processFrame(node);

            // next
            node = node.getNext();
        }

        // process try blocks
        for(Object object : method.tryCatchBlocks) {
            processTryCatchBlock((TryCatchBlockNode)object);
        }

        // process visible annotations
        if (method.visibleLocalVariableAnnotations != null) {
            for (Object object : method.visibleLocalVariableAnnotations) {
                processAnnotation((LocalVariableAnnotationNode)object);
            }
        }

        // process invisible annotations
        if (method.invisibleLocalVariableAnnotations != null) {
            for (Object object : method.invisibleLocalVariableAnnotations) {
                processAnnotation((LocalVariableAnnotationNode)object);
            }
        }

    }

    LabelNodeInfo updateLabelInfo(LabelNodeInfo info, AbstractInsnNode node) {

        int type = node.getType();

        // add labeled instruction
        if (       info != null
                && info.insn == null
                && type != AbstractInsnNode.FRAME
                && type != AbstractInsnNode.LINE) {

            info.insn = node;
        }

        // set new info
        if (type == AbstractInsnNode.LABEL) {
            info = getInfo((LabelNode)node);
        }

        return info;
    }

    FrameNodeInfo updateFrameInfo(LabelNodeInfo labelInfo, FrameNodeInfo frameInfo, AbstractInsnNode node) {

        // is frame?
        if (node.getType() == AbstractInsnNode.FRAME) {

            // add info
            FrameNode newFrame = (FrameNode)node;
            FrameNodeInfo newFrameInfo = getInfo(newFrame);

            // add label if it labels this frame
            if (labelInfo != null && !labelInfo.isCompletedLabeling()) {

                // label belong to previous frame
                if (frameInfo != null
                        && frameInfo.label != null
                        && frameInfo.label.equals(labelInfo.label)) {
                    // nothing
                }
                // add label to the new frame
                else {
                    newFrameInfo.label = labelInfo.label;
                }
            }

            frameInfo = newFrameInfo;
        }

        return frameInfo;
    }

    void processJump(AbstractInsnNode node) {

        int type = node.getType();

        // add a jump instruction as a source of jump
        if (type == AbstractInsnNode.JUMP_INSN) {
            getInfo(((JumpInsnNode)node).label).addJump(node);
        }

        // add a lookupswitch instruction as a source of jump
        else if (type == AbstractInsnNode.LOOKUPSWITCH_INSN) {
            LookupSwitchInsnNode lookup = (LookupSwitchInsnNode) node;

            // add source to the default label frame
            getInfo(lookup.dflt).addJump(lookup);

            // add source to the other label nodes
            for (Object label : lookup.labels) {
                getInfo((LabelNode)label).addJump(lookup);
            }

            lookupSwitches.add(lookup);
        }

        // add a tableswitch instruction as a source of jump
        else if (type == AbstractInsnNode.TABLESWITCH_INSN) {
            TableSwitchInsnNode table = (TableSwitchInsnNode) node;

            // add source to the default label frame
            getInfo(table.dflt).addJump(table);

            // add source to the other label nodes
            for (Object label : table.labels) {
                getInfo((LabelNode)label).addJump(table);
            }

            tableSwitches.add((TableSwitchInsnNode) node);
        }
    }

    void processTryCatchBlock(TryCatchBlockNode block) {
        getInfo(block.start).addTryCatchBlock(block);
        getInfo(block.end).addTryCatchBlock(block);
        getInfo(block.handler).addTryCatchBlock(block);
    }

    void processAnnotation(LocalVariableAnnotationNode a) {

        for (LabelNode node : (List<LabelNode>)a.start) {
            getInfo(node).addAnnotation(a);
        }

        for (LabelNode node : (List<LabelNode>)a.end) {
            getInfo(node).addAnnotation(a);
        }
    }

    void processFrame(AbstractInsnNode node) {

        // is frame?
        if (node.getType() == AbstractInsnNode.FRAME) {

            // find labels
            FrameNode frame = (FrameNode) node;
            for (Object object : frame.local) {

                // process labels
                if (object instanceof LabelNode) {

                    LabelNode label = (LabelNode) object;
                    getInfo(label).addFrame(frame);
                    getInfo(frame).addLabel(label);
                }

            }
        }
    }

}
