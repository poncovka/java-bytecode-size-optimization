package jbyco.optimization.simplifications;

import jbyco.optimization.peephole.InsnUtils;
import jbyco.optimization.peephole.Pattern;
import jbyco.optimization.peephole.Symbols;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * A library with patterns and actions for stack instructions simplifications.
 */
public class StackSimplifications {

    // -------------------------------------------------------------------------------------------- nop

    @Pattern({Symbols.NOP}) /* => nothing */
    public static boolean removeNop(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        return true;
    }

    // -------------------------------------------------------------------------------------------- swap

    @Pattern({Symbols.VALUE_TYPE1 /*i1*/, Symbols.VALUE_TYPE1 /*i2*/, Symbols.SWAP}) /* => i2; i1 */
    public static boolean doSwap(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        list.set(matched[2], matched[0]);
        return true;
    }

    @Pattern({Symbols.DUP, Symbols.SWAP}) /* => DUP */
    public static boolean doDupSwap(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.SWAP, Symbols.SWAP}) /* => nothing */
    public static boolean doSwapSwap(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.VALUE_TYPE1, Symbols.NEW, Symbols.DUP_X1, Symbols.SWAP})          /* => NEW; DUP; VALUE; */
    @Pattern({Symbols.VALUE_TYPE1, Symbols.VALUE_TYPE1, Symbols.DUP_X1, Symbols.SWAP})  /* => VALUE; DUP; VALUE; */
    public static boolean doDupX1Swap(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        list.set(matched[2], new InsnNode(Opcodes.DUP));
        list.set(matched[3],matched[0]);
        return true;
    }

    // -------------------------------------------------------------------------------------------- pop

    @Pattern({Symbols.DUP, Symbols.POP})            /* => nothing */
    @Pattern({Symbols.DUP2, Symbols.POP2})          /* => nothing */
    @Pattern({Symbols.VALUE_TYPE1, Symbols.POP})    /* => nothing */
    @Pattern({Symbols.VALUE_TYPE2, Symbols.POP2})   /* => nothing */
    public static boolean doPop(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.DUP, Symbols.POP2})             /* => POP */
    @Pattern({Symbols.VALUE_TYPE1, Symbols.POP2})     /* => POP */
    public static boolean doPartialPop(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        list.set(matched[1], new InsnNode(Opcodes.POP));
        return true;
    }

    // -------------------------------------------------------------------------------------------- variables

    @Pattern({Symbols.ALOAD /*x*/, Symbols.ASTORE /*x*/}) /* => nothing */
    @Pattern({Symbols.ILOAD /*x*/, Symbols.ISTORE /*x*/}) /* => nothing */
    @Pattern({Symbols.ILOAD /*x*/, Symbols.ISTORE /*x*/}) /* => nothing */
    @Pattern({Symbols.FLOAD /*x*/, Symbols.FSTORE /*x*/}) /* => nothing */
    @Pattern({Symbols.LLOAD /*x*/, Symbols.LSTORE /*x*/}) /* => nothing */
    @Pattern({Symbols.DLOAD /*x*/, Symbols.DSTORE /*x*/}) /* => nothing */
    public static boolean removeLoadStore(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.compareVariables(matched[0], matched[1])) {
            list.remove(matched[0]);
            list.remove(matched[1]);
            return true;
        }

        return false;
    }

    @Pattern({Symbols.ASTORE /*x*/, Symbols.ASTORE /*x*/}) /* => POP;  STORE x */
    @Pattern({Symbols.ISTORE /*x*/, Symbols.ISTORE /*x*/}) /* => POP;  STORE x */
    @Pattern({Symbols.FSTORE /*x*/, Symbols.FSTORE /*x*/}) /* => POP;  STORE x */
    public static boolean simplifyStoreStoreType1(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.compareVariables(matched[0], matched[1])) {
            list.set(matched[0], new InsnNode(Opcodes.POP));
            return true;
        }

        return false;
    }

    @Pattern({Symbols.LSTORE /*x*/, Symbols.LSTORE /*x*/}) /* => POP2; STORE x */
    @Pattern({Symbols.DSTORE /*x*/, Symbols.DSTORE /*x*/}) /* => POP2; STORE x */
    public static boolean simplifyStoreStoreType2(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.compareVariables(matched[0], matched[1])) {
            list.set(matched[0], new InsnNode(Opcodes.POP2));
            return true;
        }

        return false;
    }

    // -------------------------------------------------------------------------------------------- before return


    @Pattern({Symbols.BEFORE_RETURN,                      Symbols.RETURN})  /* => RETURN */
    @Pattern({Symbols.BEFORE_RETURN, Symbols.INT,         Symbols.IRETURN}) /* => INT; IRETURN */
    @Pattern({Symbols.BEFORE_RETURN, Symbols.FLOAT,       Symbols.FRETURN}) /* => FLOAT; FRETURN */
    @Pattern({Symbols.BEFORE_RETURN, Symbols.LONG,        Symbols.LRETURN}) /* => INT; IRETURN */
    @Pattern({Symbols.BEFORE_RETURN, Symbols.DOUBLE,      Symbols.DRETURN}) /* => DOUBLE; DRETURN */
    @Pattern({Symbols.BEFORE_RETURN, Symbols.ACONST_NULL, Symbols.ARETURN}) /* => null; ARETURN */
    public static boolean removeBeforeReturn(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        return true;
    }

    @Pattern({Symbols.DUP,  Symbols.IRETURN}) /* => IRETURN */
    @Pattern({Symbols.DUP,  Symbols.FRETURN}) /* => FRETURN */
    @Pattern({Symbols.DUP2, Symbols.LRETURN}) /* => IRETURN */
    @Pattern({Symbols.DUP2, Symbols.DRETURN}) /* => DRETURN */
    @Pattern({Symbols.DUP,  Symbols.ARETURN}) /* => ARETURN */
    public static boolean removeDupBeforeReturn(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        return true;
    }

    @Pattern({Symbols.BEFORE_RETURN, Symbols.ILOAD, Symbols.IRETURN}) /* => LOAD; IRETURN */
    @Pattern({Symbols.BEFORE_RETURN, Symbols.FLOAD, Symbols.FRETURN}) /* => LOAD; FRETURN */
    @Pattern({Symbols.BEFORE_RETURN, Symbols.LLOAD, Symbols.LRETURN}) /* => LOAD; IRETURN */
    @Pattern({Symbols.BEFORE_RETURN, Symbols.DLOAD, Symbols.DRETURN}) /* => LOAD; DRETURN */
    @Pattern({Symbols.BEFORE_RETURN, Symbols.ALOAD, Symbols.ARETURN}) /* => LOAD; ARETURN */
    public static boolean removeBeforeReturnVariable(InsnList list, AbstractInsnNode[] matched) {

        int opcode = matched[0].getOpcode();
        int var = ((VarInsnNode)matched[1]).var;

        // store to the variable
        if (Opcodes.ISTORE <= opcode && opcode <= Opcodes.ASTORE && ((VarInsnNode)matched[0]).var == var) {
            return false;
        }
        // increment the variable
        else if (opcode == Opcodes.IINC && ((IincInsnNode)matched[0]).var == var) {
            return false;
        }
        // no modification of the variabel, can be removed
        else {
            list.remove(matched[0]);
            return true;
        }
    }
}
