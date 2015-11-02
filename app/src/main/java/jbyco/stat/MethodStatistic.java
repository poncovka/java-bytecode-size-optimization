package jbyco.stat;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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
	
}
