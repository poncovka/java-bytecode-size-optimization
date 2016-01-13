package jbyco.lib;

import org.apache.bcel.classfile.AnnotationDefault;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Annotations;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
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
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.Deprecated;
import org.apache.bcel.classfile.EnclosingMethod;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.InnerClass;
import org.apache.bcel.classfile.InnerClasses;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.LocalVariableTypeTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.ParameterAnnotations;
import org.apache.bcel.classfile.Signature;
import org.apache.bcel.classfile.SourceFile;
import org.apache.bcel.classfile.StackMap;
import org.apache.bcel.classfile.StackMapEntry;
import org.apache.bcel.classfile.StackMapTable;
import org.apache.bcel.classfile.StackMapTableEntry;
import org.apache.bcel.classfile.Synthetic;
import org.apache.bcel.classfile.Unknown;
import org.apache.bcel.generic.*;

public class BytecodeVisitor extends EmptyVisitor implements org.apache.bcel.classfile.Visitor {

	@Override
	public void visitAALOAD(AALOAD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitAASTORE(AASTORE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitACONST_NULL(ACONST_NULL arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitALOAD(ALOAD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitANEWARRAY(ANEWARRAY arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitARETURN(ARETURN arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitARRAYLENGTH(ARRAYLENGTH arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitASTORE(ASTORE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitATHROW(ATHROW arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitAllocationInstruction(AllocationInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitArithmeticInstruction(ArithmeticInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitArrayInstruction(ArrayInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitBALOAD(BALOAD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitBASTORE(BASTORE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitBIPUSH(BIPUSH arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitBREAKPOINT(BREAKPOINT arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitBranchInstruction(BranchInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCALOAD(CALOAD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCASTORE(CASTORE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCHECKCAST(CHECKCAST arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCPInstruction(CPInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantPushInstruction(ConstantPushInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConversionInstruction(ConversionInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitD2F(D2F arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitD2I(D2I arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitD2L(D2L arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDADD(DADD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDALOAD(DALOAD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDASTORE(DASTORE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDCMPG(DCMPG arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDCMPL(DCMPL arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDCONST(DCONST arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDDIV(DDIV arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDLOAD(DLOAD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDMUL(DMUL arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDNEG(DNEG arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDREM(DREM arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDRETURN(DRETURN arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDSTORE(DSTORE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDSUB(DSUB arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDUP(DUP arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDUP2(DUP2 arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDUP2_X1(DUP2_X1 arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDUP2_X2(DUP2_X2 arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDUP_X1(DUP_X1 arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDUP_X2(DUP_X2 arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitExceptionThrower(ExceptionThrower arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitF2D(F2D arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitF2I(F2I arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitF2L(F2L arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFADD(FADD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFALOAD(FALOAD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFASTORE(FASTORE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFCMPG(FCMPG arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFCMPL(FCMPL arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFCONST(FCONST arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFDIV(FDIV arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFLOAD(FLOAD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFMUL(FMUL arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFNEG(FNEG arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFREM(FREM arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFRETURN(FRETURN arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFSTORE(FSTORE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFSUB(FSUB arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFieldInstruction(FieldInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFieldOrMethod(FieldOrMethod arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitGETFIELD(GETFIELD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitGETSTATIC(GETSTATIC arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitGOTO(GOTO arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitGOTO_W(GOTO_W arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitGotoInstruction(GotoInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitI2B(I2B arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitI2C(I2C arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitI2D(I2D arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitI2F(I2F arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitI2L(I2L arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitI2S(I2S arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIADD(IADD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIALOAD(IALOAD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIAND(IAND arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIASTORE(IASTORE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitICONST(ICONST arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIDIV(IDIV arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIFEQ(IFEQ arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIFGE(IFGE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIFGT(IFGT arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIFLE(IFLE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIFLT(IFLT arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIFNE(IFNE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIFNONNULL(IFNONNULL arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIFNULL(IFNULL arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIF_ACMPEQ(IF_ACMPEQ arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIF_ACMPNE(IF_ACMPNE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIF_ICMPEQ(IF_ICMPEQ arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIF_ICMPGE(IF_ICMPGE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIF_ICMPGT(IF_ICMPGT arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIF_ICMPLE(IF_ICMPLE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIF_ICMPLT(IF_ICMPLT arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIF_ICMPNE(IF_ICMPNE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIINC(IINC arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitILOAD(ILOAD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIMPDEP1(IMPDEP1 arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIMPDEP2(IMPDEP2 arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIMUL(IMUL arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitINEG(INEG arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitINSTANCEOF(INSTANCEOF arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitINVOKEINTERFACE(INVOKEINTERFACE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitINVOKESPECIAL(INVOKESPECIAL arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitINVOKESTATIC(INVOKESTATIC arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitINVOKEVIRTUAL(INVOKEVIRTUAL arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIOR(IOR arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIREM(IREM arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIRETURN(IRETURN arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitISHL(ISHL arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitISHR(ISHR arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitISTORE(ISTORE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitISUB(ISUB arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIUSHR(IUSHR arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIXOR(IXOR arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIfInstruction(IfInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitInvokeInstruction(InvokeInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitJSR(JSR arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitJSR_W(JSR_W arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitJsrInstruction(JsrInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitL2D(L2D arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitL2F(L2F arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitL2I(L2I arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLADD(LADD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLALOAD(LALOAD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLAND(LAND arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLASTORE(LASTORE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLCMP(LCMP arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLCONST(LCONST arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLDC(LDC arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLDC2_W(LDC2_W arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLDIV(LDIV arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLLOAD(LLOAD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLMUL(LMUL arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLNEG(LNEG arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLOOKUPSWITCH(LOOKUPSWITCH arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLOR(LOR arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLREM(LREM arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLRETURN(LRETURN arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLSHL(LSHL arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLSHR(LSHR arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLSTORE(LSTORE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLSUB(LSUB arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLUSHR(LUSHR arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLXOR(LXOR arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLoadClass(LoadClass arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLoadInstruction(LoadInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLocalVariableInstruction(LocalVariableInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitMONITORENTER(MONITORENTER arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitMONITOREXIT(MONITOREXIT arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitMULTIANEWARRAY(MULTIANEWARRAY arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitNEW(NEW arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitNEWARRAY(NEWARRAY arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitNOP(NOP arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitPOP(POP arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitPOP2(POP2 arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitPUTFIELD(PUTFIELD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitPUTSTATIC(PUTSTATIC arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitPopInstruction(PopInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitPushInstruction(PushInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitRET(RET arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitRETURN(RETURN arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitReturnInstruction(ReturnInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitSALOAD(SALOAD arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitSASTORE(SASTORE arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitSIPUSH(SIPUSH arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitSWAP(SWAP arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitSelect(Select arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitStackConsumer(StackConsumer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitStackInstruction(StackInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitStackProducer(StackProducer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitStoreInstruction(StoreInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitTABLESWITCH(TABLESWITCH arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitTypedInstruction(TypedInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitUnconditionalBranch(UnconditionalBranch arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitVariableLengthInstruction(VariableLengthInstruction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitAnnotation(Annotations arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitAnnotationDefault(AnnotationDefault arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitAnnotationEntry(AnnotationEntry arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCode(Code arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCodeException(CodeException arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantClass(ConstantClass arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantDouble(ConstantDouble arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantFieldref(ConstantFieldref arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantFloat(ConstantFloat arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantInteger(ConstantInteger arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantLong(ConstantLong arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantMethodref(ConstantMethodref arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantNameAndType(ConstantNameAndType arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantPool(ConstantPool arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantString(ConstantString arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantUtf8(ConstantUtf8 arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantValue(ConstantValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDeprecated(Deprecated arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitEnclosingMethod(EnclosingMethod arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitExceptionTable(ExceptionTable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitField(Field arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitInnerClass(InnerClass arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitInnerClasses(InnerClasses arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitJavaClass(JavaClass arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLineNumber(LineNumber arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLineNumberTable(LineNumberTable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLocalVariable(LocalVariable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLocalVariableTable(LocalVariableTable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLocalVariableTypeTable(LocalVariableTypeTable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitMethod(Method arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitParameterAnnotation(ParameterAnnotations arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitSignature(Signature arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitSourceFile(SourceFile arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitStackMap(StackMap arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitStackMapEntry(StackMapEntry arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitStackMapTable(StackMapTable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitStackMapTableEntry(StackMapTableEntry arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitSynthetic(Synthetic arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitUnknown(Unknown arg0) {
		// TODO Auto-generated method stub
		
	}

}
