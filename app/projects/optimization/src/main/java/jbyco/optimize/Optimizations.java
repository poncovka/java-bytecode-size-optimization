package jbyco.optimize;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * The Class Optimizations is a library of optimization methods.
 * Each optimization method has the @Pattern annotation.
 * The expected definition of the pattern method is:
 *
 *   public static boolean method(InsnList list, AbstractInsnNode[] matched);
 *
 * The array of matched instructions contains instructions that match the pattern.
 * The method returns true if the list of instructions was modified, else false.
 *
 */
public class Optimizations {

    // ------------------------------------------------------------------------ REMOVE INSTRUCTIONS

    @Pattern({Symbols.NOP}) /* => nothing */
    public static boolean removeNop(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        return true;
    }

    @Pattern({Symbols.IINC /*0*/}) /* => nothing */
    public static boolean removeIncrementZero(InsnList list, AbstractInsnNode[] matched) {

        int value = ((IincInsnNode)matched[0]).incr;

        if (value == 0) {
            list.remove(matched[0]);
            return true;
        }

        return false;
    }

    @Pattern({Symbols.INT /*0*/, Symbols.IADD}) /* => nothing */
    @Pattern({Symbols.INT /*0*/, Symbols.ISHR}) /* => nothing */
    @Pattern({Symbols.INT /*0*/, Symbols.ISHL}) /* => nothing */
    public static boolean removeIntOpZero(InsnList list, AbstractInsnNode[] matched) {

        int value = Utils.getIntValue(matched[0]);

        if (value == 0) {
            list.remove(matched[0]);
            list.remove(matched[1]);
            return true;
        }

        return false;
    }

    @Pattern({Symbols.LONG /*0*/, Symbols.LADD}) /* => nothing */
    @Pattern({Symbols.LONG /*0*/, Symbols.LSHR}) /* => nothing */
    @Pattern({Symbols.LONG /*0*/, Symbols.ISHL}) /* => nothing */
    public static boolean removeLongOpZero(InsnList list, AbstractInsnNode[] matched) {

        long value = Utils.getLongValue(matched[0]);

        if (value == 0) {
            list.remove(matched[0]);
            list.remove(matched[1]);
            return true;
        }

        return false;
    }

    @Pattern({Symbols.BEFORE_RETURN, Symbols.RETURN}) /* => RETURN */
    public static boolean removeBeforeReturn(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        return true;
    }

    // ------------------------------------------------------------------------ DO THE ACTION

    @Pattern({Symbols.VALUE_TYPE1, Symbols.POP})     /* => nothing */
    @Pattern({Symbols.VALUE_TYPE2, Symbols.POP2})   /* => nothing */
    public static boolean doPop(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.VALUE_TYPE1, Symbols.VALUE_TYPE1, Symbols.POP2}) /* => nothing */
    public static boolean doPop2(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        list.remove(matched[1]);
        list.remove(matched[2]);
        return true;
    }

    @Pattern({Symbols.VALUE_TYPE1 /*i1*/, Symbols.VALUE_TYPE1 /*i2*/, Symbols.SWAP}) /* => i2; i1 */
    public static boolean doSwap(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[1]);
        list.remove(matched[2]);
        list.insertBefore(matched[0], matched[1]);
        return true;
    }

    // TODO LOAD PAR; NEW OBJECT; DUP X1; SWAP; → NEW OBJECT; DUP; LOAD PAR;

    // ------------------------------------------------------------------------ USE MACHINE IDIOMATICS


    @Pattern({Symbols.ILOAD /*x*/, Symbols.INT /*i*/, Symbols.IADD, Symbols.ISTORE /*x*/}) /* => IINC x i */
    public static boolean replaceAddWithInc(InsnList list, AbstractInsnNode[] matched) {

        int var = ((VarInsnNode) matched[0]).var;
        int var2 = ((VarInsnNode) matched[3]).var;

        if (var == var2) {

            int value = Utils.getIntValue(matched[2]);

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

    // ------------------------------------------------------------------------ OPTIMIZE JUMPS

    @Pattern({Symbols.GOTO /*x*/, Symbols.LABEL /*x*/}) /* => LABEL x */
    public static boolean removeJump(InsnList list, AbstractInsnNode[] matched) {

        if (((JumpInsnNode)matched[0]).label.equals(matched[1])) {
            list.remove(matched[0]);
            return true;
        }

        return false;
    }

    @Pattern({Symbols.RETURN, Symbols.NOTLABEL}) /* => RETURN */
    @Pattern({Symbols.GOTO, Symbols.NOTLABEL}) /* => GOTO */
    public static boolean removeAfterJump(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[1]);
        return true;
    }

    // TODO SWITCH - implement class for switch optimizations, the absolute label values are neccessary
    // TODO LABEL x, GOTO y - implement class for jump optimizations
    // TODO GOTO x, ...., LABEL x, iRETURN

    @Pattern({Symbols.LOOKUPSWITCH /* default */}) /* => GOTO default */
    public static boolean removeLookupswitch(InsnList list, AbstractInsnNode[] matched) {

        LookupSwitchInsnNode i = ((LookupSwitchInsnNode)matched[0]);

        if (i.labels.isEmpty()) {
            list.set(matched[0], new JumpInsnNode(Opcodes.GOTO, i.dflt));
            return true;
        }

        return false;
    }


    // TODO LOAD VAR(0); LOAD VAR(0); IFEQ LABEL(0);
    // TODO LOAD VAR(0); LOAD VAR(0); IFNE LABEL(0);
    // TODO IF LABEL(0); GOTO LABEL(0);
    // TODO CONST; CONST; IF LABEL(0)
    // TODO CONST null; IFNULL LABEL(0);
    // TODO

    // ------------------------------------------------------------------------ STRING CONCATENATION

    // TODO append ""
    // TODO StringBuilder(val)
    // TODO append const; appedn const
    // TODO print ""


    // ------------------------------------------------------------------------ OBJECTS

    // TODO CONST null; CHECKCAST ARRAY;
    // TODO CHECKCAST OBJECT(0); CHECKCAST OBJECT(0);
    // TODO CONST null; ATHROW;

    // ------------------------------------------------------------------------ ALGEBRAIC SIMPLIFICATION

    // TODO mistake, result is NaN

    @Pattern({Symbols.LDC /*NaN*/, Symbols.FADD}) /* => NaN */
    @Pattern({Symbols.LDC /*NaN*/, Symbols.DADD}) /* => NaN */
    public static boolean simplifyAddNaN(InsnList list, AbstractInsnNode[] matched) {

        Object value = ((LdcInsnNode) matched[0]).cst;

        if ((value instanceof Double && ((Double)value).isNaN())
                || (value instanceof Float && ((Float)value).isNaN())) {

            list.remove(matched[0]);
            list.remove(matched[1]);
            return true;
        }

        return false;
    }

    @Pattern({Symbols.INT, Symbols.IUNARYOP}) /* => result */
    public static boolean simplifyIntUnaryOp(InsnList list, AbstractInsnNode[] matched) {

        int result = Utils.getIntResult(matched[1], matched[0]);
        list.set(matched[0], new LdcInsnNode(new Integer(result)));
        list.remove(matched[1]);
        return true;
    }


    @Pattern({Symbols.INT, Symbols.INT, Symbols.IBINARYOP}) /* => result */
    public static boolean simplifyIntBinaryOp(InsnList list, AbstractInsnNode[] matched) {


        // division by zero
        if ((Symbols.IDIV.match(matched[2]) || Symbols.IREM.match(matched[2]))
                && Utils.getIntValue(matched[1]) == 0) {

            // replace the first operand with one
            if (!Symbols.ICONST_1.match(matched[0])) {
                list.set(matched[0], new InsnNode(Opcodes.ICONST_1));
                return true;
            }

            return false;
        }

        // compute the result of the operation
        else {

            int result = Utils.getIntResult(matched[2], matched[0], matched[1]);
            list.set(matched[0], new LdcInsnNode(new Integer(result)));
            list.remove(matched[1]);
            list.remove(matched[2]);
            return true;
        }
    }

    @Pattern({Symbols.INT, Symbols.DUP, Symbols.IBINARYOP}) /* => result */
    public static boolean simplifyIntBinaryOp2(InsnList list, AbstractInsnNode[] matched) {

        // division by zero
        if ((Symbols.IDIV.match(matched[2]) || Symbols.IREM.match(matched[2]))
                && Utils.getIntValue(matched[0]) == 0) {

            return false;
        }

        // compute the result of the operation
        else {

            int result = Utils.getIntResult(matched[2], matched[0], matched[0]);
            list.set(matched[0], new LdcInsnNode(new Integer(result)));
            list.remove(matched[1]);
            list.remove(matched[2]);
            return true;
        }
    }

    @Pattern({Symbols.LONG, Symbols.LUNARYOP}) /* => result */
    public static boolean simplifyLongUnaryOp(InsnList list, AbstractInsnNode[] matched) {

        long result = Utils.getLongResult(matched[1], matched[0]);
        list.set(matched[0], new LdcInsnNode(new Long(result)));
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.LONG, Symbols.LONG, Symbols.LBINARYOP}) /* => result */
    public static boolean simplifyLongBinaryOp(InsnList list, AbstractInsnNode[] matched) {

        // division by zero
        if ((Symbols.LDIV.match(matched[2]) || Symbols.LREM.match(matched[2]))
                && Utils.getLongValue(matched[1]) == 0) {

            // replace the first operand with one
            if (!Symbols.LCONST_1.match(matched[0])) {
                list.set(matched[0], new InsnNode(Opcodes.LCONST_1));
                return true;
            }

            return false;
        }

        // compute the result of the operation
        else {

            long result = Utils.getLongResult(matched[2], matched[0], matched[1]);
            list.set(matched[0], new LdcInsnNode(new Long(result)));
            list.remove(matched[1]);
            list.remove(matched[2]);
            return true;
        }
    }

    @Pattern({Symbols.LONG, Symbols.DUP2, Symbols.LBINARYOP}) /* => result */
    public static boolean simplifyLongBinaryOp2(InsnList list, AbstractInsnNode[] matched) {

        // division by zero
        if ((Symbols.LDIV.match(matched[2]) || Symbols.LREM.match(matched[2]))
                && Utils.getLongValue(matched[0]) == 0) {

            return false;
        }

        // compute the result of the operation
        else {

            long result = Utils.getLongResult(matched[2], matched[0], matched[0]);
            list.set(matched[0], new LdcInsnNode(new Long(result)));
            list.remove(matched[1]);
            list.remove(matched[2]);
            return true;
        }
    }

    @Pattern({Symbols.FLOAT, Symbols.FUNARYOP}) /* => result */
    public static boolean simplifyFloatUnaryOp(InsnList list, AbstractInsnNode[] matched) {

        float result = Utils.getFloatResult(matched[1], matched[0]);
        list.set(matched[0], new LdcInsnNode(new Float(result)));
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.FLOAT, Symbols.FLOAT, Symbols.FBINARYOP}) /* => result */
    public static boolean simplifyFloatBinaryOp(InsnList list, AbstractInsnNode[] matched) {

        float result = Utils.getFloatResult(matched[2], matched[0], matched[1]);
        list.set(matched[0], new LdcInsnNode(new Float(result)));
        list.remove(matched[1]);
        list.remove(matched[2]);
        return true;
    }

    @Pattern({Symbols.FLOAT, Symbols.DUP, Symbols.FBINARYOP}) /* => result */
    public static boolean simplifyFloatBinaryOp2(InsnList list, AbstractInsnNode[] matched) {

        float result = Utils.getFloatResult(matched[2], matched[0], matched[0]);
        list.set(matched[0], new LdcInsnNode(new Float(result)));
        list.remove(matched[1]);
        list.remove(matched[2]);
        return true;
    }

    @Pattern({Symbols.DOUBLE, Symbols.DUNARYOP}) /* => result */
    public static boolean simplifyDoubleUnaryOp(InsnList list, AbstractInsnNode[] matched) {

        double result = Utils.getDoubleResult(matched[1], matched[0]);
        list.set(matched[0], new LdcInsnNode(new Double(result)));
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.DOUBLE, Symbols.DOUBLE, Symbols.DBINARYOP}) /* => result */
    public static boolean simplifyDoubleBinaryOp(InsnList list, AbstractInsnNode[] matched) {

        double result = Utils.getDoubleResult(matched[2], matched[0], matched[1]);
        list.set(matched[0], new LdcInsnNode(new Double(result)));
        list.remove(matched[1]);
        list.remove(matched[2]);
        return true;
    }

    @Pattern({Symbols.DOUBLE, Symbols.DUP2, Symbols.DBINARYOP}) /* => result */
    public static boolean simplifyDoubleBinaryOp2(InsnList list, AbstractInsnNode[] matched) {

        double result = Utils.getDoubleResult(matched[2], matched[0], matched[0]);
        list.set(matched[0], new LdcInsnNode(new Double(result)));
        list.remove(matched[1]);
        list.remove(matched[2]);
        return true;
    }


    // ------------------------------------------------------------------------ DUPLICATE

    @Pattern({Symbols.INT, Symbols.INT}) /* => INT; DUP */
    public static boolean duplicateInt(InsnList list, AbstractInsnNode[] matched) {

        if (Utils.compareInt(matched[0], matched[1])) {
            list.set(matched[1], new InsnNode(Opcodes.DUP));
            return true;
        }

        return false;
    }

    @Pattern({Symbols.LONG, Symbols.LONG}) /* => LONG; DUP2 */
    public static boolean duplicateLong(InsnList list, AbstractInsnNode[] matched) {

        if (Utils.compareLong(matched[0], matched[1])) {
            list.set(matched[1], new InsnNode(Opcodes.DUP2));
            return true;
        }

        return false;
    }


    @Pattern({Symbols.FLOAT, Symbols.FLOAT}) /* => FLOAT; DUP */
    public static boolean duplicateFloat(InsnList list, AbstractInsnNode[] matched) {

        if (Utils.compareFloat(matched[0], matched[1])) {
            list.set(matched[1], new InsnNode(Opcodes.DUP));
            return true;
        }

        return false;
    }

    @Pattern({Symbols.DOUBLE, Symbols.DOUBLE}) /* => DOUBLE; DUP2 */
    public static boolean duplicateDouble(InsnList list, AbstractInsnNode[] matched) {

        if (Utils.compareDouble(matched[0], matched[1])) {
            list.set(matched[1], new InsnNode(Opcodes.DUP2));
            return true;
        }

        return false;
    }


    @Pattern({Symbols.ILOAD /*x*/, Symbols.ILOAD /*x*/}) /* => ILOAD; DUP */
    @Pattern({Symbols.FLOAD /*x*/, Symbols.FLOAD /*x*/}) /* => FLOAT; DUP */
    public static boolean duplication1(InsnList list, AbstractInsnNode[] matched) {

        if (Utils.compareVariables(matched[0], matched[1])) {
            list.set(matched[1], new InsnNode(Opcodes.DUP));
            return true;
        }

        return false;
    }

    @Pattern({Symbols.LLOAD /*x*/, Symbols.LLOAD /*x*/}) /* => LLOAD x; DUP2 */
    @Pattern({Symbols.DLOAD /*x*/, Symbols.DLOAD /*x*/}) /* => DLOAD x; DUP2 */
    public static boolean duplication2(InsnList list, AbstractInsnNode[] matched) {

        if (Utils.compareVariables(matched[0], matched[1])) {
            list.set(matched[1], new InsnNode(Opcodes.DUP2));
            return true;
        }

        return false;
    }


    @Pattern({Symbols.ISTORE /*x*/, Symbols.ILOAD /*x*/}) /* => DUP; ISTORE x */
    @Pattern({Symbols.FSTORE /*x*/, Symbols.FLOAD /*x*/}) /* => DUP; FSTORE x */
    public static boolean duplication3(InsnList list, AbstractInsnNode[] matched) {

        if (Utils.compareVariables(matched[0], matched[1])) {
            list.insertBefore(matched[0], new InsnNode(Opcodes.DUP));
            list.remove(matched[1]);
            return true;
        }

        return false;
    }

    @Pattern({Symbols.LSTORE /*x*/, Symbols.LLOAD /*x*/}) /* => DUP; LSTORE x */
    @Pattern({Symbols.DSTORE /*x*/, Symbols.DLOAD /*x*/}) /* => DUP; DSTORE x */
    public static boolean duplication4(InsnList list, AbstractInsnNode[] matched) {

        if (Utils.compareVariables(matched[0], matched[1])) {
            list.insertBefore(matched[0], new InsnNode(Opcodes.DUP2));
            list.remove(matched[1]);
            return true;
        }

        return false;
    }

    // TODO PUTSTATIC OBJECT(0) FIELD(0); GETSTATIC OBJECT(0) FIELD(0); → DUP; PUTSTATIC OBJECT(0) FIELD(0);
    // TODO LOAD VAR(0); LOAD VAR(1); LOAD VAR(0); → LOAD VAR(1); LOAD VAR(0); DUP X;
    // TODO LOAD VAR(0); LOAD VAR(1); LOAD VAR(0); LOAD VAR(1); → LOAD VAR(0); LOAD VAR(1); DUP2;




}
