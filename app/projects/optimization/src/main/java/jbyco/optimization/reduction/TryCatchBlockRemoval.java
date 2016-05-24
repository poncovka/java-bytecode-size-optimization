package jbyco.optimization.reduction;

import jbyco.optimization.Statistics;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by vendy on 20.5.16.
 */
public class TryCatchBlockRemoval extends MethodVisitor {

    Statistics stats;

    public TryCatchBlockRemoval(Statistics stats) {
        super(Opcodes.ASM5);
        this.stats = stats;

    }

    public TryCatchBlockRemoval(MethodVisitor mv, Statistics stats) {
        super(Opcodes.ASM5, mv);
        this.stats = stats;
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {

        if (!start.equals(end)) {
            if (stats != null) {
                stats.addOptimization(this.getClass().getSimpleName());
            }
        }
        else {
            super.visitTryCatchBlock(start, end, handler, type);
        }
    }
}
