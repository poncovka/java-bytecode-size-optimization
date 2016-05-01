package jbyco.optimize;

import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public interface Pattern {

    public boolean findAndReplace(InsnList list, List<AbstractInsnNode> group);

}
