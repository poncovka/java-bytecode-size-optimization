package jbyco.optimization.simplifications;

import jbyco.optimization.peephole.InsnUtils;
import jbyco.optimization.peephole.Pattern;
import jbyco.optimization.peephole.Symbols;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;

/**
 * A library of patterns and simplifications for optimizations with variables.
 */
public class VariablesOptimizations {

    // -------------------------------------------------------------------------------------------- load x, store x

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

    // -------------------------------------------------------------------------------------------- store x, store x

    @Pattern({Symbols.ISTORE /*x*/, Symbols.ISTORE /*x*/}) /* => POP;  STORE x */
    @Pattern({Symbols.FSTORE /*x*/, Symbols.FSTORE /*x*/}) /* => POP;  STORE x */
    public static boolean removeIntFloatStoreStore(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.compareVariables(matched[0], matched[1])) {
            list.set(matched[0], new InsnNode(Opcodes.POP));
            return true;
        }

        return false;
    }

    @Pattern({Symbols.LSTORE /*x*/, Symbols.LSTORE /*x*/}) /* => POP2; STORE x */
    @Pattern({Symbols.DSTORE /*x*/, Symbols.DSTORE /*x*/}) /* => POP2; STORE x */
    public static boolean removeStoreStore(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.compareVariables(matched[0], matched[1])) {
            list.set(matched[0], new InsnNode(Opcodes.POP2));
            return true;
        }

        return false;
    }

    // -------------------------------------------------------------------------------------------- load x, load x

    @Pattern({Symbols.ILOAD /*x*/, Symbols.ILOAD /*x*/}) /* => ILOAD; DUP */
    @Pattern({Symbols.FLOAD /*x*/, Symbols.FLOAD /*x*/}) /* => FLOAT; DUP */
    public static boolean duplicateLoadIntFloat(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.compareVariables(matched[0], matched[1])) {
            list.set(matched[1], new InsnNode(Opcodes.DUP));
            return true;
        }

        return false;
    }

    @Pattern({Symbols.LLOAD /*x*/, Symbols.LLOAD /*x*/}) /* => LLOAD x; DUP2 */
    @Pattern({Symbols.DLOAD /*x*/, Symbols.DLOAD /*x*/}) /* => DLOAD x; DUP2 */
    public static boolean duplicateLoadLongDouble(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.compareVariables(matched[0], matched[1])) {
            list.set(matched[1], new InsnNode(Opcodes.DUP2));
            return true;
        }

        return false;
    }

    // -------------------------------------------------------------------------------------------- store x, load x

    @Pattern({Symbols.ISTORE /*x*/, Symbols.ILOAD /*x*/}) /* => DUP; ISTORE x */
    @Pattern({Symbols.FSTORE /*x*/, Symbols.FLOAD /*x*/}) /* => DUP; FSTORE x */
    public static boolean duplicateStoreIntFloat(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.compareVariables(matched[0], matched[1])) {
            list.insertBefore(matched[0], new InsnNode(Opcodes.DUP));
            list.remove(matched[1]);
            return true;
        }

        return false;
    }

    @Pattern({Symbols.LSTORE /*x*/, Symbols.LLOAD /*x*/}) /* => DUP; LSTORE x */
    @Pattern({Symbols.DSTORE /*x*/, Symbols.DLOAD /*x*/}) /* => DUP; DSTORE x */
    public static boolean duplicateStoreLongDouble(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.compareVariables(matched[0], matched[1])) {
            list.insertBefore(matched[0], new InsnNode(Opcodes.DUP2));
            list.remove(matched[1]);
            return true;
        }

        return false;
    }
}
