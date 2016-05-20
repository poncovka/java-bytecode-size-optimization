package jbyco.optimization.expansion;

import jbyco.optimization.peephole.Pattern;
import jbyco.optimization.peephole.PeepholeAction;
import jbyco.optimization.peephole.Symbols;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

/**
 * Created by vendy on 12.5.16.
 */
public class DuplicationExpansion {

    // -------------------------------------------------------------------------------------------- dup

    @Pattern({
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.DUP }) /* => x;x; */
    public static class DupExpansion implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.set(matched[1], matched[0].clone(null));
            return true;
        }
    }

    // -------------------------------------------------------------------------------------------- dup_x1

    @Pattern({
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.DUP_X1 }) /* => y;x;y */
    public static class DupX1Expansion implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.insertBefore(matched[0], matched[1].clone(null));
            list.remove(matched[2]);
            return true;
        }
    }

    // -------------------------------------------------------------------------------------------- dup_x2

    @Pattern({
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.VALUE_TYPE1 /*z*/,
            Symbols.DUP_X2 }) /* => z;x;y;z */
    public static class DupX2Form1Expansion implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.insertBefore(matched[0], matched[2].clone(null));
            list.remove(matched[3]);
            return true;
        }
    }

    @Pattern({
            Symbols.VALUE_TYPE2 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.DUP_X2 }) /* => y;x;y */
    public static class DupX2Form2Expansion implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.insertBefore(matched[0], matched[1].clone(null));
            list.remove(matched[2]);
            return true;
        }
    }

    // -------------------------------------------------------------------------------------------- dup2

    @Pattern({
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.DUP2 }) /* => x;y;x;y */
    public static class Dup2Form1Expansion implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.insertBefore(matched[0], matched[0].clone(null));
            list.insertBefore(matched[0], matched[1].clone(null));
            list.remove(matched[2]);
            return true;
        }
    }

    @Pattern({
            Symbols.VALUE_TYPE2 /*x*/,
            Symbols.DUP }) /* => x;x; */
    public static class Dup2Form2Expansion implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.set(matched[1], matched[0].clone(null));
            return true;
        }
    }

    // -------------------------------------------------------------------------------------------- dup2_x1

    @Pattern({
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.VALUE_TYPE1 /*z*/,
            Symbols.DUP2_X1 }) /* => y;z;x;y;z */
    public static class Dup2X1Form1Expansion implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.insertBefore(matched[0], matched[1].clone(null));
            list.insertBefore(matched[0], matched[2].clone(null));
            list.remove(matched[3]);
            return true;
        }
    }

    @Pattern({
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.VALUE_TYPE2 /*y*/,
            Symbols.DUP2_X1 }) /* => y;x;y */
    public static class Dup2X1Form2Expansion implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.insertBefore(matched[0], matched[1].clone(null));
            list.remove(matched[2]);
            return true;
        }
    }

    // -------------------------------------------------------------------------------------------- dup2_x2

    @Pattern({
            Symbols.VALUE_TYPE1 /*w*/,
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.VALUE_TYPE1 /*z*/,
            Symbols.DUP2_X2 }) /* => y;z;w;x;y;z */
    public static class Dup2X2Form1Expansion implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.insertBefore(matched[0], matched[2].clone(null));
            list.insertBefore(matched[0], matched[3].clone(null));
            list.remove(matched[4]);
            return true;
        }
    }

    @Pattern({
            Symbols.VALUE_TYPE1 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.VALUE_TYPE2 /*z*/,
            Symbols.DUP2_X2 }) /* => z;x;y;z */
    public static class Dup2X2Form2Expansion implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.insertBefore(matched[0], matched[2].clone(null));
            list.remove(matched[3]);
            return true;
        }
    }

    @Pattern({
            Symbols.VALUE_TYPE2 /*x*/,
            Symbols.VALUE_TYPE1 /*y*/,
            Symbols.VALUE_TYPE1 /*z*/,
            Symbols.DUP2_X2 }) /* => z;y;x;y;z */
    public static class Dup2X2Form3Expansion implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.insertBefore(matched[0], matched[2].clone(null));
            list.insertBefore(matched[0], matched[1].clone(null));
            list.remove(matched[3]);
            return true;
        }
    }

    @Pattern({
            Symbols.VALUE_TYPE2 /*x*/,
            Symbols.VALUE_TYPE2 /*y*/,
            Symbols.DUP2_X2 }) /* => y;x;y */
    public static class Dup2X2Form4Expansion implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.insertBefore(matched[0], matched[1].clone(null));
            list.remove(matched[2]);
            return true;
        }
    }
}
