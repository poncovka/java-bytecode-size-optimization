package jbyco.optimization.common;

import jbyco.optimization.peephole.Symbols;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.PrintWriter;

/**
 * Created by vendy on 3.5.16.
 */
public class InsnUtils {

    public static final int MAX_CODE_SIZE = 65535;
    public static final AbstractInsnNode IZERO = new InsnNode(Opcodes.ICONST_0);

    public static LabelNode getLabelNode(AbstractInsnNode i) {
        return ((JumpInsnNode)i).label;
    }

    public static boolean isNaN(AbstractInsnNode i) {

        if (i.getOpcode() == Opcodes.LDC) {
            Object cst = ((LdcInsnNode)i).cst;

            return (cst instanceof Float && ((Float)cst).isNaN())
                    || (cst instanceof Double && ((Double)cst).isNaN());
        }

        return false;
    }

    public static int getIntValue(AbstractInsnNode i) {

        if (Symbols.ICONST.match(i)) {
            return i.getOpcode() - Opcodes.ICONST_0;
        } else if (Symbols.BIPUSH.match(i) || Symbols.SIPUSH.match(i)) {
            return ((IntInsnNode) i).operand;
        } else {
            return ((Integer) ((LdcInsnNode) i).cst).intValue();
        }
    }

    public static long getLongValue(AbstractInsnNode i) {

        if (Symbols.LCONST.match(i)) {
            return i.getOpcode() - Opcodes.LCONST_0;
        } else {
            return ((Long) ((LdcInsnNode) i).cst).longValue();
        }
    }

    public static float getFloatValue(AbstractInsnNode i) {

        if (Symbols.FCONST.match(i)) {
            return i.getOpcode() - Opcodes.FCONST_0;
        } else {
            return ((Float) ((LdcInsnNode) i).cst).floatValue();
        }
    }

    public static double getDoubleValue(AbstractInsnNode i) {

        if (Symbols.DCONST.match(i)) {
            return i.getOpcode() - Opcodes.DCONST_0;
        } else {
            return ((Double) ((LdcInsnNode) i).cst).doubleValue();
        }
    }

    public static boolean compareStrings(AbstractInsnNode i1, AbstractInsnNode i2) {
        return ((String)((LdcInsnNode)i1).cst).equals(((String)((LdcInsnNode)i2).cst));
    }

    public static boolean compareTypes(AbstractInsnNode i1, AbstractInsnNode i2) {
        return ((Type)((LdcInsnNode)i1).cst).equals((Type)((LdcInsnNode)i2).cst);
    }

    public static boolean compareInt(AbstractInsnNode i1, AbstractInsnNode i2) {
        return getIntValue(i1) == getIntValue(i2);
    }

    public static boolean compareLong(AbstractInsnNode i1, AbstractInsnNode i2) {
        return getLongValue(i1) == getLongValue(i2);
    }

    public static boolean compareFloat(AbstractInsnNode i1, AbstractInsnNode i2) {
        return getFloatValue(i1) == getFloatValue(i2);
    }

    public static boolean compareDouble(AbstractInsnNode i1, AbstractInsnNode i2) {
        return getDoubleValue(i1) == getDoubleValue(i2);
    }

    public static boolean compareVariables(AbstractInsnNode i1, AbstractInsnNode i2) {
        return ((VarInsnNode) i1).var == ((VarInsnNode) i2).var;
    }

    public static boolean compareInt(AbstractInsnNode op, AbstractInsnNode i1, AbstractInsnNode i2) {

        int x = getIntValue(i1);
        int y = getIntValue(i2);

        switch (op.getOpcode()) {
            case Opcodes.IFEQ:
            case Opcodes.IF_ICMPEQ:
                return x == y;
            case Opcodes.IFGE:
            case Opcodes.IF_ICMPGE:
                return x >= y;
            case Opcodes.IFGT:
            case Opcodes.IF_ICMPGT:
                return x > y;
            case Opcodes.IFLE:
            case Opcodes.IF_ICMPLE:
                return x <= y;
            case Opcodes.IFLT:
            case Opcodes.IF_ICMPLT:
                return x < y;
            case Opcodes.IFNE:
            case Opcodes.IF_ICMPNE:
                return x != y;
            default:
                throw new IllegalArgumentException("Unexpected opcode.");
        }
    }

    public static int compareLong(AbstractInsnNode op, AbstractInsnNode i1, AbstractInsnNode i2) {
        return Long.compare(getLongValue(i1),getLongValue(i2));
    }

    public static int compareFloat(AbstractInsnNode op, AbstractInsnNode i1, AbstractInsnNode i2) {

        float x = getFloatValue(i1);
        float y = getFloatValue(i2);

        if (Float.isNaN(x) || Float.isNaN(y)) {

            switch(op.getOpcode()) {
                case Opcodes.FCMPG:
                    return 1;
                case Opcodes.FCMPL:
                    return -1;
            }
        }

        return Float.compare(x,y);
    }

    public static int compareDouble(AbstractInsnNode op, AbstractInsnNode i1, AbstractInsnNode i2) {

        double x = getDoubleValue(i1);
        double y = getDoubleValue(i2);

        if (Double.isNaN(x) || Double.isNaN(y)) {

            switch(op.getOpcode()) {
                case Opcodes.DCMPG:
                    return 1;
                case Opcodes.DCMPL:
                    return -1;
            }
        }

        return Double.compare(x,y);
    }


    public static AbstractInsnNode getIfNotInsn(int opcode, LabelNode label) {

        if (Opcodes.IFEQ <= opcode && opcode <= Opcodes.IF_ACMPNE) {

            // ne, ge, le
            if (opcode % 2 == 0) {
                opcode -= 1;
            }
            // eq, lt, gt
            else {
                opcode += 1;
            }

            return new JumpInsnNode(opcode, label);

        }
        else {
            throw new IllegalArgumentException("Unexpected opcode: " + opcode);
        }
    }

    public static int applyIntOperation(AbstractInsnNode op, AbstractInsnNode i) {

        int x = getIntValue(i);

        switch (op.getOpcode()) {
            case Opcodes.INEG:
                return -x;
            default:
                throw new IllegalArgumentException();
        }

    }

    public static int applyIntOperation(AbstractInsnNode op, AbstractInsnNode i1, AbstractInsnNode i2) {

        int x = getIntValue(i1);
        int y = getIntValue(i2);

        switch (op.getOpcode()) {
            case Opcodes.IADD:
                return x + y;
            case Opcodes.ISUB:
                return x - y;
            case Opcodes.IMUL:
                return x * y;
            case Opcodes.IDIV:
                return x / y;
            case Opcodes.IREM:
                return x % y;
            case Opcodes.ISHL:
                return x << y;
            case Opcodes.ISHR:
                return x >> y;
            case Opcodes.IUSHR:
                return x >>> y;
            case Opcodes.IAND:
                return x & y;
            case Opcodes.IOR:
                return x | y;
            case Opcodes.IXOR:
                return x ^ y;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long applyLongOperation(AbstractInsnNode op, AbstractInsnNode i) {

        long x = getLongValue(i);

        switch (op.getOpcode()) {
            case Opcodes.LNEG:
                return -x;
            default:
                throw new IllegalArgumentException();
        }

    }

    public static long applyLongOperation(AbstractInsnNode op, AbstractInsnNode i1, AbstractInsnNode i2) {

        long x = getLongValue(i1);
        long y = getLongValue(i2);

        switch (op.getOpcode()) {
            case Opcodes.LADD:
                return x + y;
            case Opcodes.LSUB:
                return x - y;
            case Opcodes.LMUL:
                return x * y;
            case Opcodes.LDIV:
                return x / y;
            case Opcodes.LREM:
                return x % y;
            case Opcodes.LSHL:
                return x << y;
            case Opcodes.LSHR:
                return x >> y;
            case Opcodes.LUSHR:
                return x >>> y;
            case Opcodes.LAND:
                return x & y;
            case Opcodes.LOR:
                return x | y;
            case Opcodes.LXOR:
                return x ^ y;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static float applyFloatOperation(AbstractInsnNode op, AbstractInsnNode i) {

        float x = getFloatValue(i);

        switch (op.getOpcode()) {
            case Opcodes.FNEG:
                return -x;
            default:
                throw new IllegalArgumentException();
        }

    }

    public static float applyFloatOperation(AbstractInsnNode op, AbstractInsnNode i1, AbstractInsnNode i2) {

        float x = getFloatValue(i1);
        float y = getFloatValue(i2);

        switch (op.getOpcode()) {
            case Opcodes.FADD:
                return x + y;
            case Opcodes.FSUB:
                return x - y;
            case Opcodes.FMUL:
                return x * y;
            case Opcodes.FDIV:
                return x / y;
            case Opcodes.FREM:
                return x % y;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static double applyDoubleOperation(AbstractInsnNode op, AbstractInsnNode i) {

        double x = getDoubleValue(i);

        switch (op.getOpcode()) {
            case Opcodes.DNEG:
                return -x;
            default:
                throw new IllegalArgumentException();
        }

    }

    public static double applyDoubleOperation(AbstractInsnNode op, AbstractInsnNode i1, AbstractInsnNode i2) {

        double x = getDoubleValue(i1);
        double y = getDoubleValue(i2);

        switch (op.getOpcode()) {
            case Opcodes.DADD:
                return x + y;
            case Opcodes.DSUB:
                return x - y;
            case Opcodes.DMUL:
                return x * y;
            case Opcodes.DDIV:
                return x / y;
            case Opcodes.DREM:
                return x % y;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean isIntRightIdentity(AbstractInsnNode op, AbstractInsnNode i) {

        int x = getIntValue(i);

        switch (op.getOpcode()) {
            case Opcodes.IADD:
                return x == 0;
            case Opcodes.ISUB:
                return x == 0;
            case Opcodes.IMUL:
                return x == 1;
            case Opcodes.IDIV:
                return x == 1;
            case Opcodes.ISHL:
                return x == 0;
            case Opcodes.ISHR:
                return x == 0;
            case Opcodes.IUSHR:
                return x == 0;
            case Opcodes.IAND:
                return x == ~0;
            case Opcodes.IOR:
                return x == 0;
        }

        return false;
    }

    public static boolean isIntLeftIdentity(AbstractInsnNode op, AbstractInsnNode i) {

        switch (op.getOpcode()) {
            case Opcodes.IADD:
            case Opcodes.IMUL:
            case Opcodes.IAND:
            case Opcodes.IOR:
                return isIntRightIdentity(op, i);
        }

        return false;
    }

    public static boolean isLongRightIdentity(AbstractInsnNode op, AbstractInsnNode i) {

        long x = getLongValue(i);

        switch (op.getOpcode()) {
            case Opcodes.LADD:
                return x == 0L;
            case Opcodes.LSUB:
                return x == 0L;
            case Opcodes.LMUL:
                return x == 1L;
            case Opcodes.LDIV:
                return x == 1L;
            case Opcodes.LSHL:
                return x == 0L;
            case Opcodes.LSHR:
                return x == 0L;
            case Opcodes.LUSHR:
                return x == 0L;
            case Opcodes.LAND:
                return x == ~0L;
            case Opcodes.LOR:
                return x == 0L;
        }

        return false;
    }

    public static boolean isLongLeftIdentity(AbstractInsnNode op, AbstractInsnNode i) {

        switch (op.getOpcode()) {
            case Opcodes.LADD:
            case Opcodes.LMUL:
            case Opcodes.LAND:
            case Opcodes.LOR:
                return isLongRightIdentity(op, i);
        }

        return false;
    }

    public static boolean compareValueType1(AbstractInsnNode i1, AbstractInsnNode i2) {

        if (Symbols.ACONST_NULL.match(i1) && Symbols.ACONST_NULL.match(i2)) {
            return true;
        }
        else if (Symbols.INT.match(i1) && Symbols.INT.match(i2)) {
            return compareInt(i1, i2);
        }
        else if (Symbols.FLOAT.match(i1) && Symbols.FLOAT.match(i2)) {
            return compareFloat(i1, i2);
        }
        else if (Symbols.ALOAD.match(i1) && Symbols.ALOAD.match(i2)) {
            return compareVariables(i1, i2);
        }
        else if (Symbols.ILOAD.match(i1) && Symbols.ILOAD.match(i2)) {
            return compareVariables(i1, i2);
        }
        else if (Symbols.FLOAD.match(i1) && Symbols.FLOAD.match(i2)) {
            return compareVariables(i1, i2);
        }
        else if (Symbols.STRING.match(i1) && Symbols.STRING.match(i2)) {
            return compareStrings(i1, i2);
        }
        else if (Symbols.TYPE.match(i1) && Symbols.TYPE.match(i2)) {
            return compareTypes(i1, i2);
        }
        else {
            return false;
        }
    }

    public static boolean compareValueType2(AbstractInsnNode i1, AbstractInsnNode i2) {

        if (Symbols.LONG.match(i1) && Symbols.LONG.match(i2)) {
            return compareLong(i1, i2);
        }
        else if (Symbols.DOUBLE.match(i1) && Symbols.DOUBLE.match(i2)) {
            return compareDouble(i1, i2);
        }
        else if (Symbols.LLOAD.match(i1) && Symbols.LLOAD.match(i2)) {
            return compareVariables(i1, i2);
        }
        else if (Symbols.DLOAD.match(i1) && Symbols.DLOAD.match(i2)) {
            return compareVariables(i1, i2);
        }
        else {
            return false;
        }
    }

    public static boolean isStringBuilderInit(AbstractInsnNode i) {
        MethodInsnNode invoke = (MethodInsnNode)i;

        return      invoke.owner.equals("java/lang/StringBuilder")
                &&  invoke.name.equals("<init>")
                &&  invoke.desc.equals("()V");
    }

    public static boolean isStringBuilderInitString(AbstractInsnNode i) {
        MethodInsnNode invoke = (MethodInsnNode)i;

        return      invoke.owner.equals("java/lang/StringBuilder")
                &&  invoke.name.equals("<init>")
                &&  invoke.desc.equals("(Ljava/lang/String;)V");
    }

    public static boolean isStringAppendMethod(AbstractInsnNode i) {
        MethodInsnNode invoke = (MethodInsnNode)i;

        return      invoke.owner.equals("java/lang/StringBuilder")
                &&  invoke.name.equals("append")
                &&  invoke.desc.equals("(Ljava/lang/String;)Ljava/lang/StringBuilder;");
    }

    public static MethodInsnNode getStringBuilderInit() {

        return new MethodInsnNode(
                Opcodes.INVOKESPECIAL,
                "java/lang/StringBuilder",
                "<init>",
                "(Ljava/lang/String;)V",
                false
        );

    }

    public static void debug(AbstractInsnNode[] array) {
        Printer printer = new Textifier();
        TraceMethodVisitor visitor = new TraceMethodVisitor(printer);

        for (AbstractInsnNode node : array) {
            node.accept(visitor);
        }

        PrintWriter writer = new PrintWriter(System.err);
        printer.print(writer);
        writer.flush();
    }

    public static void debug(InsnList list) {
        Printer printer = new Textifier();
        TraceMethodVisitor visitor = new TraceMethodVisitor(printer);
        list.accept(visitor);
        PrintWriter writer = new PrintWriter(System.err);
        printer.print(writer);
        writer.flush();
    }

    public static void debug(MethodNode method) {
        Printer printer = new Textifier();
        TraceMethodVisitor visitor = new TraceMethodVisitor(printer);
        method.accept(visitor);
        PrintWriter writer = new PrintWriter(System.err);
        printer.print(writer);
        writer.flush();
    }
}
