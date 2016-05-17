package jbyco.optimization.jump;

import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;

import java.util.Map;

/**
 * Created by vendy on 17.5.16.
 */
@FunctionalInterface
public interface FrameAction {
    public boolean replace(InsnList list, FrameNode frame, FrameNodeInfo info, Map<LabelNode, LabelNodeInfo> labels);
}
