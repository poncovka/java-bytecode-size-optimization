package jbyco.analyze.patterns.instr.operation;

import org.objectweb.asm.Opcodes;

public class GeneralOperationFactory extends AbstractOperationFactory {

	public enum GeneralOperation implements AbstractOperation {
		
		NOP			(Opcodes.NOP),
		CONST		(
					Opcodes.ACONST_NULL,
			    	Opcodes.ICONST_M1,
			    	Opcodes.ICONST_0,
			    	Opcodes.ICONST_1,
			    	Opcodes.ICONST_2,
			    	Opcodes.ICONST_3,
				    Opcodes.ICONST_4,
				    Opcodes.ICONST_5,
				    Opcodes.LCONST_0,
				    Opcodes.LCONST_1,
				    Opcodes.FCONST_0,
				    Opcodes.FCONST_1,
				    Opcodes.FCONST_2,
				    Opcodes.DCONST_0,
				    Opcodes.DCONST_1,
				    Opcodes.BIPUSH,
				    Opcodes.SIPUSH,
				    Opcodes.LDC
				    ),
	    LOAD		(
	    			Opcodes.ILOAD,
		    	    Opcodes.LLOAD,
		    	    Opcodes.FLOAD,
		    	    Opcodes.DLOAD,
		    	    Opcodes.ALOAD
	    			),
	    ALOAD		(
	    			Opcodes.IALOAD,
	    			Opcodes.LALOAD,
	    			Opcodes.FALOAD,
	    			Opcodes.DALOAD,
	    			Opcodes.AALOAD,
	    			Opcodes.BALOAD,
	    			Opcodes.CALOAD,
	    			Opcodes.SALOAD
	    			),
	    STORE		(
		    		Opcodes.ISTORE,
		    	    Opcodes.LSTORE,
		    	    Opcodes.FSTORE,
		    	    Opcodes.DSTORE,
		    	    Opcodes.ASTORE
	    			),
	    ASTORE		(
		    		Opcodes.IASTORE,
		    	    Opcodes.LASTORE,
		    	    Opcodes.FASTORE,
		    	    Opcodes.DASTORE,
		    	    Opcodes.AASTORE,
		    	    Opcodes.BASTORE,
		    	    Opcodes.CASTORE,
		    	    Opcodes.SASTORE
	    			),
	    POP			(
	    			Opcodes.POP,
	    			Opcodes.POP2
	    			),
	    DUP			(
	    			Opcodes.DUP,
	    			Opcodes.DUP2
	    			),
	    DUP_X1 		(
	    			Opcodes.DUP_X1,
	    			Opcodes.DUP2_X1
	    			),
	    DUP_X2 		(
	    	    	Opcodes.DUP_X2,
	    	    	Opcodes.DUP2_X2
	    			),
	    SWAP		(Opcodes.SWAP),
	    ADD 		(
	    			Opcodes.IADD,
		    	    Opcodes.LADD,
		    	    Opcodes.FADD,
		    	    Opcodes.DADD
	    			),
	    SUB 		(
		    		Opcodes.ISUB,
		    	    Opcodes.LSUB,
		    	    Opcodes.FSUB,
		    	    Opcodes.DSUB
	    			),
	    MUL			(
		    		Opcodes.IMUL,
		    	    Opcodes.LMUL,
		    	    Opcodes.FMUL,
		    	    Opcodes.DMUL
	    			),
	    DIV			(
	    			Opcodes.IDIV,
	    			Opcodes.LDIV,
	    			Opcodes.FDIV,
	    			Opcodes.DDIV
	    			),
	    REM			(
		    		Opcodes.IREM,
		    	    Opcodes.LREM,
		    	    Opcodes.FREM,
		    	    Opcodes.DREM
	    			),
	    NEG 		(
		    		Opcodes.INEG,
		    	    Opcodes.LNEG,
		    	    Opcodes.FNEG,
		    	    Opcodes.DNEG
		    	    ),
	    SHL 		(
	    			Opcodes.ISHL,
	    			Opcodes.LSHL
	    			),
	    SHR 		(
	    			Opcodes.ISHR,
	    			Opcodes.LSHR
	    			),
	    USHR 		(
	    			Opcodes.IUSHR,
	    			Opcodes.LUSHR
	    			),
	    AND 		(
	    			Opcodes.IAND,
	    			Opcodes.LAND
	    			),
	    OR 			(
	    			Opcodes.IOR,
	    			Opcodes.LOR
	    			),
	    XOR 		(
	    			Opcodes.IXOR,
	    			Opcodes.LXOR),
	    INC 		(Opcodes.IINC),
	    CONVERT 	(
	    			Opcodes.I2L,
		    	    Opcodes.I2F,
		    	    Opcodes.I2D,
		    	    Opcodes.L2I,
		    	    Opcodes.L2F,
		    	    Opcodes.L2D,
		    	    Opcodes.F2I,
		    	    Opcodes.F2L,
		    	    Opcodes.F2D,
		    	    Opcodes.D2I,
		    	    Opcodes.D2L,
		    	    Opcodes.D2F,
		    	    Opcodes.I2B,
		    	    Opcodes.I2C,
		    	    Opcodes.I2S
		    	    ),
	    CMP 		(
	    			Opcodes.LCMP,
		    	    Opcodes.FCMPL,
		    	    Opcodes.FCMPG,
		    	    Opcodes.DCMPL,
		    	    Opcodes.DCMPG
		    	    ),
	    IFEQ 		(Opcodes.IFEQ),
	    IFNE 		(Opcodes.IFNE),
	    IFLT 		(Opcodes.IFLT),
	    IFGE 		(Opcodes.IFGE),
	    IFGT 		(Opcodes.IFGT),
	    IFLE 		(Opcodes.IFLE),
	    IF_CMPEQ 	(
	    			Opcodes.IF_ICMPEQ, 
	    			Opcodes.IF_ACMPEQ
	    			),
	    IF_CMPNE 	(
	    			Opcodes.IF_ICMPNE, 
	    			Opcodes.IF_ACMPNE
	    			),
	    IF_CMPLT 	(Opcodes.IF_ICMPLT),
	    IF_CMPGE 	(Opcodes.IF_ICMPGE),
	    IF_CMPGT 	(Opcodes.IF_ICMPGT),
	    IF_CMPLE 	(Opcodes.IF_ICMPLE),
	    GOTO 		(Opcodes.GOTO),
	    JSR 		(Opcodes.JSR),
	    RET 		(Opcodes.RET),
	    TABLESWITCH (Opcodes.TABLESWITCH),
	    LOOKUPSWITCH (Opcodes.LOOKUPSWITCH),
	    RETURN 		(
	    			Opcodes.IRETURN,
			    	Opcodes.LRETURN,
			    	Opcodes.FRETURN,
			    	Opcodes.DRETURN,
			    	Opcodes.ARETURN,
			    	Opcodes.RETURN
			    	),
	    GETSTATIC 	(Opcodes.GETSTATIC),
	    PUTSTATIC 	(Opcodes.PUTSTATIC),
	    GETFIELD 	(Opcodes.GETFIELD),
	    PUTFIELD 	(Opcodes.PUTFIELD),
	    INVOKE 		(
	    			Opcodes.INVOKEVIRTUAL,
		    		Opcodes.INVOKESPECIAL,
		    		Opcodes.INVOKESTATIC,
		    		Opcodes.INVOKEINTERFACE,
		    		Opcodes.INVOKEDYNAMIC
		    		),
	    NEW 		(Opcodes.NEW),
	    NEWARRAY 	(
	    			Opcodes.NEWARRAY,
	    			Opcodes.ANEWARRAY,
	    			Opcodes.MULTIANEWARRAY
	    			),
	    ARRAYLENGTH (Opcodes.ARRAYLENGTH),
	    ATHROW 		(Opcodes.ATHROW), 
	    CHECKCAST 	(Opcodes.CHECKCAST), 
	    INSTANCEOF 	(Opcodes.INSTANCEOF), 
	    MONITORENTER (Opcodes.MONITORENTER), 
	    MONITOREXIT (Opcodes.MONITOREXIT),
	    IFNULL 		(Opcodes.IFNULL),
	    IFNONNULL 	(Opcodes.IFNONNULL);
		
		private int[] opcodes;
		
		private GeneralOperation(int ...opcodes) {
			this.opcodes = opcodes;
		}
		
		public int[] getOpcodes() {
			return this.opcodes;
		}
		
	}

	@Override
	public AbstractOperation[] all() {
		return GeneralOperation.values();
	}
		
}
