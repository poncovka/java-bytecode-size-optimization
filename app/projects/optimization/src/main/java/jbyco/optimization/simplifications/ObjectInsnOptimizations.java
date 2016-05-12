package jbyco.optimization.simplifications;

import jbyco.optimization.peephole.Pattern;
import jbyco.optimization.peephole.Symbols;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * A library of patterns and actions for a reduction of the operations with objects.
 */
public class ObjectInsnOptimizations {

    @Pattern({Symbols.ACONST_NULL, Symbols.CHECKCAST}) /* => null */
    public static boolean removeNullCheckcast(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.CHECKCAST /*t*/, Symbols.CHECKCAST /*t*/}) /* => checkcast */
    public static boolean removeCheckcast(InsnList list, AbstractInsnNode[] matched) {

        if (((TypeInsnNode)matched[0]).desc.equals(((TypeInsnNode)matched[1]).desc)) {
            list.remove(matched[1]);
            return true;
        }

        return false;
    }

    @Pattern({Symbols.NEW, Symbols.DUP, Symbols.INVOKESPECIAL, Symbols.ATHROW}) /* => null; throw */
    public static boolean throwNull(InsnList list, AbstractInsnNode[] matched) {

        if (((TypeInsnNode)matched[0]).desc.equals("java/lang/NullPointerException")) {

            MethodInsnNode invoke = (MethodInsnNode)matched[2];
            if (invoke.owner.equals("java/lang/NullPointerException")
                    && invoke.name.equals("<init>")
                    && invoke.desc.equals("()V")) {

                list.remove(matched[0]);
                list.remove(matched[1]);
                list.set(matched[2], new InsnNode(Opcodes.ACONST_NULL));
                return true;
            }
        }

        return false;
    }

}