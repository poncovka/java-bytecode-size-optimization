package jbyco.optimization.simplifications;

import jbyco.optimization.peephole.InsnUtils;
import jbyco.optimization.peephole.Pattern;
import jbyco.optimization.peephole.Symbols;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * A library of patterns and actions to optimize jumps, labels and comparisons.
 */
public class JumpSimplifications {

    // -------------------------------------------------------------------------------------------- useless

    @Pattern({Symbols.GOTO   /*x*/, Symbols.LABEL /*x*/}) /* => LABEL x */
    public static boolean removeJump(InsnList list, AbstractInsnNode[] matched) {

        if (((JumpInsnNode) matched[0]).label.equals(matched[1])) {
            list.remove(matched[0]);
            return true;
        }

        return false;
    }

    @Pattern({Symbols.IF /*x*/, Symbols.LABEL /*x*/}) /* => POP; LABEL x */
    public static boolean removeIf(InsnList list, AbstractInsnNode[] matched) {

        if (((JumpInsnNode) matched[0]).label.equals(matched[1])) {
            list.set(matched[0], new InsnNode(Opcodes.POP));
            return true;
        }

        return false;
    }

    @Pattern({Symbols.IF_CMP /*x*/, Symbols.LABEL /*x*/}) /* => POP2; LABEL x */
    public static boolean removeIfCmp(InsnList list, AbstractInsnNode[] matched) {

        if (((JumpInsnNode) matched[0]).label.equals(matched[1])) {
            list.set(matched[0], new InsnNode(Opcodes.POP2));
            return true;
        }

        return false;
    }

    @Pattern({Symbols.LABEL, Symbols.FRAME, Symbols.LABEL}) /* => LABEL, LABEL */
    public static boolean removeFrame(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.GOTO, Symbols.NOTLABEL})      /* => GOTO */
    @Pattern({Symbols.RETURN, Symbols.NOTLABEL})    /* => RETURN */
    @Pattern({Symbols.ARETURN, Symbols.NOTLABEL})   /* => ARETURN */
    @Pattern({Symbols.IRETURN, Symbols.NOTLABEL})   /* => IRETURN */
    @Pattern({Symbols.LRETURN, Symbols.NOTLABEL})   /* => LRETURN */
    @Pattern({Symbols.FRETURN, Symbols.NOTLABEL})   /* => FRETURN */
    @Pattern({Symbols.DRETURN, Symbols.NOTLABEL})   /* => DRETURN */
    public static boolean removeAfterJump(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[1]);
        return true;
    }

    // -------------------------------------------------------------------------------------------- default lookupswitch

    @Pattern({Symbols.LOOKUPSWITCH /* default */}) /* => POP; GOTO default */
    public static boolean removeLookupswitch(InsnList list, AbstractInsnNode[] matched) {

        LookupSwitchInsnNode i = ((LookupSwitchInsnNode) matched[0]);

        if (i.labels.isEmpty()) {
            list.insertBefore(matched[0], new InsnNode(Opcodes.POP));
            list.set(matched[0], new JumpInsnNode(Opcodes.GOTO, i.dflt));
            return true;
        }

        return false;
    }

    // -------------------------------------------------------------------------------------------- use smaller instructions

    @Pattern({Symbols.INT/*0*/, Symbols.IF_CMP/*l*/}) /* => IF l; */
    public static boolean replaceIfZero(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.getIntValue(matched[0]) == 0) {

            int opcode = Opcodes.IFEQ + (matched[1].getOpcode() - Opcodes.IF_ICMPEQ);
            LabelNode label = ((JumpInsnNode)matched[1]).label;

            list.remove(matched[0]);
            list.set(matched[1], new JumpInsnNode(opcode, label));
            return true;
        }

        return false;
    }

    // -------------------------------------------------------------------------------------------- join same path

    @Pattern({Symbols.IF/*l*/, Symbols.GOTO/*l*/}) /* => POP; GOTO l; */
    public static boolean joinSamePathsIf(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.getLabelNode(matched[0]).equals(InsnUtils.getLabelNode(matched[1]))) {
            list.set(matched[0], new InsnNode(Opcodes.POP));
            return true;
        }

        return false;
    }


    @Pattern({Symbols.IF_CMP/*l*/, Symbols.GOTO/*l*/}) /* => POP2; GOTO l; */
    public static boolean joinSamePathsIfCmp(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.getLabelNode(matched[0]).equals(InsnUtils.getLabelNode(matched[1]))) {
            list.set(matched[0], new InsnNode(Opcodes.POP2));
            return true;
        }

        return false;
    }

    // -------------------------------------------------------------------------------------------- compare null

    @Pattern({Symbols.ACONST_NULL, Symbols.IFNULL/*l*/}) /* => GOTO l; */
    public static boolean compareNull(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        list.set(matched[1], new JumpInsnNode(Opcodes.GOTO, ((JumpInsnNode)matched[1]).label));
        return true;
    }

    @Pattern({Symbols.ACONST_NULL, Symbols.IFNONNULL/*l*/}) /* => nothing */
    public static boolean compareNonNull(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        list.remove(matched[1]);
        return true;
    }

    // -------------------------------------------------------------------------------------------- simplify comparison

    @Pattern({Symbols.INT, Symbols.IF/*l*/}) /* => GOTO l; or nothing */
    public static boolean simplifyIntComparison(InsnList list, AbstractInsnNode[] matched) {

        // true
        if (InsnUtils.compareInt(matched[1], matched[0], InsnUtils.IZERO)) {
            list.remove(matched[0]);
            list.set(matched[1], new JumpInsnNode(Opcodes.GOTO, ((JumpInsnNode)matched[1]).label));
        }
        // false
        else {
            list.remove(matched[0]);
            list.remove(matched[1]);
        }

        return true;
    }

    @Pattern({Symbols.INT, Symbols.INT, Symbols.IF_CMP/*l*/}) /* => GOTO l; or nothing */
    public static boolean simplifyIntIntComparison(InsnList list, AbstractInsnNode[] matched) {

        // true
        if (InsnUtils.compareInt(matched[2], matched[0], matched[1])) {
            list.remove(matched[0]);
            list.remove(matched[1]);
            list.set(matched[2], new JumpInsnNode(Opcodes.GOTO, ((JumpInsnNode)matched[2]).label));
        }
        // false
        else {
            list.remove(matched[0]);
            list.remove(matched[1]);
            list.remove(matched[2]);
        }

        return true;
    }

    @Pattern({Symbols.LONG, Symbols.LONG, Symbols.LCMP /*l*/}) /* => result */
    public static boolean simplifyLongComparison(InsnList list, AbstractInsnNode[] matched) {

        int result = InsnUtils.compareLong(matched[2], matched[0], matched[1]);

        list.remove(matched[0]);
        list.remove(matched[1]);
        list.set(matched[2], new LdcInsnNode(new Integer(result)));
        return true;
    }

    @Pattern({Symbols.FLOAT, Symbols.FLOAT, Symbols.FCMPG /*l*/}) /* => result */
    @Pattern({Symbols.FLOAT, Symbols.FLOAT, Symbols.FCMPL /*l*/}) /* => result */
    public static boolean simplifyFloatComparison(InsnList list, AbstractInsnNode[] matched) {

        int result = InsnUtils.compareFloat(matched[2], matched[0], matched[1]);

        list.remove(matched[0]);
        list.remove(matched[1]);
        list.set(matched[2], new LdcInsnNode(new Integer(result)));
        return true;
    }

    @Pattern({Symbols.DOUBLE, Symbols.DOUBLE, Symbols.DCMPG /*l*/}) /* => result */
    @Pattern({Symbols.DOUBLE, Symbols.DOUBLE, Symbols.DCMPL /*l*/}) /* => result */
    public static boolean simplifyDoubleComparison(InsnList list, AbstractInsnNode[] matched) {

        int result = InsnUtils.compareDouble(matched[2], matched[0], matched[1]);

        list.remove(matched[0]);
        list.remove(matched[1]);
        list.set(matched[2], new LdcInsnNode(new Integer(result)));
        return true;
    }

    // -------------------------------------------------------------------------------------------- compare same values

    @Pattern({Symbols.DUP, Symbols.IF_ICMPEQ/*l*/}) /* => POP; GOTO l */
    @Pattern({Symbols.DUP, Symbols.IF_ICMPLE/*l*/}) /* => POP; GOTO l */
    @Pattern({Symbols.DUP, Symbols.IF_ICMPGE/*l*/}) /* => POP; GOTO l */
    public static boolean removeEquals(InsnList list, AbstractInsnNode[] matched) {
        list.set(matched[0], new InsnNode(Opcodes.POP));
        list.set(matched[1], new JumpInsnNode(Opcodes.GOTO, InsnUtils.getLabelNode(matched[1])));
        return true;
    }

    @Pattern({Symbols.DUP, Symbols.IF_ICMPNE/*l*/}) /* => POP; */
    @Pattern({Symbols.DUP, Symbols.IF_ICMPLT/*l*/}) /* => POP; */
    @Pattern({Symbols.DUP, Symbols.IF_ICMPGT/*l*/}) /* => POP; */
    public static boolean removeNotEquals(InsnList list, AbstractInsnNode[] matched) {
        list.set(matched[0], new InsnNode(Opcodes.POP));
        list.remove(matched[1]);
        return true;
    }


    @Pattern({Symbols.DUP2, Symbols.LCMP}) /* => 0 */
    // DCMP a FCMP cannot be optimized, because NaN != NaN
    public static boolean removeLongEquals(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        list.set(matched[1], new InsnNode(Opcodes.ICONST_0));
        return true;
    }
}
