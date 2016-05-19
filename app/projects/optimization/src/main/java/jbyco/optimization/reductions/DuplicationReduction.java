package jbyco.optimization.reductions;

import jbyco.optimization.peephole.InsnUtils;
import jbyco.optimization.peephole.Pattern;
import jbyco.optimization.peephole.PeepholeAction;
import jbyco.optimization.peephole.Symbols;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;

/**
 * A library of peephole actions for reductions with special dup instructions.
 */
public class DuplicationReduction {

    // -------------------------------------------------------------------------------------------- dup_x1

    @Pattern({
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.VALUE_TYPE1 /*x*/}) /* => y;x;dup_x1 */
    public static class DupX1Reduction implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.compareValueType1(matched[0], matched[2])) {
                list.remove(matched[0]);
                list.insert(matched[2], new InsnNode(Opcodes.DUP_X1));
                return true;
            }
            return false;
        }
    }

    // -------------------------------------------------------------------------------------------- dup_x2

    @Pattern({
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.VALUE_TYPE1 /*z*/,
            Symbols.VALUE_TYPE1 /*x*/}) /* => y;z;x;dup_x2 */
    public static class DupX2Form1Reduction implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.compareValueType1(matched[0], matched[3])) {
                list.remove(matched[0]);
                list.insert(matched[3], new InsnNode(Opcodes.DUP_X2));
                return true;
            }

            return false;
        }
    }

    @Pattern({
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.VALUE_TYPE2 /*y*/,
            Symbols.VALUE_TYPE1 /*x*/}) /* => y;x;dup_x2 */
    public static class DupX2Form2Reduction implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {

            if (InsnUtils.compareValueType1(matched[0], matched[2])) {
                list.remove(matched[0]);
                list.insert(matched[2], new InsnNode(Opcodes.DUP_X2));
                return true;
            }

            return false;
        }
    }

    // -------------------------------------------------------------------------------------------- dup2

    @Pattern({
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.DUP,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.DUP_X1}) /* => x;y;x;y => x;y;dup2  */
    public static class Dup2Reduction implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.remove(matched[1]);
            list.set(matched[3], new InsnNode(Opcodes.DUP2));
            return true;
        }
    }

    // -------------------------------------------------------------------------------------------- dup2_x1

    @Pattern({
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.DUP_X1,
            Symbols.VALUE_TYPE1 /*z*/,
            Symbols.DUP_X2}) /* y;z;x;y;z => x;y;z;dup2_x1  */
    public static class Dup2X1Form1Reduction implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.remove(matched[2]);
            list.set(matched[4], new InsnNode(Opcodes.DUP2_X1));
            return true;
        }
    }


    @Pattern({
            Symbols.VALUE_TYPE2 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.VALUE_TYPE2 /*x*/}) /* => y;x;dup2_x1 */
    public static class Dup2X1Form2Reduction implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.compareValueType2(matched[0], matched[2])) {
                list.remove(matched[0]);
                list.insert(matched[2], new InsnNode(Opcodes.DUP2_X1));
                return true;
            }

            return false;
        }
    }

    // -------------------------------------------------------------------------------------------- dup2_x2


    @Pattern({
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.VALUE_TYPE1 /*v*/,
            Symbols.VALUE_TYPE1 /*w*/,
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/, }) /* => v;w;x;y;dup2_x2 */
    public static class Dup2X2Form1Reduction implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.compareValueType1(matched[0],matched[4])
                    && InsnUtils.compareValueType1(matched[1],matched[5])) {

                list.remove(matched[0]);
                list.remove(matched[1]);
                list.insert(matched[5], new InsnNode(Opcodes.DUP2_X2));
                return true;
            }

            return false;
        }
    }

    @Pattern({
            Symbols.VALUE_TYPE2 /*z*/,
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.VALUE_TYPE2 /*z*/, }) /* => x;y;z;dup2_x2 */
    public static class Dup2X2Form2Reduction implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.compareValueType2(matched[0],matched[3])) {
                list.remove(matched[0]);
                list.insert(matched[3], new InsnNode(Opcodes.DUP2_X2));
                return true;
            }

            return false;
        }
    }

    @Pattern({
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.VALUE_TYPE1 /*z*/,
            Symbols.VALUE_TYPE2 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.VALUE_TYPE1 /*z*/, }) /* => x;y;z;dup2_x2 */
    public static class Dup2X2Form3Reduction implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.compareValueType1(matched[0],matched[3])
                    && InsnUtils.compareValueType1(matched[1],matched[4])) {
                list.remove(matched[0]);
                list.remove(matched[1]);
                list.insert(matched[4], new InsnNode(Opcodes.DUP2_X2));
                return true;
            }

            return false;
        }
    }

    @Pattern({
            Symbols.VALUE_TYPE2 /*y*/,
            Symbols.VALUE_TYPE2 /*x*/,
            Symbols.VALUE_TYPE2 /*y*/ }) /* => x;y;dup2_x2 */
    public static class Dup2X2Form4Reduction implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.compareValueType2(matched[0],matched[2])) {
                list.remove(matched[0]);
                list.insert(matched[2], new InsnNode(Opcodes.DUP2_X2));
                return true;
            }

            return false;
        }
    }
}
