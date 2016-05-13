package jbyco.optimization;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashSet;
import java.util.Set;

/**
 * The class for finding active labels.
 * <p>
 * Active labels are labels, that are targets of jumps,
 * beginnings of catch and finally blocks or beginnings
 * and ends of the methods.
 */
public class ActiveLabelsFinder extends MethodVisitor {

    /**
     * The set of active labels.
     */
    Set<Label> active = new HashSet<>();

    /**
     * Instantiates a new active labels finder.
     */
    public ActiveLabelsFinder() {
        super(Opcodes.ASM5);
    }

    /**
     * Checks if the given label is active.
     *
     * @param label the ASM label
     * @return true, if the label is active
     */
    public boolean isActive(Label label) {
        return active.contains(label);
    }

    /**
     * Adds the label to active labels.
     *
     * @param label the ASM label
     */
    public void add(Label label) {
        active.add(label);
    }

    /* (non-Javadoc)
     * @see org.objectweb.asm.MethodVisitor#visitJumpInsn(int, org.objectweb.asm.Label)
     */
    @Override
    public void visitJumpInsn(int opcode, Label label) {
        add(label);
    }

    /* (non-Javadoc)
     * @see org.objectweb.asm.MethodVisitor#visitTableSwitchInsn(int, int, org.objectweb.asm.Label, org.objectweb.asm.Label[])
     */
    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {

        // add the default label
        add(dflt);

        // add the table of labels
        for (Label label : labels) {
            add(label);
        }
    }

    /* (non-Javadoc)
     * @see org.objectweb.asm.MethodVisitor#visitLookupSwitchInsn(org.objectweb.asm.Label, int[], org.objectweb.asm.Label[])
     */
    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {

        // add the default label
        add(dflt);

        // add the table of labels
        for (Label label : labels) {
            add(label);
        }
    }

    /* (non-Javadoc)
     * @see org.objectweb.asm.MethodVisitor#visitTryCatchBlock(org.objectweb.asm.Label, org.objectweb.asm.Label, org.objectweb.asm.Label, java.lang.String)
     */
    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        add(start);
        add(end);
        add(handler);
    }

}
