package jbyco.analyze.patterns.operations;

import org.objectweb.asm.Opcodes;

/**
 * The representation of typed operation codes.
 * <p>
 * Typed operations are managed by {@link TypedOperationFactory}.
 */
enum TypedOperation implements AbstractOperation {
	
	/** The nop operation. */
	NOP			(Opcodes.NOP),
	
	/** The aconst operation. */
	ACONST 		(Opcodes.ACONST_NULL),
	
	/** The iconst operation. */
	ICONST 		(
				Opcodes.ICONST_M1,
				Opcodes.ICONST_0,
				Opcodes.ICONST_1,
				Opcodes.ICONST_2,
				Opcodes.ICONST_3,
				Opcodes.ICONST_4,
				Opcodes.ICONST_5
				),
    
	/** The lconst operation. */
	LCONST 		(
    			Opcodes.LCONST_0,
    			Opcodes.LCONST_1
    			),
    
	/** The fconst operation. */
	FCONST 		(
    			Opcodes.FCONST_0,
    			Opcodes.FCONST_1,
    			Opcodes.FCONST_2
    			),
    
	/** The dconst operation. */
	DCONST 		(
    			Opcodes.DCONST_0,
    			Opcodes.DCONST_1
    			),
    
	/** The bipush operation. */
	BIPUSH 		(Opcodes.BIPUSH),
    
	/** The sipush operation. */
	SIPUSH 		(Opcodes.SIPUSH),
    
	/** The ldc operation. */
	LDC 		(Opcodes.LDC),
    
	/** The iload operation. */
	ILOAD 		(Opcodes.ILOAD),
    
	/** The lload operation. */
	LLOAD 		(Opcodes.LLOAD),
    
	/** The fload operation. */
	FLOAD 		(Opcodes.FLOAD),
    
	/** The dload operation. */
	DLOAD 		(Opcodes.DLOAD),
    
	/** The aload operation. */
	ALOAD 		(Opcodes.ALOAD),
    
	/** The iaload operation. */
	IALOAD 		(Opcodes.IALOAD),
    
	/** The laload operation. */
	LALOAD 		(Opcodes.LALOAD),
    
	/** The faload operation. */
	FALOAD 		(Opcodes.FALOAD),
    
	/** The daload operation. */
	DALOAD 		(Opcodes.DALOAD),
    
	/** The aaload operation. */
	AALOAD 		(Opcodes.AALOAD),
    
	/** The baload operation. */
	BALOAD 		(Opcodes.BALOAD),
    
	/** The caload operation. */
	CALOAD 		(Opcodes.CALOAD),
    
	/** The saload operation. */
	SALOAD 		(Opcodes.SALOAD),
    
	/** The istore operation. */
	ISTORE 		(Opcodes.ISTORE),
    
	/** The lstore operation. */
	LSTORE 		(Opcodes.LSTORE),
    
	/** The fstore operation. */
	FSTORE 		(Opcodes.FSTORE),
    
	/** The dstore operation. */
	DSTORE 		(Opcodes.DSTORE),
    
	/** The astore operation. */
	ASTORE 		(Opcodes.ASTORE),
    
	/** The iastore operation. */
	IASTORE 	(Opcodes.IASTORE),
    
	/** The lastore operation. */
	LASTORE 	(Opcodes.LASTORE),
    
	/** The fastore operation. */
	FASTORE 	(Opcodes.FASTORE),
    
	/** The dastore operation. */
	DASTORE 	(Opcodes.DASTORE),
    
	/** The aastore operation. */
	AASTORE 	(Opcodes.AASTORE),
    
	/** The bastore operation. */
	BASTORE 	(Opcodes.BASTORE),
    
	/** The castore operation. */
	CASTORE 	(Opcodes.CASTORE),
    
	/** The sastore operation. */
	SASTORE 	(Opcodes.SASTORE),
    
	/** The pop operation. */
	POP 		(Opcodes.POP),
    
	/** The pop2 operation. */
	POP2 		(Opcodes.POP2),
    
	/** The dup operation. */
	DUP 		(Opcodes.DUP),
    
	/** The dup_x1 operation. */
	DUP_X1 		(Opcodes.DUP_X1),
    
	/** The dup_x2 operation. */
	DUP_X2 		(Opcodes.DUP_X2),
    
	/** The dup2 operation. */
	DUP2 		(Opcodes.DUP2),
    
	/** The dup2_x1 operation. */
	DUP2_X1 	(Opcodes.DUP2_X1),
    
	/** The dup2_x2 operation. */
	DUP2_X2 	(Opcodes.DUP2_X2),
    
	/** The swap operation. */
	SWAP 		(Opcodes.SWAP),
    
	/** The iadd operation. */
	IADD 		(Opcodes.IADD),
    
	/** The ladd operation. */
	LADD 		(Opcodes.LADD),
    
	/** The fadd operation. */
	FADD 		(Opcodes.FADD),
    
	/** The dadd operation. */
	DADD 		(Opcodes.DADD),
    
	/** The isub operation. */
	ISUB 		(Opcodes.ISUB),
    
	/** The lsub operation. */
	LSUB 		(Opcodes.LSUB),
    
	/** The fsub operation. */
	FSUB 		(Opcodes.FSUB),
    
	/** The dsub operation. */
	DSUB 		(Opcodes.DSUB),
    
	/** The imul operation. */
	IMUL 		(Opcodes.IMUL),
    
	/** The lmul operation. */
	LMUL 		(Opcodes.LMUL),
    
	/** The fmul operation. */
	FMUL 		(Opcodes.FMUL),
    
	/** The dmul operation. */
	DMUL 		(Opcodes.DMUL),
    
	/** The idiv operation. */
	IDIV 		(Opcodes.IDIV),
    
	/** The ldiv operation. */
	LDIV 		(Opcodes.LDIV),
    
	/** The fdiv operation. */
	FDIV 		(Opcodes.FDIV),
    
	/** The ddiv operation. */
	DDIV 		(Opcodes.DDIV),
    
	/** The irem operation. */
	IREM 		(Opcodes.IREM),
    
	/** The lrem operation. */
	LREM 		(Opcodes.LREM),
    
	/** The frem operation. */
	FREM 		(Opcodes.FREM),
    
	/** The drem operation. */
	DREM 		(Opcodes.DREM),
    
	/** The ineg operation. */
	INEG 		(Opcodes.INEG),
    
	/** The lneg operation. */
	LNEG 		(Opcodes.LNEG),
    
	/** The fneg operation. */
	FNEG 		(Opcodes.FNEG),
    
	/** The dneg operation. */
	DNEG 		(Opcodes.DNEG),
    
	/** The ishl operation. */
	ISHL 		(Opcodes.ISHL),
    
	/** The lshl operation. */
	LSHL 		(Opcodes.LSHL),
    
	/** The ishr operation. */
	ISHR 		(Opcodes.ISHR),
    
	/** The lshr operation. */
	LSHR 		(Opcodes.LSHR),
    
	/** The iushr operation. */
	IUSHR 		(Opcodes.IUSHR),
    
	/** The lushr operation. */
	LUSHR 		(Opcodes.LUSHR),
    
	/** The iand operation. */
	IAND 		(Opcodes.IAND),
    
	/** The land operation. */
	LAND 		(Opcodes.LAND),
    
	/** The ior operation. */
	IOR 		(Opcodes.IOR),
    
	/** The lor operation. */
	LOR 		(Opcodes.LOR),
    
	/** The ixor operation. */
	IXOR 		(Opcodes.IXOR),
    
	/** The lxor operation. */
	LXOR 		(Opcodes.LXOR),
    
	/** The iinc operation. */
	IINC 		(Opcodes.IINC),
    
	/** The I2 l operation. */
	I2L 		(Opcodes.I2L),
    
	/** The I2 f operation. */
	I2F 		(Opcodes.I2F),
    
	/** The I2 d operation. */
	I2D 		(Opcodes.I2D),
    
	/** The L2 i operation. */
	L2I 		(Opcodes.L2I),
    
	/** The L2 f operation. */
	L2F 		(Opcodes.L2F),
    
	/** The L2 d operation. */
	L2D 		(Opcodes.L2D),
    
	/** The F2 i operation. */
	F2I 		(Opcodes.F2I),
    
	/** The F2 l operation. */
	F2L 		(Opcodes.F2L),
    
	/** The F2 d operation. */
	F2D 		(Opcodes.F2D),
    
	/** The D2 i operation. */
	D2I 		(Opcodes.D2I),
    
	/** The D2 l operation. */
	D2L 		(Opcodes.D2L),
    
	/** The D2 f operation. */
	D2F 		(Opcodes.D2F),
    
	/** The I2 b operation. */
	I2B 		(Opcodes.I2B),
    
	/** The I2 c operation. */
	I2C 		(Opcodes.I2C),
    
	/** The I2 s operation. */
	I2S 		(Opcodes.I2S),
    
	/** The lcmp operation. */
	LCMP 		(Opcodes.LCMP),
    
	/** The fcmpl operation. */
	FCMPL 		(Opcodes.FCMPL),
    
	/** The fcmpg operation. */
	FCMPG 		(Opcodes.FCMPG),
    
	/** The dcmpl operation. */
	DCMPL 		(Opcodes.DCMPL),
    
	/** The dcmpg operation. */
	DCMPG 		(Opcodes.DCMPG),
    
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
    
	/** The if icmpeq operation. */
	IF_ICMPEQ 	(Opcodes.IF_ICMPEQ),
    
	/** The if icmpne operation. */
	IF_ICMPNE 	(Opcodes.IF_ICMPNE),
    
	/** The if icmplt operation. */
	IF_ICMPLT 	(Opcodes.IF_ICMPLT),
    
	/** The if icmpge operation. */
	IF_ICMPGE 	(Opcodes.IF_ICMPGE),
    
	/** The if icmpgt operation. */
	IF_ICMPGT 	(Opcodes.IF_ICMPGT),
    
	/** The if icmple operation. */
	IF_ICMPLE 	(Opcodes.IF_ICMPLE),
    
	/** The if acmpeq operation. */
	IF_ACMPEQ 	(Opcodes.IF_ACMPEQ),
    
	/** The if acmpne operation. */
	IF_ACMPNE 	(Opcodes.IF_ACMPNE),
    
	/** The goto operation. */
	GOTO 		(Opcodes.GOTO),
    
	/** The jsr operation. */
	JSR 		(Opcodes.JSR),
    
	/** The ret operation. */
	RET 		(Opcodes.RET),
    
	/** The tableswitch operation. */
	TABLESWITCH (Opcodes.TABLESWITCH),
    
	/** The lookupswitch operation. */
	LOOKUPSWITCH(Opcodes.LOOKUPSWITCH),
    
	/** The ireturn operation. */
	IRETURN 	(Opcodes.IRETURN),
    
	/** The lreturn operation. */
	LRETURN 	(Opcodes.LRETURN),
    
	/** The freturn operation. */
	FRETURN 	(Opcodes.FRETURN),
    
	/** The dreturn operation. */
	DRETURN 	(Opcodes.DRETURN),
    
	/** The areturn operation. */
	ARETURN 	(Opcodes.ARETURN),
    
	/** The return operation. */
	RETURN 		(Opcodes.RETURN),
    
	/** The getstatic operation. */
	GETSTATIC 	(Opcodes.GETSTATIC),
    
	/** The putstatic operation. */
	PUTSTATIC 	(Opcodes.PUTSTATIC),
    
	/** The getfield operation. */
	GETFIELD 	(Opcodes.GETFIELD),
    
	/** The putfield operation. */
	PUTFIELD 	(Opcodes.PUTFIELD),
    
	/** The invokevirtual operation. */
	INVOKEVIRTUAL 	(Opcodes.INVOKEVIRTUAL),
    
	/** The invokespecial operation. */
	INVOKESPECIAL 	(Opcodes.INVOKESPECIAL),
    
	/** The invokestatic operation. */
	INVOKESTATIC  	(Opcodes.INVOKESTATIC),
    
	/** The invokeinterface operation. */
	INVOKEINTERFACE (Opcodes.INVOKEINTERFACE),
    
	/** The invokedynamic operation. */
	INVOKEDYNAMIC 	(Opcodes.INVOKEDYNAMIC),
    
	/** The new operation. */
	NEW 		(Opcodes.NEW),
    
	/** The newarray operation. */
	NEWARRAY 	(Opcodes.NEWARRAY),
    
	/** The anewarray operation. */
	ANEWARRAY 	(Opcodes.ANEWARRAY),
    
	/** The arraylength operation. */
	ARRAYLENGTH (Opcodes.ARRAYLENGTH),
    
	/** The athrow operation. */
	ATHROW 		(Opcodes.ATHROW), 
    
	/** The checkcast operation. */
	CHECKCAST 	(Opcodes.CHECKCAST), 
    
	/** The instanceof operation. */
	INSTANCEOF 	(Opcodes.INSTANCEOF), 
    
	/** The monitorenter operation. */
	MONITORENTER(Opcodes.MONITORENTER), 
    
	/** The monitorexit operation. */
	MONITOREXIT (Opcodes.MONITOREXIT), 
    
	/** The multianewarray operation. */
	MULTIANEWARRAY (Opcodes.MULTIANEWARRAY),
    
	/** The ifnull operation. */
	IFNULL 		(Opcodes.IFNULL),
    
	/** The ifnonnull operation. */
	IFNONNULL 	(Opcodes.IFNONNULL);  

	/** The opcodes. */
	private int[] opcodes;
	
	/**
	 * Instantiates a new typed operation.
	 *
	 * @param opcodes the opcodes
	 */
	private TypedOperation(int ...opcodes) {
		this.opcodes = opcodes;
	}

	@Override
	public int[] getOpcodes() {
		return this.opcodes;
	}
}