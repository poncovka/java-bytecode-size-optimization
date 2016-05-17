package jbyco.optimization.jump;

import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by vendy on 16.5.16.
 */
public class LabelNodeInfo {

    public final LabelNode label;
    public FrameNode frame;
    public AbstractInsnNode insn;

    public Collection<FrameNode> frames;
    public Collection<AbstractInsnNode> jumps;
    public Collection<TryCatchBlockNode> tryCatchBlocks;
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
