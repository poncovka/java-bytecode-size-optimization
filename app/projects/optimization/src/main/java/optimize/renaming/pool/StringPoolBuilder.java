package jbyco.optimize.renaming.pool;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import jbyco.optimize.renaming.graph.Graph;

public class StringPoolBuilder extends ClassVisitor {

	StringPool pool;
	String klass;
	
	public StringPoolBuilder() {
		super(Opcodes.ASM5);
		this.pool = new StringPool();
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		
		// init
		this.klass = name;
		
		// klasses
		pool.addType(name);
		pool.addType(superName);
		
		// get interfaces
		for (String interfaceName : interfaces) {
			pool.addType(interfaceName);
		}
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {
		
		// klass
		pool.addType(owner);
		
		// method
		if (name != null) {
			pool.addMethod(owner, name, desc);
			pool.addType(desc);
		}
	}

	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
		
		// internal name of the inner class
		pool.addType(name);
		
		// internal name of the outer class
		if (outerName != null) {
			pool.addType(outerName);
		}
		
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {	
		
		// field
		pool.addField(this.klass, name, desc);
		// type
		pool.addType(desc);
		return null;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		
		// method
		pool.addMethod(this.klass, name, desc);
		// type
		pool.addType(desc);
		return null;
	}

	public class MethodPoolBuilder extends MethodVisitor {
		
		public MethodPoolBuilder(Graph graph) {
			super(Opcodes.ASM5);
		}

		public void processConstantValue(Object cst) {
			
			if (cst instanceof Type) {
				
				// type
				Type type = (Type) cst;
				pool.addType(type.getInternalName()); 
				
			} else if (cst instanceof Handle) {
				
				// klass and type
				Handle handle = (Handle)cst;
				pool.addType(handle.getOwner());
				pool.addType(handle.getDesc());
								
				// field
				if (handle.getTag() <= Opcodes.H_PUTSTATIC) {
					pool.addField(handle.getOwner(), handle.getName(), handle.getDesc());
				}
				// method
				else {
					pool.addMethod(handle.getOwner(), handle.getName(), handle.getDesc());
				}
			} 
		}

		
		@Override
		public void visitTypeInsn(int opcode, String type) {
			// type
			pool.addType(type);
		}

		@Override
		public void visitFieldInsn(int opcode, String owner, String name, String desc) {
			
			// klass
			pool.addType(owner);
			// field
			pool.addField(owner, name, desc);
			// type of field
			pool.addType(desc);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
			
			// klass
			pool.addType(owner);
			// method
			pool.addMethod(owner, name, desc);
			// type
			pool.addType(desc);
		}

		@Override
		public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
			
			// method - nothing?
			pool.addType(desc);
			
			// handle
			processConstantValue(bsm);
			
			// arguments
			for (Object arg : bsmArgs) {
				processConstantValue(arg);
			}
		}

		@Override
		public void visitLdcInsn(Object cst) {
			processConstantValue(cst);
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
