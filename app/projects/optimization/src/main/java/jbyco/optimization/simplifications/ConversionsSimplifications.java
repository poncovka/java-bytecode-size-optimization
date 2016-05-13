package jbyco.optimization.simplifications;

import jbyco.optimization.peephole.InsnUtils;
import jbyco.optimization.peephole.Pattern;
import jbyco.optimization.peephole.Symbols;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;

/**
 * A library of patterns and actions for conversions simplifications.
 */
public class ConversionsSimplifications {

    @Pattern({Symbols.INT, Symbols.X2Y}) /* => nothing */
    public static boolean convertInt(InsnList list, AbstractInsnNode[] matched) {

        int x = InsnUtils.getIntValue(matched[0]);
        AbstractInsnNode y = null;

        switch (matched[1].getOpcode()) {
            case Opcodes.I2B:
                y = new LdcInsnNode(new Integer((byte)x));
                break;
            case Opcodes.I2C:
                y = new LdcInsnNode(new Integer((char)x));
                break;
            case Opcodes.I2S:
                y = new LdcInsnNode(new Integer((short)x));
                break;
            case Opcodes.I2F:
                y = new LdcInsnNode(new Float((float)x));
                break;
            case Opcodes.I2L:
                y = new LdcInsnNode(new Long((long)x));
                break;
            case Opcodes.I2D:
                y = new LdcInsnNode(new Double((double)x));
                break;
            default:
                throw new IllegalArgumentException("Unexpected opcode.");
        }

        list.set(matched[0], y);
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.LONG, Symbols.X2Y}) /* => nothing */
    public static boolean convertLong(InsnList list, AbstractInsnNode[] matched) {

        long x = InsnUtils.getLongValue(matched[0]);
        AbstractInsnNode y = null;

        switch (matched[1].getOpcode()) {
            case Opcodes.L2I:
                y = new LdcInsnNode(new Integer((int) x));
                break;
            case Opcodes.L2F:
                y = new LdcInsnNode(new Float((float)x));
                break;
            case Opcodes.L2D:
                y = new LdcInsnNode(new Double((double)x));
                break;
            default:
                throw new IllegalArgumentException("Unexpected opcode.");
        }

        list.set(matched[0], y);
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.FLOAT, Symbols.X2Y}) /* => nothing */
    public static boolean convertFloat(InsnList list, AbstractInsnNode[] matched) {

        float x = InsnUtils.getFloatValue(matched[0]);
        AbstractInsnNode y = null;

        switch (matched[1].getOpcode()) {
            case Opcodes.F2I:
                y = new LdcInsnNode(new Integer((int) x));
                break;
            case Opcodes.F2L:
                y = new LdcInsnNode(new Long((long)x));
                break;
            case Opcodes.F2D:
                y = new LdcInsnNode(new Double((double)x));
                break;
            default:
                throw new IllegalArgumentException("Unexpected opcode.");
        }

        list.set(matched[0], y);
        list.remove(matched[1]);
        return true;
    }


    @Pattern({Symbols.DOUBLE, Symbols.X2Y}) /* => nothing */
    public static boolean convertDouble(InsnList list, AbstractInsnNode[] matched) {

        double x = InsnUtils.getDoubleValue(matched[0]);
        AbstractInsnNode y = null;

        switch (matched[1].getOpcode()) {
            case Opcodes.D2I:
                y = new LdcInsnNode(new Integer((int) x));
                break;
            case Opcodes.D2F:
                y = new LdcInsnNode(new Float((float)x));
                break;
            case Opcodes.D2L:
                y = new LdcInsnNode(new Long((long)x));
                break;
            default:
                throw new IllegalArgumentException("Unexpected opcode.");
        }

        list.set(matched[0], y);
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.I2L, Symbols.L2I}) /* => nothing */
    @Pattern({Symbols.F2D, Symbols.D2F}) /* => nothing */
    public static boolean removeConversion(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[0]);
        list.remove(matched[1]);
        return true;
    }

    @Pattern({Symbols.I2B, Symbols.I2B}) /* => i2b */
    @Pattern({Symbols.I2C, Symbols.I2C}) /* => i2c */
    @Pattern({Symbols.I2S, Symbols.I2S}) /* => i2c */
    public static boolean simplifyConversion(InsnList list, AbstractInsnNode[] matched) {
        list.remove(matched[1]);
        return true;
    }
}
