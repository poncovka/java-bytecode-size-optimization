package jbyco.stat;


import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Printer;

public class MethodStatistic extends MethodVisitor {

	Statistics stat;
	
	public MethodStatistic(Statistics stat) {
		super(Opcodes.ASM5);
		this.stat = stat;
	}
	
	public MethodStatistic(Statistics stat, MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
		this.stat = stat;
	}

	void visitOpcode(int opcode) {
		stat.updateOpcodes(Printer.OPCODES[opcode]);
	}
	
	void visitOpcode(String opcode) {
		stat.updateOpcodes(opcode);
	}
	
	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		stat.updateMaxs(maxStack, maxLocals);
		super.visitMaxs(maxStack, maxLocals);
	}
	
	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		
		stat.updateLocalVariables(name);
		super.visitLocalVariable(name, desc, signature, start, end, index);
	}
	
	@Override
	public void visitParameter(String name, int access) {
		
		stat.updateLocalVariables(name);
		super.visitParameter(name, access);
		
	}
	
	@Override
	public void visitInsn(int opcode) {
		visitOpcode(opcode);
		super.visitInsn(opcode);
	}

	@Override
	public void visitLdcInsn(Object cst) {
		visitOpcode("LDC");
		super.visitLdcInsn(cst);
	}
	
	@Override
	public void visitIntInsn(int opcode, int operand) {
		visitOpcode(opcode);
		super.visitIntInsn(opcode, operand);
	}
	
	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		visitOpcode(opcode);
		super.visitFieldInsn(opcode, owner, name, desc);
	}
	
	@Override
	public void visitIincInsn(int var, int increment) {
		visitOpcode("IINC");
		super.visitIincInsn(var, increment);
	}
	
	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
		// TODO Auto-generated method stub
		visitOpcode("INVOKEDYNAMIC");
		super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
	}
	
	@Override
	public void visitJumpInsn(int opcode, Label label) {
		visitOpcode(opcode);
		super.visitJumpInsn(opcode, label);
	}
	
	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
		visitOpcode("LOOKUPSWITCH");
		super.visitLookupSwitchInsn(dflt, keys, labels);
	}	

	@Override
	public void visitMultiANewArrayInsn(String desc, int dims) {
		visitOpcode("MULTIANEWARRAY");
		super.visitMultiANewArrayInsn(desc, dims);
	}
	
	@Override
	public void visitTypeInsn(int opcode, String type) {
		visitOpcode(opcode);
		super.visitTypeInsn(opcode, type);
	}
	
	@Override
	public void visitVarInsn(int opcode, int var) {
		visitOpcode(opcode);
		super.visitVarInsn(opcode, var);
	}
	
	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
		visitOpcode("TABLESWITCH");
		super.visitTableSwitchInsn(min, max, dflt, labels);
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		visitOpcode(opcode);
		super.visitMethodInsn(opcode, owner, name, desc, itf);
	}	
	
	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		visitOpcode("TRYCATCHBLOCK");
		super.visitTryCatchBlock(start, end, handler, type);
	}

}
