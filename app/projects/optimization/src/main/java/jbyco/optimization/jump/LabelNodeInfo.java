package jbyco.optimization.jump;

import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Information about a label.
 */
public class LabelNodeInfo {

    /**
     * Label node.
     */
    public final LabelNode label;

    /**
     * Frame labeled by this label.
     */
    public FrameNode frame;

    /**
     * Instruction labeled by this label.
     */
    public AbstractInsnNode insn;

    /**
     * Collection of frames with this labels.
     */
    public Collection<FrameNode> frames;

    /**
     * Collection of instructions that jump at this lable.
     */
    public Collection<AbstractInsnNode> jumps;

    /**
     * Collection of try catch blocks that are defined by this label.
     */
    public Collection<TryCatchBlockNode> tryCatchBlocks;

    /**
     * Collection of annotations that contain this label.
     */
    public Collection<LocalVariableAnnotationNode> annotations;

    public LabelNodeInfo(LabelNode label) {

        // set the label frame
        this.label = label;

        // init
        this.jumps = new ArrayList<>();
        this.frames = new ArrayList<>();
        this.tryCatchBlocks = new ArrayList<>();
        this.annotations = new ArrayList<>();
    }

    public void addFrame(FrameNode frame) {
        frames.add(frame);
    }

    public void addJump(AbstractInsnNode jump) {
        jumps.add(jump);
    }

    public void addTryCatchBlock(TryCatchBlockNode block) {
        tryCatchBlocks.add(block);
    }

    public void addAnnotation(LocalVariableAnnotationNode node) {
        annotations.add(node);
    }

    public boolean isUsefull() {
        return     !jumps.isEmpty()
                || !frames.isEmpty()
                || !tryCatchBlocks.isEmpty()
                || !annotations.isEmpty();
    }

    public boolean isOnlyInFrame(FrameNode frame) {
        return     frames.size() == 1
                && frames.contains(frame)
                && jumps.isEmpty()
                && tryCatchBlocks.isEmpty()
                && annotations.isEmpty();
    }

    public boolean isCompletedLabeling() {
        return insn != null;
    }

}
