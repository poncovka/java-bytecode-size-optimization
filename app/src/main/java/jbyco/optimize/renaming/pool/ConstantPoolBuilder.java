package jbyco.optimize.renaming.pool;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import jbyco.optimize.renaming.graph.Graph;

public class ConstantPoolBuilder extends ClassVisitor {

	ConstantPool pool;
	String klass;
	
	public ConstantPoolBuilder() {
		super(Opcodes.ASM5);
		this.pool = new ConstantPool();
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		
		// init
		this.klass = name;
		
		// klasses
		pool.addKlass(name);
		pool.addKlass(superName);
		
		// get interfaces
		for (String interfaceName : interfaces) {
			pool.addKlass(interfaceName);
		}
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {
		
		// klass
		pool.addKlass(owner);
		
		// method
		if (name != null) {
			pool.addMethod(owner, name, desc);
			pool.addType(desc);
		}
	}

	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
		
		// internal name of the inner class
		pool.addKlass(name);
		
		// internal name of the outer class
		if (outerName != null) {
			pool.addKlass(outerName);
		}
		
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {		
		pool.addField(this.klass, name, desc);
		pool.addType(desc);
		return null;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		pool.addMethod(this.klass, name, desc);
		pool.addType(desc);
		return null;
	}

	public class MethodPoolBuilder extends MethodVisitor {
		
		public MethodPoolBuilder(Graph graph) {
			super(Opcodes.ASM5);
		}

		@Override
		public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
			// nothing ?
		}

		@Override
		public void visitTypeInsn(int opcode, String type) {
			// type
			pool.addType(type);
		}

		@Override
		public void visitFieldInsn(int opcode, String owner, String name, String desc) {
			
			// klass
			pool.addKlass(owner);
			// field
			pool.addField(owner, name, desc);
			// type of field
			pool.addType(desc);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
			
			// klass
			pool.addKlass(owner);
			// method
			pool.addMethod(owner, name, desc);
			// type
			pool.addType(desc);
		}

		@Override
		public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
			// nothing ?
		}

		@Override
		public void visitLdcInsn(Object cst) {
			// TODO
		}

		@Override
		public void visitMultiANewArrayInsn(String desc, int dims) {
			// type
			pool.addType(desc);
		}

		@Override
		public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
			// type
			pool.addType(type);
		}
		
	}
	
}
