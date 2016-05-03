package jbyco.optimize;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;

/**
 * Created by vendy on 2.5.16.
 */
public enum Symbols implements Symbol {

    // ------------------------------------------------------------------------ SYMBOLS FOR OPCODES

    /**
     * The nop symbol.
     */
    NOP(Opcodes.NOP),

    /**
     * The aconst symbol.
     */
    ACONST_NULL(Opcodes.ACONST_NULL),

    /**
     * The iconst symbols.
     */
    ICONST_M1(Opcodes.ICONST_M1),
    ICONST_0(Opcodes.ICONST_0),
    ICONST_1(Opcodes.ICONST_1),
    ICONST_2(Opcodes.ICONST_2),
    ICONST_3(Opcodes.ICONST_3),
    ICONST_4(Opcodes.ICONST_4),
    ICONST_5(Opcodes.ICONST_5),

    /**
     * The lconst symbols.
     */
    LCONST_0(Opcodes.LCONST_0),
    LCONST_1(Opcodes.LCONST_1),

    /**
     * The fconst symbols.
     */
    FCONST_0(Opcodes.FCONST_0),
    FCONST_1(Opcodes.FCONST_1),
    FCONST_2(Opcodes.FCONST_2),

    /**
     * The dconst symbols.
     */
    DCONST_0(Opcodes.DCONST_0),
    DCONST_1(Opcodes.DCONST_1),

    /**
     * The bipush symbol.
     */
    BIPUSH(Opcodes.BIPUSH),

    /**
     * The sipush symbol.
     */
    SIPUSH(Opcodes.SIPUSH),

    /**
     * The ldc symbol.
     */
    LDC(Opcodes.LDC),

    /**
     * The iload symbol.
     */
    ILOAD(Opcodes.ILOAD),

    /**
     * The lload symbol.
     */
    LLOAD(Opcodes.LLOAD),

    /**
     * The fload symbol.
     */
    FLOAD(Opcodes.FLOAD),

    /**
     * The dload symbol.
     */
    DLOAD(Opcodes.DLOAD),

    /**
     * The aload symbol.
     */
    ALOAD(Opcodes.ALOAD),

    /**
     * The iaload symbol.
     */
    IALOAD(Opcodes.IALOAD),

    /**
     * The laload symbol.
     */
    LALOAD(Opcodes.LALOAD),

    /**
     * The faload symbol.
     */
    FALOAD(Opcodes.FALOAD),

    /**
     * The daload symbol.
     */
    DALOAD(Opcodes.DALOAD),

    /**
     * The aaload symbol.
     */
    AALOAD(Opcodes.AALOAD),

    /**
     * The baload symbol.
     */
    BALOAD(Opcodes.BALOAD),

    /**
     * The caload symbol.
     */
    CALOAD(Opcodes.CALOAD),

    /**
     * The saload symbol.
     */
    SALOAD(Opcodes.SALOAD),

    /**
     * The istore symbol.
     */
    ISTORE(Opcodes.ISTORE),

    /**
     * The lstore symbol.
     */
    LSTORE(Opcodes.LSTORE),

    /**
     * The fstore symbol.
     */
    FSTORE(Opcodes.FSTORE),

    /**
     * The dstore symbol.
     */
    DSTORE(Opcodes.DSTORE),

    /**
     * The astore symbol.
     */
    ASTORE(Opcodes.ASTORE),

    /**
     * The iastore symbol.
     */
    IASTORE(Opcodes.IASTORE),

    /**
     * The lastore symbol.
     */
    LASTORE(Opcodes.LASTORE),

    /**
     * The fastore symbol.
     */
    FASTORE(Opcodes.FASTORE),

    /**
     * The dastore symbol.
     */
    DASTORE(Opcodes.DASTORE),

    /**
     * The aastore symbol.
     */
    AASTORE(Opcodes.AASTORE),

    /**
     * The bastore symbol.
     */
    BASTORE(Opcodes.BASTORE),

    /**
     * The castore symbol.
     */
    CASTORE(Opcodes.CASTORE),

    /**
     * The sastore symbol.
     */
    SASTORE(Opcodes.SASTORE),

    /**
     * The pop symbol.
     */
    POP(Opcodes.POP),

    /**
     * The pop2 symbol.
     */
    POP2(Opcodes.POP2),

    /**
     * The dup symbol.
     */
    DUP(Opcodes.DUP),

    /**
     * The dup_x1 symbol.
     */
    DUP_X1(Opcodes.DUP_X1),

    /**
     * The dup_x2 symbol.
     */
    DUP_X2(Opcodes.DUP_X2),

    /**
     * The dup2 symbol.
     */
    DUP2(Opcodes.DUP2),

    /**
     * The dup2_x1 symbol.
     */
    DUP2_X1(Opcodes.DUP2_X1),

    /**
     * The dup2_x2 symbol.
     */
    DUP2_X2(Opcodes.DUP2_X2),

    /**
     * The swap symbol.
     */
    SWAP(Opcodes.SWAP),

    /**
     * The iadd symbol.
     */
    IADD(Opcodes.IADD),

    /**
     * The ladd symbol.
     */
    LADD(Opcodes.LADD),

    /**
     * The fadd symbol.
     */
    FADD(Opcodes.FADD),

    /**
     * The dadd symbol.
     */
    DADD(Opcodes.DADD),

    /**
     * The isub symbol.
     */
    ISUB(Opcodes.ISUB),

    /**
     * The lsub symbol.
     */
    LSUB(Opcodes.LSUB),

    /**
     * The fsub symbol.
     */
    FSUB(Opcodes.FSUB),

    /**
     * The dsub symbol.
     */
    DSUB(Opcodes.DSUB),

    /**
     * The imul symbol.
     */
    IMUL(Opcodes.IMUL),

    /**
     * The lmul symbol.
     */
    LMUL(Opcodes.LMUL),

    /**
     * The fmul symbol.
     */
    FMUL(Opcodes.FMUL),

    /**
     * The dmul symbol.
     */
    DMUL(Opcodes.DMUL),

    /**
     * The idiv symbol.
     */
    IDIV(Opcodes.IDIV),

    /**
     * The ldiv symbol.
     */
    LDIV(Opcodes.LDIV),

    /**
     * The fdiv symbol.
     */
    FDIV(Opcodes.FDIV),

    /**
     * The ddiv symbol.
     */
    DDIV(Opcodes.DDIV),

    /**
     * The irem symbol.
     */
    IREM(Opcodes.IREM),

    /**
     * The lrem symbol.
     */
    LREM(Opcodes.LREM),

    /**
     * The frem symbol.
     */
    FREM(Opcodes.FREM),

    /**
     * The drem symbol.
     */
    DREM(Opcodes.DREM),

    /**
     * The ineg symbol.
     */
    INEG(Opcodes.INEG),

    /**
     * The lneg symbol.
     */
    LNEG(Opcodes.LNEG),

    /**
     * The fneg symbol.
     */
    FNEG(Opcodes.FNEG),

    /**
     * The dneg symbol.
     */
    DNEG(Opcodes.DNEG),

    /**
     * The ishl symbol.
     */
    ISHL(Opcodes.ISHL),

    /**
     * The lshl symbol.
     */
    LSHL(Opcodes.LSHL),

    /**
     * The ishr symbol.
     */
    ISHR(Opcodes.ISHR),

    /**
     * The lshr symbol.
     */
    LSHR(Opcodes.LSHR),

    /**
     * The iushr symbol.
     */
    IUSHR(Opcodes.IUSHR),

    /**
     * The lushr symbol.
     */
    LUSHR(Opcodes.LUSHR),

    /**
     * The iand symbol.
     */
    IAND(Opcodes.IAND),

    /**
     * The land symbol.
     */
    LAND(Opcodes.LAND),

    /**
     * The ior symbol.
     */
    IOR(Opcodes.IOR),

    /**
     * The lor symbol.
     */
    LOR(Opcodes.LOR),

    /**
     * The ixor symbol.
     */
    IXOR(Opcodes.IXOR),

    /**
     * The lxor symbol.
     */
    LXOR(Opcodes.LXOR),

    /**
     * The iinc symbol.
     */
    IINC(Opcodes.IINC),

    /**
     * The i2l symbol.
     */
    I2L(Opcodes.I2L),

    /**
     * The i2f symbol.
     */
    I2F(Opcodes.I2F),

    /**
     * The i2d symbol.
     */
    I2D(Opcodes.I2D),

    /**
     * The l2i symbol.
     */
    L2I(Opcodes.L2I),

    /**
     * The l2f symbol.
     */
    L2F(Opcodes.L2F),

    /**
     * The l2d symbol.
     */
    L2D(Opcodes.L2D),

    /**
     * The f2i symbol.
     */
    F2I(Opcodes.F2I),

    /**
     * The f2l symbol.
     */
    F2L(Opcodes.F2L),

    /**
     * The f2d symbol.
     */
    F2D(Opcodes.F2D),

    /**
     * The f2i symbol.
     */
    D2I(Opcodes.D2I),

    /**
     * The d2l symbol.
     */
    D2L(Opcodes.D2L),

    /**
     * The d2f symbol.
     */
    D2F(Opcodes.D2F),

    /**
     * The i2b symbol.
     */
    I2B(Opcodes.I2B),

    /**
     * The i2c symbol.
     */
    I2C(Opcodes.I2C),

    /**
     * The i2s symbol.
     */
    I2S(Opcodes.I2S),

    /**
     * The lcmp symbol.
     */
    LCMP(Opcodes.LCMP),

    /**
     * The fcmpl symbol.
     */
    FCMPL(Opcodes.FCMPL),

    /**
     * The fcmpg symbol.
     */
    FCMPG(Opcodes.FCMPG),

    /**
     * The dcmpl symbol.
     */
    DCMPL(Opcodes.DCMPL),

    /**
     * The dcmpg symbol.
     */
    DCMPG(Opcodes.DCMPG),

    /**
     * The ifeq symbol.
     */
    IFEQ(Opcodes.IFEQ),

    /**
     * The ifne symbol.
     */
    IFNE(Opcodes.IFNE),

    /**
     * The iflt symbol.
     */
    IFLT(Opcodes.IFLT),

    /**
     * The ifge symbol.
     */
    IFGE(Opcodes.IFGE),

    /**
     * The ifgt symbol.
     */
    IFGT(Opcodes.IFGT),

    /**
     * The ifle symbol.
     */
    IFLE(Opcodes.IFLE),

    /**
     * The if icmpeq symbol.
     */
    IF_ICMPEQ(Opcodes.IF_ICMPEQ),

    /**
     * The if icmpne symbol.
     */
    IF_ICMPNE(Opcodes.IF_ICMPNE),

    /**
     * The if icmplt symbol.
     */
    IF_ICMPLT(Opcodes.IF_ICMPLT),

    /**
     * The if icmpge symbol.
     */
    IF_ICMPGE(Opcodes.IF_ICMPGE),

    /**
     * The if icmpgt symbol.
     */
    IF_ICMPGT(Opcodes.IF_ICMPGT),

    /**
     * The if icmple symbol.
     */
    IF_ICMPLE(Opcodes.IF_ICMPLE),

    /**
     * The if acmpeq symbol.
     */
    IF_ACMPEQ(Opcodes.IF_ACMPEQ),

    /**
     * The if acmpne symbol.
     */
    IF_ACMPNE(Opcodes.IF_ACMPNE),

    /**
     * The goto symbol.
     */
    GOTO(Opcodes.GOTO),

    /**
     * The jsr symbol.
     */
    JSR(Opcodes.JSR),

    /**
     * The ret symbol.
     */
    RET(Opcodes.RET),

    /**
     * The tableswitch symbol.
     */
    TABLESWITCH(Opcodes.TABLESWITCH),

    /**
     * The lookupswitch symbol.
     */
    LOOKUPSWITCH(Opcodes.LOOKUPSWITCH),

    /**
     * The ireturn symbol.
     */
    IRETURN(Opcodes.IRETURN),

    /**
     * The lreturn symbol.
     */
    LRETURN(Opcodes.LRETURN),

    /**
     * The freturn symbol.
     */
    FRETURN(Opcodes.FRETURN),

    /**
     * The dreturn symbol.
     */
    DRETURN(Opcodes.DRETURN),

    /**
     * The areturn symbol.
     */
    ARETURN(Opcodes.ARETURN),

    /**
     * The return symbol.
     */
    RETURN(Opcodes.RETURN),

    /**
     * The getstatic symbol.
     */
    GETSTATIC(Opcodes.GETSTATIC),

    /**
     * The putstatic symbol.
     */
    PUTSTATIC(Opcodes.PUTSTATIC),

    /**
     * The getfield symbol.
     */
    GETFIELD(Opcodes.GETFIELD),

    /**
     * The putfield symbol.
     */
    PUTFIELD(Opcodes.PUTFIELD),

    /**
     * The invokevirtual symbol.
     */
    INVOKEVIRTUAL(Opcodes.INVOKEVIRTUAL),

    /**
     * The invokespecial symbol.
     */
    INVOKESPECIAL(Opcodes.INVOKESPECIAL),

    /**
     * The invokestatic symbol.
     */
    INVOKESTATIC(Opcodes.INVOKESTATIC),

    /**
     * The invokeinterface symbol.
     */
    INVOKEINTERFACE(Opcodes.INVOKEINTERFACE),

    /**
     * The invokedynamic symbol.
     */
    INVOKEDYNAMIC(Opcodes.INVOKEDYNAMIC),

    /**
     * The new symbol.
     */
    NEW(Opcodes.NEW),

    /**
     * The newarray symbol.
     */
    NEWARRAY(Opcodes.NEWARRAY),

    /**
     * The anewarray symbol.
     */
    ANEWARRAY(Opcodes.ANEWARRAY),

    /**
     * The arraylength symbol.
     */
    ARRAYLENGTH(Opcodes.ARRAYLENGTH),

    /**
     * The athrow symbol.
     */
    ATHROW(Opcodes.ATHROW),

    /**
     * The checkcast symbol.
     */
    CHECKCAST(Opcodes.CHECKCAST),

    /**
     * The instanceof symbol.
     */
    INSTANCEOF(Opcodes.INSTANCEOF),

    /**
     * The monitorenter symbol.
     */
    MONITORENTER(Opcodes.MONITORENTER),

    /**
     * The monitorexit symbol.
     */
    MONITOREXIT(Opcodes.MONITOREXIT),

    /**
     * The multianewarray symbol.
     */
    MULTIANEWARRAY(Opcodes.MULTIANEWARRAY),

    /**
     * The ifnull symbol.
     */
    IFNULL(Opcodes.IFNULL),

    /**
     * The ifnonnull symbol.
     */
    IFNONNULL(Opcodes.IFNONNULL),

    // ------------------------------------------------------------------------ SPECIAL SYMBOLS

    /**
     * Any symbol.
     */
    ANY {
        @Override
        public boolean match(AbstractInsnNode i) {
            return true;
        }
    },

    LABEL {
        @Override
        public boolean match(AbstractInsnNode i) {
            return i instanceof LabelNode;
        }
    },

    NOTLABEL {
        @Override
        public boolean match(AbstractInsnNode i) {
            return !(i instanceof LabelNode);
        }
    },

    ICONST {
        @Override
        public boolean match(AbstractInsnNode i) {
            switch(i.getOpcode()) {
                case Opcodes.ICONST_M1:
                case Opcodes.ICONST_0:
                case Opcodes.ICONST_1:
                case Opcodes.ICONST_2:
                case Opcodes.ICONST_3:
                case Opcodes.ICONST_4:
                case Opcodes.ICONST_5:  return true;
                default:                return false;
            }
        }
    },

    LCONST {
        @Override
        public boolean match(AbstractInsnNode i) {
            switch(i.getOpcode()) {
                case Opcodes.LCONST_0:
                case Opcodes.LCONST_1:  return true;
                default:                return false;
            }
        }
    },

    FCONST {
        @Override
        public boolean match(AbstractInsnNode i) {
            switch(i.getOpcode()) {
                case Opcodes.FCONST_0:
                case Opcodes.FCONST_1:
                case Opcodes.FCONST_2:  return true;
                default:                return false;
            }
        }
    },

    DCONST {
        @Override
        public boolean match(AbstractInsnNode i) {
            switch (i.getOpcode()) {
                case Opcodes.DCONST_0:
                case Opcodes.DCONST_1:  return true;
                default:                return false;
            }
        }
    },

    INT {
        @Override
        public boolean match(AbstractInsnNode i) {
            return     ICONST.match(i)
                    || BIPUSH.match(i)
                    || SIPUSH.match(i)
                    || (LDC.match(i) && ((LdcInsnNode)i).cst instanceof Integer);
        }
    },

    LONG {
        @Override
        public boolean match(AbstractInsnNode i) {
            return     LCONST.match(i)
                    || (LDC.match(i) && ((LdcInsnNode)i).cst instanceof Long);
        }
    },

    FLOAT {
        @Override
        public boolean match(AbstractInsnNode i) {
            return     FCONST.match(i)
                    || (LDC.match(i) && ((LdcInsnNode)i).cst instanceof Float);
        }
    },

    DOUBLE {
        @Override
        public boolean match(AbstractInsnNode i) {
            return     DCONST.match(i)
                    || (LDC.match(i) && ((LdcInsnNode)i).cst instanceof Double);
        }
    },

    IBINARYOP {
        @Override
        public boolean match(AbstractInsnNode i) {
            switch(i.getOpcode()) {
                case Opcodes.IADD:
                case Opcodes.ISUB:
                case Opcodes.IMUL:
                case Opcodes.IDIV:
                case Opcodes.IREM:
                case Opcodes.ISHL:
                case Opcodes.ISHR:
                case Opcodes.IUSHR:
                case Opcodes.IAND:
                case Opcodes.IOR:
                case Opcodes.IXOR:  return true;
                default:            return false;
            }
        }
    },

    IUNARYOP {
        @Override
        public boolean match(AbstractInsnNode i) {
            switch(i.getOpcode()) {
                case Opcodes.INEG:  return true;
                default:            return false;
            }
        }
    },

    LBINARYOP {
        @Override
        public boolean match(AbstractInsnNode i) {
            switch(i.getOpcode()) {
                case Opcodes.LADD:
                case Opcodes.LSUB:
                case Opcodes.LMUL:
                case Opcodes.LDIV:
                case Opcodes.LREM:
                case Opcodes.LSHL:
                case Opcodes.LSHR:
                case Opcodes.LUSHR:
                case Opcodes.LAND:
                case Opcodes.LOR:
                case Opcodes.LXOR:  return true;
                default:            return false;
            }
        }
    },

    LUNARYOP {
        @Override
        public boolean match(AbstractInsnNode i) {
            switch(i.getOpcode()) {
                case Opcodes.LNEG:  return true;
                default:            return false;
            }
        }
    },

    FBINARYOP {
        @Override
        public boolean match(AbstractInsnNode i) {
            switch(i.getOpcode()) {
                case Opcodes.FADD:
                case Opcodes.FSUB:
                case Opcodes.FMUL:
                case Opcodes.FDIV:
                case Opcodes.FREM:  return true;
                default:            return false;
            }
        }
    },

    FUNARYOP {
        @Override
        public boolean match(AbstractInsnNode i) {
            switch(i.getOpcode()) {
                case Opcodes.FNEG:  return true;
                default:            return false;
            }
        }
    },

    DBINARYOP {
        @Override
        public boolean match(AbstractInsnNode i) {
            switch(i.getOpcode()) {
                case Opcodes.DADD:
                case Opcodes.DSUB:
                case Opcodes.DMUL:
                case Opcodes.DDIV:
                case Opcodes.DREM:  return true;
                default:            return false;
            }
        }
    },

    DUNARYOP {
        @Override
        public boolean match(AbstractInsnNode i) {
            switch(i.getOpcode()) {
                case Opcodes.DNEG:  return true;
                default:            return false;
            }
        }
    },

    BEFORE_RETURN {
        @Override
        public boolean match(AbstractInsnNode i) {


            // all instructions with no side affects
            // that modify only the stack and local variables

            int opcode = i.getOpcode();
            switch (opcode) {

                // forbidden math operations:
                case Opcodes.IDIV:
                case Opcodes.LDIV:
                case Opcodes.IREM:
                case Opcodes.LREM:          return false;

                // allowed object operations:
                case Opcodes.INSTANCEOF:    return true;
            }

            // constants, loads, stores, stack, math, conversion
            return Opcodes.NOP <= opcode && opcode <= Opcodes.I2S;
        }
    },

    VALUE_TYPE1 {
        @Override
        public boolean match(AbstractInsnNode i) {

            // all values of the type 1
            return      Symbols.ACONST_NULL.match(i)
                    ||  Symbols.INT.match(i)
                    ||  Symbols.FLOAT.match(i)
                    ||  Symbols.ILOAD.match(i)
                    ||  Symbols.FLOAD.match(i);
        }
    },

    VALUE_TYPE2 {
        @Override
        public boolean match(AbstractInsnNode i) {

            // all values of the type 2
            return      Symbols.LONG.match(i)
                    ||  Symbols.DOUBLE.match(i)
                    ||  Symbols.LLOAD.match(i)
                    ||  Symbols.DLOAD.match(i);
        }
    };

    /**
     * The opcodes.
     */
    private int opcode;

    private Symbols(int opcode) {
        this.opcode = opcode;
    }

    private Symbols() {
        this.opcode = -1;
    }

    @Override
    public boolean match(AbstractInsnNode i) {
        return (opcode == i.getOpcode());
    }
}

