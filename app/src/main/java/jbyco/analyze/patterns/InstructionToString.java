package jbyco.analyze.patterns;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPushInstruction;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.INSTANCEOF;
import org.apache.bcel.generic.INVOKEDYNAMIC;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;
import org.apache.bcel.generic.LOOKUPSWITCH;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.MULTIANEWARRAY;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.NEWARRAY;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.TABLESWITCH;

import jbyco.lib.BytecodeVisitor;

public class InstructionToString extends BytecodeVisitor {
	
	ConstantPool cp;
	
	int opAbstraction;
	int varAbstraction;
	int valAbstraction;
	
	StringBuilder builder;
	Instruction instr;
	
	public InstructionToString(int opAbstraction, int varAbstraction, int valAbstraction) {
		this.opAbstraction = opAbstraction;
		this.varAbstraction = varAbstraction;
		this.valAbstraction = valAbstraction;
	}
	
	public void setConstantPool(ConstantPool cp) {
		this.cp = cp;
	}
	
	public String get(Instruction i) {
		
		// set builder
		builder = new StringBuilder();
		
		// get opcode
		instr = i;
		
		// get name
		builder.append(getName(i.getOpcode(), opAbstraction));
		
		// get operands
		i.accept(this);		
		
		// return string
		return builder.toString();
	}
	
	public static String getName(short opcode, int abstraction) {
		return OPCODES_ABSTRACTION[Integer.min(abstraction, 3)][opcode];
	}
	
	/*
	
	ACONST_NULL; // null
	LoadInstruction; // variabel
	StoreInstrucion; // variable
	IINC // variable, value
	ConstantPushInstruction // value
	Select // switch, ofsetty
	BranchInstruction //offet (součástí i Select)
	FieldInstruction // field
	InvokeInstruction // method
	InvokeDynamic // method
	MultiAnewArray
	AnewArray
	CPInstruction // index do constant pool (Anewarray, multianewarray)
	NEWARRAY // type
	RET // variable
	*/
	
	private void addVariable(int index) {
		
		switch(varAbstraction) {
			case 0: 	builder.append(" ").append(index); break;
			case 1: 	builder.append(" V").append(-1); break; // TODO 
			default: 	builder.append(" V");
		}
		
	}
	
	private void addValue(Object value) {
		
		String type = OPCODES_TYPES[instr.getOpcode()];
		
		switch(valAbstraction) {
			case 0: 
			case 1: 	builder.append(" ").append(value); break; 
			case 2: 	builder.append(" ").append(type).append(-1); break; 
			default: 	builder.append(" ").append(type);
		}
	}
	
	private void addConstantPoolIndex(int index) {
		
		String type = OPCODES_TYPES[instr.getOpcode()];
		
		switch(valAbstraction) {
			case 0: 	builder.append(" ").append(index); break;
			case 1: 	builder.append(" "); cp.getConstant(index).accept(this); break;
			case 2: 	builder.append(" ").append(type).append(-1); break; // TODO
			default: 	builder.append(" ").append(type);
		}
	}
	
	private void addLabel(int offset) {
		
		String type = OPCODES_TYPES[instr.getOpcode()];
		
		switch(valAbstraction) {
			case 0: 	
			case 1: 	builder.append(" ").append(offset); break;
			case 2: 	builder.append(" ").append(type).append(-1); break; // TODO
			default: 	builder.append(" ").append(type);
		}
	}
	
	@Override
	public void visitACONST_NULL(ACONST_NULL i) {
		addValue("null");
	}
	
	@Override
	public void visitConstantPushInstruction(ConstantPushInstruction i) {
		addValue(i.getValue());
	}
	
	@Override
	public void visitLocalVariableInstruction(LocalVariableInstruction i) {
		addVariable(i.getIndex());
	}
	
	@Override
	public void visitIINC(IINC i) {
		addValue(i.getIncrement());
	}
	
	@Override
	public void visitRET(RET i) {
		addVariable(i.getIndex());
	}
	
	@Override
	public void visitCHECKCAST(CHECKCAST i) {
		int index = i.getIndex();
		addConstantPoolIndex(index);
	}
	
	@Override
	public void visitINSTANCEOF(INSTANCEOF i) {
		int index = i.getIndex();
		addConstantPoolIndex(index);
	}
	
	@Override
	public void visitLDC(LDC i) {
		int index = i.getIndex();
		addConstantPoolIndex(index);
	}
	
	@Override
	public void visitLDC2_W(LDC2_W i) {
		int index = i.getIndex();
		addConstantPoolIndex(index);
	}
	
	@Override
	public void visitNEW(NEW i) {
		int index = i.getIndex();
		addConstantPoolIndex(index);
	}
	
	@Override
	public void visitINVOKEDYNAMIC(INVOKEDYNAMIC i) {
		int index = i.getIndex();
		addConstantPoolIndex(index);
	}
	
	@Override
	public void visitFieldInstruction(FieldInstruction i) {
		int index = i.getIndex();
		addConstantPoolIndex(index);
	}
	
	@Override
	public void visitInvokeInstruction(InvokeInstruction i) {
		int index = i.getIndex();
		addConstantPoolIndex(index);
	}
	
	@Override
	public void visitNEWARRAY(NEWARRAY i) {
		int dims = 1;
		String value = Constants.TYPE_NAMES[i.getTypecode()];
		
		switch(valAbstraction) {
			case 0: 	builder.append(" ").append(i.getTypecode()); break;
			case 1: 	builder.append(" ").append(dims).append(" ").append(value); break;
			case 2: 	builder.append(" ").append(dims).append(" ").append(value); break;
			default: 	
		}
	}
	
	@Override
	public void visitANEWARRAY(ANEWARRAY i) {
		int dims = 1;
		String type = OPCODES_TYPES[instr.getOpcode()];
		
		switch(valAbstraction) {
			case 0: 	builder.append(" ").append(i.getIndex()); break;
			case 1: 	builder.append(" ").append(dims).append(" "); cp.getConstant(i.getIndex()); break;
			case 2: 	builder.append(" ").append(dims).append(" ").append(type).append(-1); break;
			default: 	
		}
	}
	
	@Override
	public void visitMULTIANEWARRAY(MULTIANEWARRAY i) {
		int dims = i.getDimensions();
		String type = OPCODES_TYPES[instr.getOpcode()];
		
		switch(valAbstraction) {
			case 0: 	builder.append(" ").append(dims).append(" ").append(i.getIndex()); break;
			case 1: 	builder.append(" ").append(dims).append(" "); cp.getConstant(i.getIndex()); break;
			case 2: 	builder.append(" ").append(dims).append(" ").append(type).append(-1); break;
			default: 	
		}
	}
	
	@Override
	public void visitBranchInstruction(BranchInstruction i) {
		addLabel(i.getIndex());
	}
	
	@Override
	public void visitLOOKUPSWITCH(LOOKUPSWITCH i) {
		switch(valAbstraction) {
			case 0: 	
			case 1: 	
			case 2: 	builder.append(" ").append(i.toString(true)); break;
			default: 	
		}
	}
	
	@Override
	public void visitTABLESWITCH(TABLESWITCH i) {
		
		switch(valAbstraction) {
			case 0: 	
			case 1: 	
			case 2: 	builder.append(" ").append(i.toString(true)); break;
			default: 	
		}
	}
	
	
	@Override
	public void visitConstantClass(ConstantClass c) {
		cp.getConstant(c.getNameIndex()).accept(this);
	}
	
	@Override
	public void visitConstantDouble(ConstantDouble c) {
		builder.append(c.getBytes());
	}
	
	@Override
	public void visitConstantFieldref(ConstantFieldref c) {
		cp.getConstant(c.getNameAndTypeIndex()).accept(this);
	}
	
	@Override
	public void visitConstantFloat(ConstantFloat c) {
		builder.append(c.getBytes());
	}
	
	@Override
	public void visitConstantInteger(ConstantInteger c) {
		builder.append(c.getBytes());
	}
	
	@Override
	public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref c) {
		cp.getConstant(c.getClassIndex()).accept(this);
		builder.append(".");
		cp.getConstant(c.getNameAndTypeIndex()).accept(this);
		builder.append("()");
	}
	
	@Override
	public void visitConstantLong(ConstantLong c) {
		builder.append(c.getBytes());
	}
	@Override
	public void visitConstantMethodref(ConstantMethodref c) {
		cp.getConstant(c.getClassIndex()).accept(this);
		builder.append(".");
		cp.getConstant(c.getNameAndTypeIndex()).accept(this);
		builder.append("()");
	}
	
	@Override
	public void visitConstantNameAndType(ConstantNameAndType c) {
		cp.getConstant(c.getNameIndex()).accept(this);
	}
	
	@Override
	public void visitConstantString(ConstantString c) {
		builder.append("'");
		cp.getConstant(c.getStringIndex()).accept(this);
		builder.append("'");
	}
	
	@Override
	public void visitConstantUtf8(ConstantUtf8 c) {
		builder.append(Utility.replace(c.getBytes(), "\n", "\\n"));
	}
	

	public static final String ILLEGAL_OPCODE = "<illegal opcode>";
	
	public static final String[][] OPCODES_ABSTRACTION = {
	{   // abstraction 0
		"nop", "aconst_null", "iconst_m1", "iconst_0", "iconst_1",
		"iconst_2", "iconst_3", "iconst_4", "iconst_5", "lconst_0",
		"lconst_1", "fconst_0", "fconst_1", "fconst_2", "dconst_0",
		"dconst_1", "bipush", "sipush", "ldc", "ldc_w", "ldc2_w", "iload",
		"lload", "fload", "dload", "aload", "iload_0", "iload_1", "iload_2",
		"iload_3", "lload_0", "lload_1", "lload_2", "lload_3", "fload_0",
		"fload_1", "fload_2", "fload_3", "dload_0", "dload_1", "dload_2",
		"dload_3", "aload_0", "aload_1", "aload_2", "aload_3", "iaload",
		"laload", "faload", "daload", "aaload", "baload", "caload", "saload",
		"istore", "lstore", "fstore", "dstore", "astore", "istore_0",
		"istore_1", "istore_2", "istore_3", "lstore_0", "lstore_1",
		"lstore_2", "lstore_3", "fstore_0", "fstore_1", "fstore_2",
		"fstore_3", "dstore_0", "dstore_1", "dstore_2", "dstore_3",
		"astore_0", "astore_1", "astore_2", "astore_3", "iastore", "lastore",
		"fastore", "dastore", "aastore", "bastore", "castore", "sastore",
		"pop", "pop2", "dup", "dup_x1", "dup_x2", "dup2", "dup2_x1",
		"dup2_x2", "swap", "iadd", "ladd", "fadd", "dadd", "isub", "lsub",
		"fsub", "dsub", "imul", "lmul", "fmul", "dmul", "idiv", "ldiv",
		"fdiv", "ddiv", "irem", "lrem", "frem", "drem", "ineg", "lneg",
		"fneg", "dneg", "ishl", "lshl", "ishr", "lshr", "iushr", "lushr",
		"iand", "land", "ior", "lor", "ixor", "lxor", "iinc", "i2l", "i2f",
		"i2d", "l2i", "l2f", "l2d", "f2i", "f2l", "f2d", "d2i", "d2l", "d2f",
		"i2b", "i2c", "i2s", "lcmp", "fcmpl", "fcmpg",
		"dcmpl", "dcmpg", "ifeq", "ifne", "iflt", "ifge", "ifgt", "ifle",
		"if_icmpeq", "if_icmpne", "if_icmplt", "if_icmpge", "if_icmpgt",
		"if_icmple", "if_acmpeq", "if_acmpne", "goto", "jsr", "ret",
		"tableswitch", "lookupswitch", "ireturn", "lreturn", "freturn",
		"dreturn", "areturn", "return", "getstatic", "putstatic", "getfield",
		"putfield", "invokevirtual", "invokespecial", "invokestatic",
		"invokeinterface", ILLEGAL_OPCODE, "new", "newarray", "anewarray",
		"arraylength", "athrow", "checkcast", "instanceof", "monitorenter",
		"monitorexit", "wide", "multianewarray", "ifnull", "ifnonnull",
		"goto_w", "jsr_w", "breakpoint", ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, "impdep1", "impdep2"
	},
	{   // abstraction 1
		"nop", "aconst", "iconst", "iconst", "iconst",
		"iconst", "iconst", "iconst", "iconst", "lconst",
		"lconst", "fconst", "fconst", "fconst", "dconst",
		"dconst", "bipush", "sipush", "ldc", "ldc", "ldc", "iload",
		"lload", "fload", "dload", "aload", "iload", "iload", "iload",
		"iload", "lload", "lload", "lload", "lload", "fload",
		"fload", "fload", "fload", "dload", "dload", "dload",
		"dload", "aload", "aload", "aload", "aload", "iaload",
		"laload", "faload", "daload", "aaload", "baload", "caload", "saload",
		"istore", "lstore", "fstore", "dstore", "astore", "istore",
		"istore", "istore", "istore", "lstore", "lstore",
		"lstore", "lstore", "fstore", "fstore", "fstore",
		"fstore", "dstore", "dstore", "dstore", "dstore",
		"astore", "astore", "astore", "astore", "iastore", "lastore",
		"fastore", "dastore", "aastore", "bastore", "castore", "sastore",
		"pop", "pop2", "dup", "dup_x1", "dup_x2", "dup2", "dup2_x1",
		"dup2_x2", "swap", "iadd", "ladd", "fadd", "dadd", "isub", "lsub",
		"fsub", "dsub", "imul", "lmul", "fmul", "dmul", "idiv", "ldiv",
		"fdiv", "ddiv", "irem", "lrem", "frem", "drem", "ineg", "lneg",
		"fneg", "dneg", "ishl", "lshl", "ishr", "lshr", "iushr", "lushr",
		"iand", "land", "ior", "lor", "ixor", "lxor", "iinc", "i2l", "i2f",
		"i2d", "l2i", "l2f", "l2d", "f2i", "f2l", "f2d", "d2i", "d2l", "d2f",
		"i2b", "i2c", "i2s", "lcmp", "fcmpl", "fcmpg",
		"dcmpl", "dcmpg", "ifeq", "ifne", "iflt", "ifge", "ifgt", "ifle",
		"if_icmpeq", "if_icmpne", "if_icmplt", "if_icmpge", "if_icmpgt",
		"if_icmple", "if_acmpeq", "if_acmpne", "goto", "jsr", "ret",
		"tableswitch", "lookupswitch", "ireturn", "lreturn", "freturn",
		"dreturn", "areturn", "return", "getstatic", "putstatic", "getfield",
		"putfield", "invokevirtual", "invokespecial", "invokestatic",
		"invokeinterface", ILLEGAL_OPCODE, "new", "newarray", "newarray",
		"arraylength", "athrow", "checkcast", "instanceof", "monitorenter",
		"monitorexit", "", "newarray", "ifnull", "ifnonnull",
		"goto", "jsr", "breakpoint", ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, "impdep1", "impdep2"
	},
	{   // abstraction 2
		"nop", "const", "const", "const", "const",
		"const", "const", "const", "const", "const",
		"const", "const", "const", "const", "const",
		"const", "const", "const", "const", "const", "const", "load",
		"load", "load", "load", "load", "load", "load", "load",
		"load", "load", "load", "load", "load", "load",
		"load", "load", "load", "load", "load", "load",
		"load", "load", "load", "load", "load", "aload",
		"aload", "aload", "aload", "aload", "aload", "aload", "aload",
		"store", "store", "store", "store", "store", "store",
		"store", "store", "store", "store", "store",
		"store", "store", "store", "store", "store",
		"store", "store", "store", "store", "store",
		"store", "store", "store", "store", "astore", "astore",
		"astore", "astore", "astore", "astore", "astore", "astore",
		"pop", "pop", "dup", "dup_x1", "dup_x2", "dup", "dup_x1",
		"dup_x2", "swap", "add", "add", "add", "add", "sub", "sub",
		"sub", "sub", "mul", "mul", "mul", "mul", "div", "div",
		"div", "div", "rem", "rem", "rem", "rem", "neg", "neg",
		"neg", "neg", "shl", "shl", "shr", "shr", "ushr", "ushr",
		"and", "and", "or", "or", "xor", "xor", "inc", "convert", "convert",
		"convert", "convert", "convert", "convert", "convert", "convert", "convert", "convert", "convert", "convert",
		"convert", "convert", "convert", "cmp", "cmpl", "cmp",
		"cmp", "cmp", "ifeq", "ifne", "iflt", "ifge", "ifgt", "ifle",
		"if_cmpeq", "if_cmpne", "if_cmplt", "if_cmpge", "if_cmpgt",
		"if_cmple", "if_cmpeq", "if_cmpne", "goto", "jsr", "ret",
		"tableswitch", "lookupswitch", "return", "return", "return",
		"return", "return", "return", "getstatic", "putstatic", "getfield",
		"putfield", "invokevirtual", "invokespecial", "invokestatic",
		"invokeinterface", ILLEGAL_OPCODE, "new", "newarray", "newarray",
		"arraylength", "throw", "checkcast", "instanceof", "monitorenter",
		"monitorexit", "", "newarray", "ifnull", "ifnonnull",
		"goto", "jsr", "breakpoint", ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, "impdep1", "impdep2"
	}
	};
	
	
	public static final String[] OPCODES_TYPES = {
		"", "A", "I", "I", "I",
		"I", "I", "I", "I", "L",
		"L", "F", "F", "F", "D",
		"D", "I", "I", "VALUE", "VALUE", "VALUE", "I",
		"L", "F", "D", "A", "I", "I", "I",
		"I", "L", "L", "L", "L", "F",
		"F", "F", "F", "D", "D", "D",
		"D", "A", "A", "A", "A", "I",
		"L", "F", "D", "A", "B", "C", "S",
		"I", "L", "F", "D", "A", "I",
		"I", "I", "I", "L", "L",
		"L", "L", "F", "F", "F",
		"F", "D", "D", "D", "D",
		"A", "A", "A", "A", "I", "L",
		"F", "D", "A", "B", "C", "S",
		"", "", "", "", "", "", "",
		"", "", "", "", "", "", "", "",
		"", "", "", "", "", "", "", "",
		"", "", "", "", "", "", "", "",
		"", "", "", "", "", "", "", "",
		"", "", "", "", "", "", "I", "", "",
		"", "", "", "", "", "", "", "", "", "",
		"", "", "", "", "", "",
		"", "", "LABEL", "LABEL", "LABEL", "LABEL", "LABEL", "LABEL",
		"LABEL", "LABEL", "LABEL", "LABEL", "LABEL",
		"LABEL", "LABEL", "LABEL", "LABEL", "LABEL", "LABEL",
		"", "", "", "", "",
		"", "", "", "FIELD", "FIELD", "FIELD",
		"FIELD", "METHOD", "METHOD", "METHOD",
		"METHOD", ILLEGAL_OPCODE, "TYPE", "TYPE", "TYPE",
		"", "TYPE", "TYPE", "TYPE", "",
		"", "", "A", "LABEL", "LABEL",
		"LABEL", "LABEL", "", ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
		 ILLEGAL_OPCODE, "", ""
	};
	
}
