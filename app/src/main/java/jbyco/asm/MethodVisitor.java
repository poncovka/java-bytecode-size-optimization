package jbyco.asm;

import org.objectweb.asm.Opcodes;

public class MethodVisitor extends org.objectweb.asm.MethodVisitor{

	public MethodVisitor() {
		super(Opcodes.ASM5);
	}
	
	public MethodVisitor(MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
	}
	
}
