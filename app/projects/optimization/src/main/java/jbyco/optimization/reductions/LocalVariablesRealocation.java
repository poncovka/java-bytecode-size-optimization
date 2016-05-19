package jbyco.optimization.reductions;

import jbyco.optimization.Statistics;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

/**
 * Created by vendy on 19.5.16.
 */
public class LocalVariablesRealocation extends LocalVariablesSorter {

    public LocalVariablesRealocation(int access, String desc, MethodVisitor mv, Statistics stats) {
        super(Opcodes.ASM5, access, desc, mv);

        if (stats != null) {
            stats.addOptimization(this.getClass().getSimpleName());
        }
    }
}
