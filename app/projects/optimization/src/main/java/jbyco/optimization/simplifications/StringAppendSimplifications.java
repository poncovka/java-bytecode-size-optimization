package jbyco.optimization.simplifications;

import jbyco.optimization.peephole.InsnUtils;
import jbyco.optimization.peephole.Pattern;
import jbyco.optimization.peephole.Symbols;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * A library of patterns and actions to optimize string concatenation.
 */
public class StringAppendSimplifications {

    @Pattern({Symbols.INVOKESPECIAL/*init*/, Symbols.STRING/*s*/, Symbols.INVOKEVIRTUAL/*append*/}) /* => nothing */
    public static boolean initStringBuilder(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.isStringBuilderInit(matched[0])
                && InsnUtils.isStringAppendMethod(matched[2])) {

            list.remove(matched[1]);
            list.remove(matched[2]);
            list.insertBefore(matched[0], matched[1]);
            list.set(matched[0],
                    new MethodInsnNode(
                            Opcodes.INVOKESPECIAL,
                            "java/lang/StringBuilder",
                            "<init>",
                            "(Ljava/lang/String;)V",
                            false
                    )
            );
            return true;
        }

        return false;
    }

    @Pattern({Symbols.STRING/*""*/, Symbols.INVOKEVIRTUAL/*append*/}) /* => nothing */
    public static boolean appendEmptyString(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.isStringAppendMethod(matched[1])
                && ((String)((LdcInsnNode)matched[0]).cst).isEmpty()) {

            list.remove(matched[0]);
            list.remove(matched[1]);
            return true;
        }

        return false;
    }

    @Pattern({Symbols.STRING/*s1*/, Symbols.INVOKEVIRTUAL/*append*/, Symbols.STRING/*s2*/, Symbols.INVOKEVIRTUAL/*append*/}) /* => s1+s2; append */
    public static boolean appendStrings(InsnList list, AbstractInsnNode[] matched) {

        if (InsnUtils.isStringAppendMethod(matched[1])
                && InsnUtils.isStringAppendMethod(matched[3])) {

            String s1 = (String) ((LdcInsnNode)matched[0]).cst;
            String s2 = (String) ((LdcInsnNode)matched[2]).cst;

            list.remove(matched[0]);
            list.remove(matched[1]);
            list.set(matched[2], new LdcInsnNode(new String(s1+s2)));
            return true;
        }

        return false;
    }
}