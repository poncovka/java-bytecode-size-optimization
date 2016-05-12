package jbyco.optimization.simplifications;

import jbyco.optimization.peephole.InsnUtils;
import jbyco.optimization.peephole.Pattern;
import jbyco.optimization.peephole.Symbols;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * Created by vendy on 11.5.16.
 */
public class JumpOptimizations {


    // TODO CONST null; IFNULL LABEL(0);
    // TODO IFNONNULL
    // TODO DUP; CMP
    // TODO const; const; cmp
    // TODO const; ifx;



    // -------------------------------------------------------------------------------------------- remove

    @Pattern({Symbols.GOTO /*x*/, Symbols.LABEL /*x*/}) /* => LABEL x */
    public static boolean removeJump(InsnList list, AbstractInsnNode[] matched) {

        if (((JumpInsnNode) matched[0]).label.equals(matched[1])) {
            list.remove(matched[0]);
            return true;
        }

        return false;
    }

    @Pattern({Symbols.LABEL, Symbols.FRAME, Symbols.LABEL}) /* => LABEL, LABEL */
    public static boolean removeFrame(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.RETURN, Symbols.NOTLABEL}) /* => RETURN */
    @Pattern({Symbols.GOTO, Symbols.NOTLABEL}) /* => GOTO */
    public static boolean removeAfterJump(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[1]);
        return true;
    }

    // -------------------------------------------------------------------------------------------- lookupswitch

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

    // -------------------------------------------------------------------------------------------- simplify comparison

    // TODO for other data types

    @Pattern({Symbols.DUP, Symbols.IF_ICMPEQ/*l*/}) /* => POP; GOTO l */
    @Pattern({Symbols.DUP, Symbols.IF_ICMPLE/*l*/}) /* => POP; GOTO l */
    @Pattern({Symbols.DUP, Symbols.IF_ICMPGE/*l*/}) /* => POP; GOTO l */
    public static boolean removeDupEquals(InsnList list, AbstractInsnNode[] matched) {
        list.set(matched[0], new InsnNode(Opcodes.POP));
        list.set(matched[1], new JumpInsnNode(Opcodes.GOTO, InsnUtils.getLabelNode(matched[1])));
        return true;
    }

    @Pattern({Symbols.DUP, Symbols.IF_ICMPNE/*l*/}) /* => POP; */
    @Pattern({Symbols.DUP, Symbols.IF_ICMPLT/*l*/}) /* => POP; */
    @Pattern({Symbols.DUP, Symbols.IF_ICMPGT/*l*/}) /* => POP; */
    public static boolean removeIntVarsNotEquals(InsnList list, AbstractInsnNode[] matched) {
        list.set(matched[0], new InsnNode(Opcodes.POP));
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.IF_CMP/*l*/, Symbols.GOTO/*l*/}) /* => POP2; GOTO l; */
    public static boolean removeIfWithSamePaths(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.getLabelNode(matched[0]).equals(InsnUtils.getLabelNode(matched[1]))) {
            list.set(matched[0], new InsnNode(Opcodes.POP2));
            return true;
        }

        return false;
    }
}
