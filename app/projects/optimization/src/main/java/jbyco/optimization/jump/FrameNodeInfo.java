package jbyco.optimization.jump;

import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LabelNode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Info about frame.
 */
public class FrameNodeInfo {

    /**
     * Frame node.
     */
    public final FrameNode frame;

    /**
     * Label that labels the frame.
     */
    public LabelNode label;

    /**
     * Collections of labels that are used in a frame.
     */
    public Collection<LabelNode> labels;


    public FrameNodeInfo(FrameNode node) {

        this.frame = node;
        this.labels = new ArrayList<>();
    }

    public void addLabel(LabelNode label) {
        labels.add(label);
    }

}
