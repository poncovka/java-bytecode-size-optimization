package jbyco.stat;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassStatistic extends ClassVisitor{

	Statistics stat;
	
	public ClassStatistic(Statistics stat) {
		super(Opcodes.ASM5);
		this.stat = stat;
	}
	
	public ClassStatistic(Statistics stat, ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		this.stat = stat;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		
		stat.updateClasses(name);
		
		if (superName != null) {
			stat.updateStrings(superName);
		}
		
		for(String iname:interfaces) {
			stat.updateStrings(iname);	
		}
		
		super.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		
		stat.updateMethods(name);
		
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		
		if (mv  != null) {
			mv = new MethodStatistic(stat, mv);
		}
		else {
			mv = new MethodStatistic(stat);
		}
		
		return mv;
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		
		stat.updateClassVariables(name);
		return super.visitField(access, name, desc, signature, value);
	}
}
