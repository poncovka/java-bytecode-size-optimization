package jbyco.optimization.jump;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;

/**
 * Created by vendy on 17.5.16.
 */
@FunctionalInterface
public interface LabelAction {
    public boolean replace(InsnList list, LabelNode label, LabelNodeInfo info);
}
