package jbyco.optimization.simplifications;

import jbyco.optimization.peephole.InsnUtils;
import jbyco.optimization.peephole.Pattern;
import jbyco.optimization.peephole.Symbols;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;

/**
 * Created by vendy on 12.5.16.
 */
public class DuplicationSimplifications {

    // -------------------------------------------------------------------------------------------- dup
    // -------------------------------------------------------------------------------------------- load x, load x

    @Pattern({Symbols.VALUE_TYPE1 /*x*/, Symbols.VALUE_TYPE1 /*x*/}) /* => x; dup; */
    public static boolean simplifyDuplicationType1(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.compareValueType1(matched[0], matched[1])) {
            list.set(matched[1], new InsnNode(Opcodes.DUP));
            return true;
        }

        return false;
    }

    @Pattern({Symbols.VALUE_TYPE2 /*x*/, Symbols.VALUE_TYPE2 /*x*/}) /* => x; dup2; */
    public static boolean simplifyDuplicationType2(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.compareValueType2(matched[0], matched[1])) {
            list.set(matched[1], new InsnNode(Opcodes.DUP2));
            return true;
        }

        return false;
    }

    // -------------------------------------------------------------------------------------------- store x, load x

    @Pattern({Symbols.ASTORE /*x*/, Symbols.ALOAD /*x*/}) /* => DUP; ISTORE x */
    @Pattern({Symbols.ISTORE /*x*/, Symbols.ILOAD /*x*/}) /* => DUP; ISTORE x */
    @Pattern({Symbols.FSTORE /*x*/, Symbols.FLOAD /*x*/}) /* => DUP; FSTORE x */
    public static boolean simplifyStoreLoadType1(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.compareVariables(matched[0], matched[1])) {
            list.insertBefore(matched[0], new InsnNode(Opcodes.DUP));
            list.remove(matched[1]);
            return true;
        }

        return false;
    }

    @Pattern({Symbols.LSTORE /*x*/, Symbols.LLOAD /*x*/}) /* => DUP; LSTORE x */
    @Pattern({Symbols.DSTORE /*x*/, Symbols.DLOAD /*x*/}) /* => DUP; DSTORE x */
    public static boolean simplifyStoreLoadType2(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.compareVariables(matched[0], matched[1])) {
            list.insertBefore(matched[0], new InsnNode(Opcodes.DUP2));
            list.remove(matched[1]);
            return true;
        }

        return false;
    }
}
