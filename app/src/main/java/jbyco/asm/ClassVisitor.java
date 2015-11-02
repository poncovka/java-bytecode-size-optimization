package jbyco.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import jbyco.stat.MethodStatistic;

public class ClassVisitor extends org.objectweb.asm.ClassVisitor {

	MethodVisitor mv;
	
	public ClassVisitor() {
		super(Opcodes.ASM5);		
	}
	
	public ClassVisitor(ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
	}
	
	@Override
	public org.objectweb.asm.MethodVisitor visitMethod(int access, String name, String desc, String signature,
			String[] exceptions) {
		
		org.objectweb.asm.MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		
		//if (mv)
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
	
}
