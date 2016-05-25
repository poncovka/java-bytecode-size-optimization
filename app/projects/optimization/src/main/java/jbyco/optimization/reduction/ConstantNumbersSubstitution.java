package jbyco.optimization.reduction;

import jbyco.lib.Utils;
import jbyco.optimization.Statistics;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * The method adapter for constant numbers substitution.
 */
public class ConstantNumbersSubstitution extends MethodVisitor {

    public ConstantNumbersSubstitution(MethodVisitor visitor, Statistics stats) {
        super(Opcodes.ASM5, visitor);

        if (stats != null) {
            stats.addOptimization(this.getClass().getSimpleName());
        }
    }

    public void visitOptimalInt(int operand) {

        // ICONST
        if (operand >= -1 && operand <= 5) {
            mv.visitInsn(Opcodes.ICONST_0 + operand);
        }
        // BIPUSH
        else if (operand >= Byte.MIN_VALUE && operand <= Byte.MAX_VALUE) {
            mv.visitIntInsn(Opcodes.BIPUSH, operand);
        }
        // SIPUSH
        else if (operand >= Short.MIN_VALUE && operand <= Short.MAX_VALUE) {
            mv.visitIntInsn(Opcodes.SIPUSH, operand);
        }
        // LDC I
        else {
            mv.visitLdcInsn(new Integer(operand));
        }
    }

    public void visitOptimalLong(long operand) {

        // LCONST
        if (operand >= 0 && operand <= 1) {
            mv.visitInsn(Opcodes.LCONST_0 + (int) operand);
        }
        // ICONST; I2L;
        // BIPUSH; I2L;
        // SIPUSH; I2L;
        // LDC I;  I2L; ?
        else if (operand >= Integer.MIN_VALUE && operand <= Integer.MAX_VALUE) {
            visitOptimalInt((int) operand);
            mv.visitInsn(Opcodes.I2L);
        }
        // LDC L
        else {
            mv.visitLdcInsn(new Long(operand));
        }
    }

    public void visitOptimalFloat(float operand) {

        // is NAN?
        if (Float.isNaN(operand)) {

            // LDC F
            mv.visitLdcInsn(new Float(operand));

        }
        // is infinite?
        else if (Float.isInfinite(operand)) {

            // LDC F
            mv.visitLdcInsn(new Float(operand));

        }
        // is finite?
        else {

            // FCONST
            if (operand == 0.0 || operand == 1.0 || operand == 2.0) {
                mv.visitInsn(Opcodes.FCONST_0 + (int) operand);
            }

            // ICONST; I2F;
            // BIPUSH; I2F; ?
            // SIPUSH; I2F; ?
            else if (Utils.isMathInteger(operand)
                    && operand >= Short.MIN_VALUE && operand <= Short.MAX_VALUE) {

                visitOptimalInt((int) operand);
                mv.visitInsn(Opcodes.I2F);
            }

            // LDC F
            else {
                mv.visitLdcInsn(new Float(operand));
            }
        }
    }

    public void visitOptimalDouble(double operand) {

        // is NAN?
        if (Double.isNaN(operand)) {

            // DCONST_0; DUP2; DDIV;
            mv.visitInsn(Opcodes.DCONST_0);
            mv.visitInsn(Opcodes.DUP2);
            mv.visitInsn(Opcodes.DDIV);

        }
        // is infinite?
        else if (Double.isInfinite(operand)) {

            // LDC D
            mv.visitLdcInsn(new Double(operand));

        }
        // is finite?
        else {

            // DCONST
            if (operand == 0.0 || operand == 1.0) {
                mv.visitInsn(Opcodes.DCONST_0 + (int) operand);
            }

            // ICONST; I2D;
            // BIPUSH; I2D;
            // SIPUSH; I2D;
            // LDC I;  I2D; ?
            else if (Utils.isMathInteger(operand)
                    && operand >= Integer.MIN_VALUE && operand <= Integer.MAX_VALUE) {

                visitOptimalInt((int) operand);
                mv.visitInsn(Opcodes.I2D);
            }

            // LDC F; F2D; ?
            else if (operand >= Float.MIN_VALUE && operand <= Float.MAX_VALUE) {
                mv.visitLdcInsn(new Float(operand));
                mv.visitInsn(Opcodes.F2D);
            }

            // LDC D
            else {
                mv.visitLdcInsn(new Double(operand));
            }
        }
    }

    @Override
    public void visitInsn(int opcode) {
        // nothing, all instructions for constant numbers are optimal
        mv.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {

        // SIPUSH, BIPUSH substitution
        if (opcode == Opcodes.SIPUSH || opcode == Opcodes.BIPUSH) {
            visitOptimalInt(operand);
        }
        // or nothing
        else {
            mv.visitIntInsn(opcode, operand);
        }
    }


    @Override
    public void visitLdcInsn(Object cst) {

        // LDC I substitution
        if (cst instanceof Integer) {
            visitOptimalInt(((Integer) cst).intValue());
        }
        // LDC L substitution
        else if (cst instanceof Long) {
            visitOptimalLong(((Long) cst).longValue());
        }
        // LDC F substitution
        else if (cst instanceof Float) {
            visitOptimalFloat(((Float) cst).floatValue());
        }
        // LDC D substitution
        else if (cst instanceof Double) {
            visitOptimalDouble(((Double) cst).doubleValue());
        }
        // or nothing
        else {
            mv.visitLdcInsn(cst);
        }
    }


}
