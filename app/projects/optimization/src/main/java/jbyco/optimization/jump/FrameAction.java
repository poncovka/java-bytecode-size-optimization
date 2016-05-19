package jbyco.optimization.jump;

import jbyco.optimization.common.Action;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;

import java.util.Map;

/**
 * Created by vendy on 17.5.16.
 */
@FunctionalInterface
public interface FrameAction extends Action {
    boolean replace(InsnList list, FrameNodeInfo frameInfo, Map<LabelNode, LabelNodeInfo> labels);
}
