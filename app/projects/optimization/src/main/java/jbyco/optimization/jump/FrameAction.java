package jbyco.optimization.jump;

import jbyco.optimization.common.Action;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;

import java.util.Map;

/**
 * An interface for frame action.
 */
@FunctionalInterface
public interface FrameAction extends Action {

    /**
     * Replaces instructions in a list based on frame info and labels.
     * @param list          list of instructions
     * @param frameInfo     information about a frame
     * @param labels        information about labels
     * @return is the list changed?
     */
    boolean replace(InsnList list, FrameNodeInfo frameInfo, Map<LabelNode, LabelNodeInfo> labels);
}
