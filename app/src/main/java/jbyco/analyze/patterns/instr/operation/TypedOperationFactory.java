package jbyco.analyze.patterns.instr.operation;

import org.objectweb.asm.Opcodes;

import jbyco.analyze.patterns.instr.param.AbstractParameter;

public class TypedOperationFactory extends AbstractOperationFactory {

	enum TypedOperation implements AbstractOperation {
		
		NOP			(Opcodes.NOP),
		ACONST 		(Opcodes.ACONST_NULL),
		ICONST 		(
					Opcodes.ICONST_M1,
					Opcodes.ICONST_0,
					Opcodes.ICONST_1,
					Opcodes.ICONST_2,
					Opcodes.ICONST_3,
					Opcodes.ICONST_4,
					Opcodes.ICONST_5
					),
	    LCONST 		(
	    			Opcodes.LCONST_0,
	    			Opcodes.LCONST_1
	    			),
	    FCONST 		(
	    			Opcodes.FCONST_0,
	    			Opcodes.FCONST_1,
	    			Opcodes.FCONST_2
	    			),
	    DCONST 		(
	    			Opcodes.DCONST_0,
	    			Opcodes.DCONST_1
	    			),
	    BIPUSH 		(Opcodes.BIPUSH),
	    SIPUSH 		(Opcodes.SIPUSH),
	    LDC 		(Opcodes.LDC),
	    ILOAD 		(Opcodes.ILOAD),
	    LLOAD 		(Opcodes.LLOAD),
	    FLOAD 		(Opcodes.FLOAD),
	    DLOAD 		(Opcodes.DLOAD),
	    ALOAD 		(Opcodes.ALOAD),
	    IALOAD 		(Opcodes.IALOAD),
	    LALOAD 		(Opcodes.LALOAD),
	    FALOAD 		(Opcodes.FALOAD),
	    DALOAD 		(Opcodes.DALOAD),
	    AALOAD 		(Opcodes.AALOAD),
	    BALOAD 		(Opcodes.BALOAD),
	    CALOAD 		(Opcodes.CALOAD),
	    SALOAD 		(Opcodes.SALOAD),
	    ISTORE 		(Opcodes.ISTORE),
	    LSTORE 		(Opcodes.LSTORE),
	    FSTORE 		(Opcodes.FSTORE),
	    DSTORE 		(Opcodes.DSTORE),
	    ASTORE 		(Opcodes.ASTORE),
	    IASTORE 	(Opcodes.IASTORE),
	    LASTORE 	(Opcodes.LASTORE),
	    FASTORE 	(Opcodes.FASTORE),
	    DASTORE 	(Opcodes.DASTORE),
	    AASTORE 	(Opcodes.AASTORE),
	    BASTORE 	(Opcodes.BASTORE),
	    CASTORE 	(Opcodes.CASTORE),
	    SASTORE 	(Opcodes.SASTORE),
	    POP 		(Opcodes.POP),
	    POP2 		(Opcodes.POP2),
	    DUP 		(Opcodes.DUP),
	    DUP_X1 		(Opcodes.DUP_X1),
	    DUP_X2 		(Opcodes.DUP_X2),
	    DUP2 		(Opcodes.DUP2),
	    DUP2_X1 	(Opcodes.DUP2_X1),
	    DUP2_X2 	(Opcodes.DUP2_X2),
	    SWAP 		(Opcodes.SWAP),
	    IADD 		(Opcodes.IADD),
	    LADD 		(Opcodes.LADD),
	    FADD 		(Opcodes.FADD),
	    DADD 		(Opcodes.DADD),
	    ISUB 		(Opcodes.ISUB),
	    LSUB 		(Opcodes.LSUB),
	    FSUB 		(Opcodes.FSUB),
	    DSUB 		(Opcodes.DSUB),
	    IMUL 		(Opcodes.IMUL),
	    LMUL 		(Opcodes.LMUL),
	    FMUL 		(Opcodes.FMUL),
	    DMUL 		(Opcodes.DMUL),
	    IDIV 		(Opcodes.IDIV),
	    LDIV 		(Opcodes.LDIV),
	    FDIV 		(Opcodes.FDIV),
	    DDIV 		(Opcodes.DDIV),
	    IREM 		(Opcodes.IREM),
	    LREM 		(Opcodes.LREM),
	    FREM 		(Opcodes.FREM),
	    DREM 		(Opcodes.DREM),
	    INEG 		(Opcodes.INEG),
	    LNEG 		(Opcodes.LNEG),
	    FNEG 		(Opcodes.FNEG),
	    DNEG 		(Opcodes.DNEG),
	    ISHL 		(Opcodes.ISHL),
	    LSHL 		(Opcodes.LSHL),
	    ISHR 		(Opcodes.ISHR),
	    LSHR 		(Opcodes.LSHR),
	    IUSHR 		(Opcodes.IUSHR),
	    LUSHR 		(Opcodes.LUSHR),
	    IAND 		(Opcodes.IAND),
	    LAND 		(Opcodes.LAND),
	    IOR 		(Opcodes.IOR),
	    LOR 		(Opcodes.LOR),
	    IXOR 		(Opcodes.IXOR),
	    LXOR 		(Opcodes.LXOR),
	    IINC 		(Opcodes.IINC),
	    I2L 		(Opcodes.I2L),
	    I2F 		(Opcodes.I2F),
	    I2D 		(Opcodes.I2D),
	    L2I 		(Opcodes.L2I),
	    L2F 		(Opcodes.L2F),
	    L2D 		(Opcodes.L2D),
	    F2I 		(Opcodes.F2I),
	    F2L 		(Opcodes.F2L),
	    F2D 		(Opcodes.F2D),
	    D2I 		(Opcodes.D2I),
	    D2L 		(Opcodes.D2L),
	    D2F 		(Opcodes.D2F),
	    I2B 		(Opcodes.I2B),
	    I2C 		(Opcodes.I2C),
	    I2S 		(Opcodes.I2S),
	    LCMP 		(Opcodes.LCMP),
	    FCMPL 		(Opcodes.FCMPL),
	    FCMPG 		(Opcodes.FCMPG),
	    DCMPL 		(Opcodes.DCMPL),
	    DCMPG 		(Opcodes.DCMPG),
	    IFEQ 		(Opcodes.IFEQ),
	    IFNE 		(Opcodes.IFNE),
	    IFLT 		(Opcodes.IFLT),
	    IFGE 		(Opcodes.IFGE),
	    IFGT 		(Opcodes.IFGT),
	    IFLE 		(Opcodes.IFLE),
	    IF_ICMPEQ 	(Opcodes.IF_ICMPEQ),
	    IF_ICMPNE 	(Opcodes.IF_ICMPNE),
	    IF_ICMPLT 	(Opcodes.IF_ICMPLT),
	    IF_ICMPGE 	(Opcodes.IF_ICMPGE),
	    IF_ICMPGT 	(Opcodes.IF_ICMPGT),
	    IF_ICMPLE 	(Opcodes.IF_ICMPLE),
	    IF_ACMPEQ 	(Opcodes.IF_ACMPEQ),
	    IF_ACMPNE 	(Opcodes.IF_ACMPNE),
	    GOTO 		(Opcodes.GOTO),
	    JSR 		(Opcodes.JSR),
	    RET 		(Opcodes.RET),
	    TABLESWITCH (Opcodes.TABLESWITCH),
	    LOOKUPSWITCH(Opcodes.LOOKUPSWITCH),
	    IRETURN 	(Opcodes.IRETURN),
	    LRETURN 	(Opcodes.LRETURN),
	    FRETURN 	(Opcodes.FRETURN),
	    DRETURN 	(Opcodes.DRETURN),
	    ARETURN 	(Opcodes.ARETURN),
	    RETURN 		(Opcodes.RETURN),
	    GETSTATIC 	(Opcodes.GETSTATIC),
	    PUTSTATIC 	(Opcodes.PUTSTATIC),
	    GETFIELD 	(Opcodes.GETFIELD),
	    PUTFIELD 	(Opcodes.PUTFIELD),
	    INVOKEVIRTUAL 	(Opcodes.INVOKEVIRTUAL),
	    INVOKESPECIAL 	(Opcodes.INVOKESPECIAL),
	    INVOKESTATIC  	(Opcodes.INVOKESTATIC),
	    INVOKEINTERFACE (Opcodes.INVOKEINTERFACE),
	    INVOKEDYNAMIC 	(Opcodes.INVOKEDYNAMIC),
	    NEW 		(Opcodes.NEW),
	    NEWARRAY 	(Opcodes.NEWARRAY),
	    ANEWARRAY 	(Opcodes.ANEWARRAY),
	    ARRAYLENGTH (Opcodes.ARRAYLENGTH),
	    ATHROW 		(Opcodes.ATHROW), 
	    CHECKCAST 	(Opcodes.CHECKCAST), 
	    INSTANCEOF 	(Opcodes.INSTANCEOF), 
	    MONITORENTER(Opcodes.MONITORENTER), 
	    MONITOREXIT (Opcodes.MONITOREXIT), 
	    MULTIANEWARRAY (Opcodes.MULTIANEWARRAY),
	    IFNULL 		(Opcodes.IFNULL),
	    IFNONNULL 	(Opcodes.IFNONNULL);  
	
		private int[] opcodes;
		
		private TypedOperation(int ...opcodes) {
			this.opcodes = opcodes;
		}
	
		@Override
		public int[] getOpcodes() {
			return this.opcodes;
		}
	}
	
	public enum TypedHandleOperation implements AbstractHandleOperation {
		
		H_GETFIELD	(Opcodes.H_GETFIELD),
		H_GETSTATIC	(Opcodes.H_GETSTATIC),
		H_PUTFIELD	(Opcodes.H_PUTFIELD),
		H_PUTSTATIC	(Opcodes.H_PUTSTATIC),	
		H_INVOKEVIRTUAL (Opcodes.H_INVOKEVIRTUAL),
		H_INVOKESTATIC (Opcodes.H_INVOKESTATIC),
		H_INVOKESPECIAL (Opcodes.H_INVOKESPECIAL),
		H_NEWINVOKESPECIAL (Opcodes.H_NEWINVOKESPECIAL),
		H_INVOKEINTERFACE (Opcodes.H_INVOKEINTERFACE);
	
		private int[] tags;
		
		private TypedHandleOperation(int ...tags) {
			this.tags = tags;
		}
		
		public int[] getOpcodes() {
			return this.tags;
		}
	}
	
	public AbstractOperation[] allOperations() {
		return TypedOperation.values();
	}

	@Override
	public AbstractOperation[] allHandleOperations() {
		return TypedHandleOperation.values();
	}
}
