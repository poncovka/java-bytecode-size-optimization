package jbyco.optimization.simplifications;

import jbyco.optimization.common.InsnUtils;
import jbyco.optimization.peephole.Pattern;
import jbyco.optimization.peephole.PeepholeAction;
import jbyco.optimization.peephole.Symbols;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * A library of actions to simplify algebraic expressions.
 */
public class AlgebraicSimplifications {

    // -------------------------------------------------------------------------------------------- iinc

    @Pattern({Symbols.IINC /*0*/}) /* => nothing */
    public static class Inc0Simplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (((IincInsnNode) matched[0]).incr == 0) {
                list.remove(matched[0]);
                return true;
            }
            return false;
        }
    }

    @Pattern({Symbols.IINC /*x i*/, Symbols.IINC /*x j*/}) /* => LOAD x; LDC i+j; IADD; STORE x; */
    public static class IncIncToAddReplacement implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            IincInsnNode n1 = (IincInsnNode) matched[0];
            IincInsnNode n2 = (IincInsnNode) matched[1];

            if (n1.var == n2.var) {
                list.insertBefore(matched[0], new VarInsnNode(Opcodes.ILOAD, n1.var));
                list.insertBefore(matched[0], new LdcInsnNode(new Integer(n1.incr + n2.incr)));
                list.set(matched[0], new InsnNode(Opcodes.IADD));
                list.set(matched[1], new VarInsnNode(Opcodes.ISTORE, n1.var));
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.ILOAD /*x*/, Symbols.INT /*i*/, Symbols.IADD, Symbols.ISTORE /*x*/}) /* => IINC x i */
    @Pattern({Symbols.ILOAD /*x*/, Symbols.INT /*i*/, Symbols.ISUB, Symbols.ISTORE /*x*/}) /* => IINC x -i */
    public static class OpWithIncReplacement implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            int var = ((VarInsnNode) matched[0]).var;
            int var2 = ((VarInsnNode) matched[3]).var;
            if (var == var2) {

                int value = InsnUtils.getIntValue(matched[1]);
                if (Symbols.ISUB.match(matched[2])) {
                    value = -value;
                }

                if (Short.MIN_VALUE <= value && value <= Short.MAX_VALUE) {
                    list.set(matched[0], new IincInsnNode(var, value));
                    list.remove(matched[1]);
                    list.remove(matched[2]);
                    list.remove(matched[3]);
                    return true;
                }
            }
            return false;
        }
    }

    // -------------------------------------------------------------------------------------------- x * 0 = 0

    @Pattern({Symbols.INT /*0*/, Symbols.IMUL}) /* => POP; 0 */
    @Pattern({Symbols.INT /*0*/, Symbols.IAND}) /* => POP; 0 */
    public static class IntXOp0Simplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.getIntValue(matched[0]) == 0) {
                list.set(matched[0], new InsnNode(Opcodes.POP));
                list.set(matched[1], new InsnNode(Opcodes.ICONST_0));
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.LONG /*0*/, Symbols.LMUL}) /* => POP2; 0 */
    @Pattern({Symbols.LONG /*0*/, Symbols.LAND}) /* => POP2; 0 */
    public static class LongXOp0Simplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.getLongValue(matched[0]) == 0L) {
                list.set(matched[0], new InsnNode(Opcodes.POP2));
                list.set(matched[1], new InsnNode(Opcodes.LCONST_0));
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.INT /*0*/, Symbols.VALUE_TYPE1, Symbols.IMUL}) /* => 0 */
    @Pattern({Symbols.INT /*0*/, Symbols.VALUE_TYPE1, Symbols.IAND}) /* => 0 */
    public static class Int0OpXSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.getIntValue(matched[0]) == 0) {
                list.remove(matched[1]);
                list.remove(matched[2]);
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.LONG /*0*/, Symbols.VALUE_TYPE2, Symbols.LMUL}) /* => 0 */
    @Pattern({Symbols.LONG /*0*/, Symbols.VALUE_TYPE2, Symbols.LAND}) /* => 0 */
    public static class Long0OpXSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.getLongValue(matched[0]) == 0L) {
                list.remove(matched[1]);
                list.remove(matched[2]);
                return true;
            }

            return false;
        }
    }

    // -------------------------------------------------------------------------------------------- x or ~0 = ~0

    @Pattern({Symbols.INT /*~0*/, Symbols.IOR}) /* => POP; ~0 */
    public static class IntXOrNot0Simplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.getIntValue(matched[0]) == ~0) {
                list.set(matched[0], new InsnNode(Opcodes.POP));
                list.set(matched[1], new LdcInsnNode(new Integer(~0)));
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.LONG /*~0*/, Symbols.LOR}) /* => POP2; ~0 */
    public static class LongXOrNot0Simplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.getLongValue(matched[0]) == ~0L) {
                list.set(matched[0], new InsnNode(Opcodes.POP2));
                list.set(matched[1], new LdcInsnNode(new Long(~0)));
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.INT /*~0*/, Symbols.VALUE_TYPE1, Symbols.IOR}) /* => ~0 */
    public static class IntNot0OrXSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.getIntValue(matched[0]) == ~0) {
                list.remove(matched[1]);
                list.remove(matched[2]);
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.LONG /*~0*/, Symbols.VALUE_TYPE2, Symbols.LOR}) /* => ~0 */
    public static class LongNot0OrXSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.getLongValue(matched[0]) == ~0L) {
                list.remove(matched[1]);
                list.remove(matched[2]);
                return true;
            }

            return false;
        }
    }

    // -------------------------------------------------------------------------------------------- x % 1 = 0

    @Pattern({Symbols.INT /*1*/, Symbols.IREM}) /* => POP; ~0 */
    public static class IntXRem1 implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.getIntValue(matched[0]) == 1) {
                list.set(matched[0], new InsnNode(Opcodes.POP));
                list.set(matched[1], new InsnNode(Opcodes.ICONST_0));
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.LONG /*1*/, Symbols.LREM}) /* => POP2; ~0 */
    public static class LongXRem1Simplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.getLongValue(matched[0]) == 1L) {
                list.set(matched[0], new InsnNode(Opcodes.POP2));
                list.set(matched[1], new InsnNode(Opcodes.LCONST_0));
                return true;
            }

            return false;
        }
    }

    // -------------------------------------------------------------------------------------------- x op x = 0

    @Pattern({Symbols.DUP, Symbols.ISUB}) /* => POP;0 */
    @Pattern({Symbols.DUP, Symbols.IXOR}) /* => POP;0 */
    public static class IntXOpXIs0Simplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.set(matched[0], new InsnNode(Opcodes.POP));
            list.set(matched[1], new InsnNode(Opcodes.ICONST_0));
            return true;
        }
    }

    @Pattern({Symbols.DUP2, Symbols.LSUB}) /* => POP2; 0 */
    @Pattern({Symbols.DUP2, Symbols.LXOR}) /* => POP2; 0 */
    public static class LongXOpXIs0Simplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.set(matched[0], new InsnNode(Opcodes.POP2));
            list.set(matched[1], new InsnNode(Opcodes.LCONST_0));
            return true;
        }
    }

    // -------------------------------------------------------------------------------------------- x op x = x

    @Pattern({Symbols.DUP, Symbols.IAND}) /* => nothing */
    @Pattern({Symbols.DUP, Symbols.IOR}) /* => nothing */
    @Pattern({Symbols.DUP2, Symbols.LAND}) /* => nothing */
    @Pattern({Symbols.DUP2, Symbols.LOR}) /* => nothing */
    public static class XOpXIsXSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.remove(matched[0]);
            list.remove(matched[1]);
            return true;
        }
    }

    // -------------------------------------------------------------------------------------------- x op id = x

    @Pattern({Symbols.INT, Symbols.IBINARYOP}) /* => result */
    public static class IntXOpIdentitySimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.isIntRightIdentity(matched[1], matched[0])) {
                list.remove(matched[0]);
                list.remove(matched[1]);
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.INT, Symbols.VALUE_TYPE1, Symbols.IBINARYOP}) /* => result */
    public static class IntIdentityOpXSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.isIntLeftIdentity(matched[2], matched[0])) {
                list.remove(matched[2]);
                list.remove(matched[0]);
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.LONG, Symbols.LBINARYOP}) /* => result */
    public static class xLongXOpIdentitySimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.isLongRightIdentity(matched[1], matched[0])) {
                list.remove(matched[0]);
                list.remove(matched[1]);
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.LONG, Symbols.VALUE_TYPE2, Symbols.LBINARYOP}) /* => result */
    public static class LongIdentityOpXSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {

            if (InsnUtils.isLongLeftIdentity(matched[2], matched[0])) {
                list.remove(matched[2]);
                list.remove(matched[0]);
                return true;
            }
            return false;
        }
    }

    // -------------------------------------------------------------------------------------------- x op NaN = NaN

    @Pattern({Symbols.LDC /*NaN*/, Symbols.FBINARYOP}) /* => POP, LDC NaN */
    public static class FloatXOpNaNSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.isNaN(matched[0])) {
                list.insertBefore(matched[0], new InsnNode(Opcodes.POP));
                list.remove(matched[1]);
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.LDC /*NaN*/, Symbols.DBINARYOP}) /* => POP2, LDC NaN */
    public static class DoubleXOpNaNSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.isNaN(matched[0])) {
                list.insertBefore(matched[0], new InsnNode(Opcodes.POP2));
                list.remove(matched[1]);
                return true;
            }
            return false;
        }
    }

    @Pattern({Symbols.LDC /*NaN*/, Symbols.VALUE_TYPE1, Symbols.FBINARYOP}) /* => LDC NaN */
    @Pattern({Symbols.LDC /*NaN*/, Symbols.VALUE_TYPE2, Symbols.DBINARYOP}) /* => LDC NaN */
    public static class NaNOpXSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.isNaN(matched[0])) {
                list.remove(matched[1]);
                list.remove(matched[2]);
                return true;
            }
            return false;
        }
    }

    // -------------------------------------------------------------------------------------------- x op y = z

    @Pattern({Symbols.INT, Symbols.IUNARYOP}) /* => result */
    public static class IntXOpSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            int result = InsnUtils.applyIntOperation(matched[1], matched[0]);
            list.set(matched[0], new LdcInsnNode(new Integer(result)));
            list.remove(matched[1]);
            return true;
        }
    }

    @Pattern({Symbols.INT, Symbols.INT, Symbols.IBINARYOP}) /* => result */
    public static class IntXOpYSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {

            // division by zero
            if ((Symbols.IDIV.match(matched[2]) || Symbols.IREM.match(matched[2]))
                    && InsnUtils.getIntValue(matched[1]) == 0) {

                // replace the first operand with one
                if (!Symbols.ICONST_1.match(matched[0])) {
                    list.set(matched[0], new InsnNode(Opcodes.ICONST_1));
                    return true;
                }

                return false;
            }

            // compute the result of the operation
            int result = InsnUtils.applyIntOperation(matched[2], matched[0], matched[1]);
            list.set(matched[0], new LdcInsnNode(new Integer(result)));
            list.remove(matched[1]);
            list.remove(matched[2]);
            return true;
        }
    }

    @Pattern({Symbols.INT, Symbols.DUP, Symbols.IBINARYOP}) /* => result */
    public static class IntXOpXSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            // division by zero
            if ((Symbols.IDIV.match(matched[2]) || Symbols.IREM.match(matched[2]))
                    && InsnUtils.getIntValue(matched[0]) == 0) {

                return false;
            }

            // compute the result of the operation
            int result = InsnUtils.applyIntOperation(matched[2], matched[0], matched[0]);
            list.set(matched[0], new LdcInsnNode(new Integer(result)));
            list.remove(matched[1]);
            list.remove(matched[2]);
            return true;
        }
    }

    @Pattern({Symbols.LONG, Symbols.LUNARYOP}) /* => result */
    public static class LongXOpSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            long result = InsnUtils.applyLongOperation(matched[1], matched[0]);
            list.set(matched[0], new LdcInsnNode(new Long(result)));
            list.remove(matched[1]);
            return true;
        }
    }

    @Pattern({Symbols.LONG, Symbols.LONG, Symbols.LBINARYOP}) /* => result */
    public static class LongXOpYSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {

            // division by zero
            if ((Symbols.LDIV.match(matched[2]) || Symbols.LREM.match(matched[2]))
                    && InsnUtils.getLongValue(matched[1]) == 0) {

                // replace the first operand with one
                if (!Symbols.LCONST_1.match(matched[0])) {
                    list.set(matched[0], new InsnNode(Opcodes.LCONST_1));
                    return true;
                }

                return false;
            }

            // compute the result of the operation
            long result = InsnUtils.applyLongOperation(matched[2], matched[0], matched[1]);
            list.set(matched[0], new LdcInsnNode(new Long(result)));
            list.remove(matched[1]);
            list.remove(matched[2]);
            return true;
        }
    }

    @Pattern({Symbols.LONG, Symbols.DUP2, Symbols.LBINARYOP}) /* => result */
    public static class LongXOpXSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {

            // division by zero
            if ((Symbols.LDIV.match(matched[2]) || Symbols.LREM.match(matched[2]))
                    && InsnUtils.getLongValue(matched[0]) == 0) {

                return false;
            }

            // compute the result of the operation
            long result = InsnUtils.applyLongOperation(matched[2], matched[0], matched[0]);
            list.set(matched[0], new LdcInsnNode(new Long(result)));
            list.remove(matched[1]);
            list.remove(matched[2]);
            return true;
        }
    }

    @Pattern({Symbols.FLOAT, Symbols.FUNARYOP}) /* => result */
    public static class FloatXOpSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            float result = InsnUtils.applyFloatOperation(matched[1], matched[0]);
            list.set(matched[0], new LdcInsnNode(new Float(result)));
            list.remove(matched[1]);
            return true;
        }
    }

    @Pattern({Symbols.FLOAT, Symbols.FLOAT, Symbols.FBINARYOP}) /* => result */
    public static class FloatXOpYSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            float result = InsnUtils.applyFloatOperation(matched[2], matched[0], matched[1]);
            list.set(matched[0], new LdcInsnNode(new Float(result)));
            list.remove(matched[1]);
            list.remove(matched[2]);
            return true;
        }
    }

    @Pattern({Symbols.FLOAT, Symbols.DUP, Symbols.FBINARYOP}) /* => result */
    public static class FloatXOpXSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            float result = InsnUtils.applyFloatOperation(matched[2], matched[0], matched[0]);
            list.set(matched[0], new LdcInsnNode(new Float(result)));
            list.remove(matched[1]);
            list.remove(matched[2]);
            return true;
        }
    }

    @Pattern({Symbols.DOUBLE, Symbols.DUNARYOP}) /* => result */
    public static class DoubleXOpSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            double result = InsnUtils.applyDoubleOperation(matched[1], matched[0]);
            list.set(matched[0], new LdcInsnNode(new Double(result)));
            list.remove(matched[1]);
            return true;
        }
    }

    @Pattern({Symbols.DOUBLE, Symbols.DOUBLE, Symbols.DBINARYOP}) /* => result */
    public static class DoubleXOpYSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            double result = InsnUtils.applyDoubleOperation(matched[2], matched[0], matched[1]);
            list.set(matched[0], new LdcInsnNode(new Double(result)));
            list.remove(matched[1]);
            list.remove(matched[2]);
            return true;
        }
    }

    @Pattern({Symbols.DOUBLE, Symbols.DUP2, Symbols.DBINARYOP}) /* => result */
    public static class DoubleXOpXSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            double result = InsnUtils.applyDoubleOperation(matched[2], matched[0], matched[0]);
            list.set(matched[0], new LdcInsnNode(new Double(result)));
            list.remove(matched[1]);
            list.remove(matched[2]);
            return true;
        }
    }
}
