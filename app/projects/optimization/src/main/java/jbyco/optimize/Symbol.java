package jbyco.optimize;

import org.objectweb.asm.tree.AbstractInsnNode;

/**
 * Created by vendy on 2.5.16.
 */
public interface Symbol {

    public boolean match(AbstractInsnNode i);

}
