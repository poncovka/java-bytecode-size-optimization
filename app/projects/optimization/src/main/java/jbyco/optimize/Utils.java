package jbyco.optimize;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * Created by vendy on 3.5.16.
 */
public class Utils {


    public static int getIntValue(AbstractInsnNode i) {

        if (Symbols.ICONST.match(i)){
            return i.getOpcode() - Opcodes.ICONST_0;
        }
        else if (Symbols.BIPUSH.match(i) || Symbols.SIPUSH.match(i)) {
            return ((IntInsnNode)i).operand;
        }
        else {
            return ((Integer)((LdcInsnNode)i).cst).intValue();
        }
    }

    public static long getLongValue(AbstractInsnNode i) {

        if (Symbols.LCONST.match(i)){
            return i.getOpcode() - Opcodes.LCONST_0;
        }
        else {
            return ((Long)((LdcInsnNode)i).cst).longValue();
        }
    }

    public static float getFloatValue(AbstractInsnNode i) {

        if (Symbols.FCONST.match(i)){
            return i.getOpcode() - Opcodes.FCONST_0;
        }
        else {
            return ((Float)((LdcInsnNode)i).cst).floatValue();
        }
    }

    public static double getDoubleValue(AbstractInsnNode i) {

        if (Symbols.DCONST.match(i)){
            return i.getOpcode() - Opcodes.DCONST_0;
        }
        else {
            return ((Double)((LdcInsnNode)i).cst).doubleValue();
        }
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
        return ((VarInsnNode)i1).var == ((VarInsnNode)i2).var;
    }

    public static int getIntResult(AbstractInsnNode op, AbstractInsnNode i) {

        int x = getIntValue(i);

        switch(op.getOpcode()) {
            case Opcodes.INEG:  return -x;
            default:            throw new IllegalArgumentException();
        }

    }

    public static int getIntResult(AbstractInsnNode op, AbstractInsnNode i1, AbstractInsnNode i2) {

        int x = getIntValue(i1);
        int y = getIntValue(i2);

        switch(op.getOpcode()) {
            case Opcodes.IADD:  return x + y;
            case Opcodes.ISUB:  return x - y;
            case Opcodes.IMUL:  return x * y;
            case Opcodes.IDIV:  return x / y;
            case Opcodes.IREM:  return x % y;
            case Opcodes.ISHL:  return x << y;
            case Opcodes.ISHR:  return x >> y;
            case Opcodes.IUSHR: return x >>> y;
            case Opcodes.IAND:  return x & y;
            case Opcodes.IOR:   return x | y;
            case Opcodes.IXOR:  return x ^ y;
            default:            throw new IllegalArgumentException();
        }
    }

    public static long getLongResult(AbstractInsnNode op, AbstractInsnNode i) {

        long x = getLongValue(i);

        switch(op.getOpcode()) {
            case Opcodes.LNEG:  return -x;
            default:            throw new IllegalArgumentException();
        }

    }

    public static long getLongResult(AbstractInsnNode op, AbstractInsnNode i1, AbstractInsnNode i2) {

        long x = getLongValue(i1);
        long y = getLongValue(i2);

        switch(op.getOpcode()) {
            case Opcodes.LADD:  return x + y;
            case Opcodes.LSUB:  return x - y;
            case Opcodes.LMUL:  return x * y;
            case Opcodes.LDIV:  return x / y;
            case Opcodes.LREM:  return x % y;
            case Opcodes.LSHL:  return x << y;
            case Opcodes.LSHR:  return x >> y;
            case Opcodes.LUSHR: return x >>> y;
            case Opcodes.LAND:  return x & y;
            case Opcodes.LOR:   return x | y;
            case Opcodes.LXOR:  return x ^ y;
            default:            throw new IllegalArgumentException();
        }
    }

    public static float getFloatResult(AbstractInsnNode op, AbstractInsnNode i) {

        float x = getFloatValue(i);

        switch(op.getOpcode()) {
            case Opcodes.FNEG:  return -x;
            default:            throw new IllegalArgumentException();
        }

    }

    public static float getFloatResult(AbstractInsnNode op, AbstractInsnNode i1, AbstractInsnNode i2) {

        float x = getFloatValue(i1);
        float y = getFloatValue(i2);

        switch(op.getOpcode()) {
            case Opcodes.FADD:  return x + y;
            case Opcodes.FSUB:  return x - y;
            case Opcodes.FMUL:  return x * y;
            case Opcodes.FDIV:  return x / y;
            case Opcodes.FREM:  return x % y;
            default:            throw new IllegalArgumentException();
        }
    }

    public static double getDoubleResult(AbstractInsnNode op, AbstractInsnNode i) {

        double x = getDoubleValue(i);

        switch(op.getOpcode()) {
            case Opcodes.DNEG:  return -x;
            default:            throw new IllegalArgumentException();
        }

    }

    public static double getDoubleResult(AbstractInsnNode op, AbstractInsnNode i1, AbstractInsnNode i2) {

        double x = getDoubleValue(i1);
        double y = getDoubleValue(i2);

        switch(op.getOpcode()) {
            case Opcodes.DADD:  return x + y;
            case Opcodes.DSUB:  return x - y;
            case Opcodes.DMUL:  return x * y;
            case Opcodes.DDIV:  return x / y;
            case Opcodes.DREM:  return x % y;
            default:            throw new IllegalArgumentException();
        }
    }


}
