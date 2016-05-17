package jbyco.optimization.jump;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.*;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * Created by vendy on 16.5.16.
 */
public class LabelNodeInfo {

    public final LabelNode label;
    public FrameNode frame;
    public AbstractInsnNode insn;

    public Collection<AbstractInsnNode> jumps;
    public Collection<TryCatchBlockNode> startedTryCatchBlocks;
    public Collection<TryCatchBlockNode> endedTryCatchBlocks;
    public Collection<TryCatchBlockNode> handledTryCatchBlocks;

    public LabelNodeInfo(LabelNode label) {

        // set the label node
        this.label = label;

        // init
        this.jumps = new LinkedList<>();
        this.startedTryCatchBlocks = new LinkedList<>();
        this.endedTryCatchBlocks = new LinkedList<>();
        this.handledTryCatchBlocks = new LinkedList<>();
    }

    public void addJump(AbstractInsnNode jump) {
        jumps.add(jump);
    }
    public void addStartedTryCatchBlock(TryCatchBlockNode block) {
        startedTryCatchBlocks.add(block);
    }
    public void addEndedTryCatchBlock(TryCatchBlockNode block) {
        endedTryCatchBlocks.add(block);
    }
    public void addHandledTryCatchBlock(TryCatchBlockNode block) {
        handledTryCatchBlocks.add(block);
    }

    public boolean isUsefull() {
        return     !jumps.isEmpty()
                || !startedTryCatchBlocks.isEmpty()
                || !endedTryCatchBlocks.isEmpty()
                || !handledTryCatchBlocks.isEmpty();
    }

}
