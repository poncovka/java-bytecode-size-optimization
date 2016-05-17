package jbyco.optimization.jump;

import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LabelNode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vendy on 17.5.16.
 */
public class FrameNodeInfo {

    public final FrameNode frame;
    public LabelNode label;
    public Collection<LabelNode> labels;

    public FrameNodeInfo(FrameNode node) {

        this.frame = node;
        this.labels = new ArrayList<>();
    }

    public void addLabel(LabelNode label) {
        labels.add(label);
    }

}
