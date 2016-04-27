package jbyco.analyze.patterns.operations;

import org.objectweb.asm.Opcodes;

/**
 * The general representation of operation codes.
 * <p>
 * General operations are managed by {@link GeneralOperationFactory}.
 */
public enum GeneralOperation implements AbstractOperation {
	
	/** The nop operation. */
	NOP			(Opcodes.NOP),
	
	/** The operation for const, bipush, sipush, ldc. */
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
    
	/** The load operation. */
	LOAD		(
    			Opcodes.ILOAD,
	    	    Opcodes.LLOAD,
	    	    Opcodes.FLOAD,
	    	    Opcodes.DLOAD,
	    	    Opcodes.ALOAD
    			),
    
	/** The aload operation for loading values from an array. */
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
    
	/** The store operation. */
	STORE		(
	    		Opcodes.ISTORE,
	    	    Opcodes.LSTORE,
	    	    Opcodes.FSTORE,
	    	    Opcodes.DSTORE,
	    	    Opcodes.ASTORE
    			),
    
	/** The astore operation for storing values into an array. */
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
    
	/** The pop operation. */
	POP			(
    			Opcodes.POP,
    			Opcodes.POP2
    			),
    
	/** The dup operation. */
	DUP			(
    			Opcodes.DUP,
    			Opcodes.DUP2
    			),
    
	/** The dup_x1 operation. */
	DUP_X1 		(
    			Opcodes.DUP_X1,
    			Opcodes.DUP2_X1
    			),
    
	/** The dup_x2 operation. */
	DUP_X2 		(
    	    	Opcodes.DUP_X2,
    	    	Opcodes.DUP2_X2
    			),
    
	/** The swap operation. */
	SWAP		(Opcodes.SWAP),
    
	/** The add operation. */
	ADD 		(
    			Opcodes.IADD,
	    	    Opcodes.LADD,
	    	    Opcodes.FADD,
	    	    Opcodes.DADD
    			),
    
	/** The sub operation. */
	SUB 		(
	    		Opcodes.ISUB,
	    	    Opcodes.LSUB,
	    	    Opcodes.FSUB,
	    	    Opcodes.DSUB
    			),
    
	/** The mul operation. */
	MUL			(
	    		Opcodes.IMUL,
	    	    Opcodes.LMUL,
	    	    Opcodes.FMUL,
	    	    Opcodes.DMUL
    			),
    
	/** The div operation. */
	DIV			(
    			Opcodes.IDIV,
    			Opcodes.LDIV,
    			Opcodes.FDIV,
    			Opcodes.DDIV
    			),
    
	/** The rem operation. */
	REM			(
	    		Opcodes.IREM,
	    	    Opcodes.LREM,
	    	    Opcodes.FREM,
	    	    Opcodes.DREM
    			),
    
	/** The neg operation. */
	NEG 		(
	    		Opcodes.INEG,
	    	    Opcodes.LNEG,
	    	    Opcodes.FNEG,
	    	    Opcodes.DNEG
	    	    ),
    
	/** The shl operation. */
	SHL 		(
    			Opcodes.ISHL,
    			Opcodes.LSHL
    			),
    
	/** The shr operation. */
	SHR 		(
    			Opcodes.ISHR,
    			Opcodes.LSHR
    			),
    
	/** The ushr operation. */
	USHR 		(
    			Opcodes.IUSHR,
    			Opcodes.LUSHR
    			),
    
	/** The and operation. */
	AND 		(
    			Opcodes.IAND,
    			Opcodes.LAND
    			),
    
	/** The or operation. */
	OR 			(
    			Opcodes.IOR,
    			Opcodes.LOR
    			),
    
	/** The xor operation. */
	XOR 		(
    			Opcodes.IXOR,
    			Opcodes.LXOR),
    
	/** The inc operation. */
	INC 		(Opcodes.IINC),
    
	/** The conversion operation. */
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
    
	/** The cmp operation. */
	CMP 		(
    			Opcodes.LCMP,
	    	    Opcodes.FCMPL,
	    	    Opcodes.FCMPG,
	    	    Opcodes.DCMPL,
	    	    Opcodes.DCMPG
	    	    ),
    
	/** The ifeq operation. */
	IFEQ 		(Opcodes.IFEQ),
    
	/** The ifne operation. */
	IFNE 		(Opcodes.IFNE),
    
	/** The iflt operation. */
	IFLT 		(Opcodes.IFLT),
    
	/** The ifge operation. */
	IFGE 		(Opcodes.IFGE),
    
	/** The ifgt operation. */
	IFGT 		(Opcodes.IFGT),
    
	/** The ifle operation. */
	IFLE 		(Opcodes.IFLE),
    
	/** The if_cmpeq operation. */
	IF_CMPEQ 	(
    			Opcodes.IF_ICMPEQ, 
    			Opcodes.IF_ACMPEQ
    			),
    
	/** The if_cmpne operation. */
	IF_CMPNE 	(
    			Opcodes.IF_ICMPNE, 
    			Opcodes.IF_ACMPNE
    			),
    
	/** The if_cmplt operation. */
	IF_CMPLT 	(Opcodes.IF_ICMPLT),
    
	/** The if_cmpge operation. */
	IF_CMPGE 	(Opcodes.IF_ICMPGE),
    
	/** The if_cmpgt operation. */
	IF_CMPGT 	(Opcodes.IF_ICMPGT),
    
	/** The if_cmple operation. */
	IF_CMPLE 	(Opcodes.IF_ICMPLE),
    
	/** The goto operation. */
	GOTO 		(Opcodes.GOTO),
    
	/** The jsr operation. */
	JSR 		(Opcodes.JSR),
    
	/** The ret operation. */
	RET 		(Opcodes.RET),
    
	/** The tableswitch operation. */
	TABLESWITCH (Opcodes.TABLESWITCH),
    
	/** The lookupswitch operation. */
	LOOKUPSWITCH (Opcodes.LOOKUPSWITCH),
    
	/** The return operation. */
	RETURN 		(
    			Opcodes.IRETURN,
		    	Opcodes.LRETURN,
		    	Opcodes.FRETURN,
		    	Opcodes.DRETURN,
		    	Opcodes.ARETURN,
		    	Opcodes.RETURN
		    	),
    
	/** The getstatic operation. */
	GETSTATIC 	(Opcodes.GETSTATIC),
    
	/** The putstatic operation. */
	PUTSTATIC 	(Opcodes.PUTSTATIC),
    
	/** The getfield operation. */
	GETFIELD 	(Opcodes.GETFIELD),
    
	/** The putfield operation. */
	PUTFIELD 	(Opcodes.PUTFIELD),
    
	/** The invoke method operation. */
	INVOKE 		(
    			Opcodes.INVOKEVIRTUAL,
	    		Opcodes.INVOKESPECIAL,
	    		Opcodes.INVOKESTATIC,
	    		Opcodes.INVOKEINTERFACE,
	    		Opcodes.INVOKEDYNAMIC
	    		),
    
	/** The new object operation. */
	NEW 		(Opcodes.NEW),
    
	/** The new array operation. */
	NEWARRAY 	(
    			Opcodes.NEWARRAY,
    			Opcodes.ANEWARRAY,
    			Opcodes.MULTIANEWARRAY
    			),
    
	/** The array length operation. */
	ARRAYLENGTH (Opcodes.ARRAYLENGTH),
    
	/** The athrow operation. */
	ATHROW 		(Opcodes.ATHROW), 
    
	/** The checkcast operation. */
	CHECKCAST 	(Opcodes.CHECKCAST), 
    
	/** The instanceof operation. */
	INSTANCEOF 	(Opcodes.INSTANCEOF), 
    
	/** The monitorenter operation. */
	MONITORENTER (Opcodes.MONITORENTER), 
    
	/** The monitorexit operation. */
	MONITOREXIT (Opcodes.MONITOREXIT),
    
	/** The ifnull operation. */
	IFNULL 		(Opcodes.IFNULL),
    
	/** The ifnonnull operation. */
	IFNONNULL 	(Opcodes.IFNONNULL);
	
	/** The operation codes. */
	private int[] opcodes;
	
	/**
	 * Instantiates a new general operation.
	 *
	 * @param opcodes the opcodes
	 */
	private GeneralOperation(int ...opcodes) {
		this.opcodes = opcodes;
	}
	
	public int[] getOpcodes() {
		return this.opcodes;
	}
	
}