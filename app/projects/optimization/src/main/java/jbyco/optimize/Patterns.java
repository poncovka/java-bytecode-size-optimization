package jbyco.optimize;

import java.util.Deque;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

// TODO: Auto-generated Javadoc
/**
 * The Class Patterns.
 */
public class Patterns {

    /**
     * The Class AddPatern.
     * Simplify:
     *     CONST I(0); ADD;
     */
    public class AddPatern implements Pattern {

        /* (non-Javadoc)
         * @see jbyco.optimize.Pattern#findAndReplace(org.objectweb.asm.tree.InsnList, java.util.List)
         */
        @Override
        public boolean findAndReplace(InsnList list, List<AbstractInsnNode> group) {

            int len = group.size();
            int last = len - 1;

            if (len >= 2) {
                AbstractInsnNode n1 = group.get(last);
                AbstractInsnNode n2 = group.get(last - 1);

                if (n1.getOpcode() == Opcodes.IADD) {
                }
            }


            return false;
        }

    }

}
