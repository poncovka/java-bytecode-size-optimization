package jbyco.analyze.size;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.AnnotationDefault;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Annotations;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.Constant;
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
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.util.ByteSequence;

/*TODO
 * sum constant pool
 * sum attributes
 * count names of classes, fields, methods, local variables, ...
 */

public class SizeVisitor implements org.apache.bcel.classfile.Visitor  {
	
	// constant pool
	ConstantPool pool;
	
	// dictionary of sizes
	 SizeMap map;
	
	public SizeVisitor(SizeMap map) {
		this.map = map;
	}
	
	public void init(ConstantPool pool) {
		this.pool = pool;
	}
	
	public void add(String name, int real) {
		map.add(name, real);
	}
	
	public void add(Constant c, int real) {
		
		//int real = 0;
		String name = Constants.CONSTANT_NAMES[c.getTag()];
		add(name, real);
		add("ALL_CONSTANTS", real);
	}

	public void add(Attribute attr) {
		
		int real = attr.getLength();
		String name = "ATTRIBUTE_" + attr.getName();
		
		add(name, real);
		add("ALL_ATTRIBUTES", real);
	}
	
	public void add(Instruction i) {

		int real = i.getLength();
		String name = "INSTRUCTION_" + i.getName();
		
		add(name, real);
		add("ALL_INSTRUCTIONS", real);
	}

	@Override
	public void visitCode(Code code) {		
		add(code);
		visitCodeInstructions(code);
	}
	
	public void visitCodeInstructions(Code code) {
		
		try {		
			
			// get byte sequence
			ByteSequence seq = new ByteSequence(code.getCode());
			
			// read instructions
			while(seq.available() > 0) {
				
				// get instruction
				Instruction i = Instruction.readInstruction(seq);
				
				// visit instruction
				add(i);
			}
			
		} catch (ClassGenException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitConstantClass(ConstantClass c) {
		add(c, 3);
	}

	@Override
	public void visitConstantDouble(ConstantDouble c) {
		add(c, 9);
	}

	@Override
	public void visitConstantFieldref(ConstantFieldref c) {
		add(c, 5);
	}

	@Override
	public void visitConstantFloat(ConstantFloat c) {
		add(c, 5);
	}

	@Override
	public void visitConstantInteger(ConstantInteger c) {
		add(c, 5);
	}

	@Override
	public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref c) {
		add(c, 5);
	}

	@Override
	public void visitConstantLong(ConstantLong c) {
		add(c, 9);
	}

	@Override
	public void visitConstantMethodref(ConstantMethodref c) {
		add(c, 5);
	}

	@Override
	public void visitConstantNameAndType(ConstantNameAndType c) {
		add(c, 5);
	}

	@Override
	public void visitConstantPool(ConstantPool pool) {
		add("ALL_CONSTANT_POOL", pool.getLength());
	}

	@Override
	public void visitConstantString(ConstantString c) {
		add(c, 3);
	}

	@Override
	public void visitConstantUtf8(ConstantUtf8 c) {
		
		try {	
			// calculate size of utf8 string
			int size = 3 + c.getBytes().getBytes("UTF-8").length;
			
			// finish visit
			add(c, size);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitField(Field field) {
		
		for(Attribute a : field.getAttributes()) {
			a.accept(this);
		}
		
		add("fields", 8);
	}

	@Override
	public void visitInnerClass(InnerClass cls) {
		add("inner classes", 8);
	}

	@Override
	public void visitJavaClass(JavaClass klass) {
		int size = klass.getBytes().length;
		add("class files", size);
	}



	@Override
	public void visitMethod(Method method) {		
		for(Attribute a : method.getAttributes()) {
			a.accept(this);
		}
				
		add("methods", 8);
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
	public void visitCodeException(CodeException arg0) {
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
	public void visitInnerClasses(InnerClasses arg0) {
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
