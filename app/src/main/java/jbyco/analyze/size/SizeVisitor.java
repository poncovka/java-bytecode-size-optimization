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
import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.util.ByteSequence;

/*TODO
 * sum constant pool
 * sum attributes
 * count names of classes, fields, methods, local variables, ...
 */

public class SizeVisitor implements org.apache.bcel.classfile.Visitor  {
	
	// real size of node
	int realSize;
	
	// full size of node
	int fullSize;	
	
	// currently visited node
	Object visited;
	
	// constant pool
	ConstantPool pool;
	
	// dictionary of sizes
	 SizeMap map;
	
	public SizeVisitor(SizeMap map) {
		this.map = map;
	}
	
	public void init(ConstantPool pool) {
		this.pool = pool;
		this.visited = null;
	}
	
	public boolean startVisit(Object node) {
		
		if (visited == null) {		
			visited = node;
			realSize = 0;
			fullSize = 0;
			return true;
		}
		
		return false;
	}

	public boolean endVisit(Object node) {
		
		if (visited == node) {	
			visited = null;
			return true;
		}
		
		return false;
	}
	
	public boolean finishVisit(Object node, String name, int real, int full) {
		
		// update values
		realSize = real;
		fullSize += full; 
		
		// end visit
		if (endVisit(node)) {
			
			// update map
			map.add(name, realSize, fullSize);
			return true;
		}
		
		return false;
	}
	
	public boolean finishVisit(Constant c, int real, int full) {	
		
		// get name
		String name = Constants.CONSTANT_NAMES[c.getTag()];
		
		// finish visit
		if (finishVisit(c, name, real, full)) {
			
			// update map
			map.add("constants", realSize, fullSize);
			return true;
		}
		return false;
	}

	public boolean finishVisit(Attribute attr, int real, int full) {
		
		// get name
		String name = "ATTRIBUTE_" + attr.getName();
		
		// finish visit
		if (finishVisit(attr, name, real, full)) {
			
			// update map
			map.add("attributes", realSize, fullSize);
			return true;
		}
		return false;
	}
	
	public boolean finishVisit(Instruction i, int real, int full) {
		
		// get name
		String name = "INSTRUCTION_" + i.getName();
		
		// finish visit
		if (finishVisit(i, name, real, full)) {
			
			// update map
			map.add("instructions", realSize, fullSize);
			return true;
		}
		
		return false;
	}
	
	public void visitConst(int index) {
		if (index != 0) {
			pool.getConstant(index).accept(this);
		}
	}

	@Override
	public void visitAnnotation(Annotations attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		finishVisit(attr, attr.getLength(), attr.getLength());
	}

	@Override
	public void visitAnnotationDefault(AnnotationDefault attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		finishVisit(attr, attr.getLength(), attr.getLength());
	}

	@Override
	public void visitAnnotationEntry(AnnotationEntry entry) {
		// nothing		
	}

	@Override
	public void visitCode(Code code) {
		startVisit(code);
		visitConst(code.getNameIndex());
		
		for(CodeException e : code.getExceptionTable()) {
			e.accept(this);
		}
		
		for(Attribute a : code.getAttributes()) {
			a.accept(this);
		}
		
		int full  = 6 /* initial */ 
				  + 2 /* max_stack */ 
				  + 2 /* max_locals */
				  + 4 /* code_length */
				  + code.getCode().length /* code */
				  + 2 /* exception_table_length */
				  + 2 /* attributes_count */; 
		
		if (finishVisit(code, code.getLength(), full)) {
			visitCodeInstructions(code);
		}
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
				visitCodeInstruction(i);
			}
			
		} catch (ClassGenException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void visitCodeInstruction(Instruction i) {
		startVisit(i);
		
		if(i instanceof CPInstruction) {
			visitConst(((CPInstruction)i).getIndex());
		}
		
		finishVisit(i, i.getLength(), i.getLength());
	}

	@Override
	public void visitCodeException(CodeException e) {
		startVisit(e);
		visitConst(e.getCatchType());		
		finishVisit(e, "exceptions", 8, 8);
	}

	@Override
	public void visitConstantClass(ConstantClass c) {
		startVisit(c);
		visitConst(c.getNameIndex());
		finishVisit(c, 3, 3);
	}

	@Override
	public void visitConstantDouble(ConstantDouble c) {
		startVisit(c);
		finishVisit(c, 9, 9);
	}

	@Override
	public void visitConstantFieldref(ConstantFieldref c) {
		startVisit(c);
		visitConst(c.getClassIndex());
		visitConst(c.getNameAndTypeIndex());
		finishVisit(c, 5, 5);
	}

	@Override
	public void visitConstantFloat(ConstantFloat c) {

		startVisit(c);
		finishVisit(c, 5, 5);
	}

	@Override
	public void visitConstantInteger(ConstantInteger c) {
		startVisit(c);
		finishVisit(c, 5, 5);
	}

	@Override
	public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref c) {
		startVisit(c);
		visitConst(c.getClassIndex());
		visitConst(c.getNameAndTypeIndex());
		finishVisit(c, 5, 5);
	}

	@Override
	public void visitConstantLong(ConstantLong c) {
		startVisit(c);
		finishVisit(c, 9, 9);
	}

	@Override
	public void visitConstantMethodref(ConstantMethodref c) {
		startVisit(c);
		visitConst(c.getClassIndex());
		visitConst(c.getNameAndTypeIndex());
		finishVisit(c, 5, 5);
	}

	@Override
	public void visitConstantNameAndType(ConstantNameAndType c) {
		startVisit(c);
		visitConst(c.getNameIndex());
		visitConst(c.getSignatureIndex());
		finishVisit(c, 5, 5);
	}

	@Override
	public void visitConstantPool(ConstantPool pool) {
		// nothing
	}

	@Override
	public void visitConstantString(ConstantString c) {
		startVisit(c);
		visitConst(c.getStringIndex());
		finishVisit(c, 3, 3);
	}

	@Override
	public void visitConstantUtf8(ConstantUtf8 c) {
		
		try {	
			// start visit
			startVisit(c);
			
			// calculate size of utf8 string
			int size = 3 + c.getBytes().getBytes("UTF-8").length;
			
			// finish visit
			finishVisit(c, size, size);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitConstantValue(ConstantValue attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		visitConst(attr.getConstantValueIndex());
		finishVisit(attr, attr.getLength(), attr.getLength());
	}

	@Override
	public void visitDeprecated(Deprecated attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		finishVisit(attr, attr.getLength(), attr.getLength());
	}

	@Override
	public void visitEnclosingMethod(EnclosingMethod attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		visitConst(attr.getEnclosingClassIndex());
		visitConst(attr.getEnclosingMethodIndex());
		finishVisit(attr, attr.getLength(), attr.getLength());
	}

	@Override
	public void visitExceptionTable(ExceptionTable attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		
		for(int i : attr.getExceptionIndexTable()) {
			visitConst(i);
		}
		
		int full = 6 /* initial */ 
				 + 2 /* number of exceptions */; 
		
		finishVisit(attr, attr.getLength(), full);
	}

	@Override
	public void visitField(Field field) {
		
		startVisit(field);
		visitConst(field.getNameIndex());
		visitConst(field.getSignatureIndex());
		
		for(Attribute a : field.getAttributes()) {
			a.accept(this);
		}
		
		finishVisit(field, "fields", 8, 8);
	}

	@Override
	public void visitInnerClass(InnerClass cls) {
		startVisit(cls);
		visitConst(cls.getInnerClassIndex());
		visitConst(cls.getOuterClassIndex());
		visitConst(cls.getInnerNameIndex());
		finishVisit(cls, "inner classes", 8, 8);
	}

	@Override
	public void visitInnerClasses(InnerClasses attr) {
		
		startVisit(attr);
		visitConst(attr.getNameIndex());
		
		for(InnerClass cls : attr.getInnerClasses()) {
			cls.accept(this);
		}
		
		int full = 6 /* initial */ 
				 + 2 /* number of classes */;
		
		finishVisit(attr, attr.getLength(), full);
	}

	@Override
	public void visitJavaClass(JavaClass klass) {
		startVisit(klass);
		int size = klass.getBytes().length;
		finishVisit(klass, "class files", size, size);
	}

	@Override
	public void visitLineNumber(LineNumber num) {
		// nothing
	}

	@Override
	public void visitLineNumberTable(LineNumberTable attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		finishVisit(attr, attr.getLength(), attr.getLength());
	}

	@Override
	public void visitLocalVariable(LocalVariable var) {
		startVisit(var);
		visitConst(var.getNameIndex());
		visitConst(var.getSignatureIndex());
		finishVisit(var, "local variables", 10, 10);
	}

	@Override
	public void visitLocalVariableTable(LocalVariableTable attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		
		for(LocalVariable var : attr.getLocalVariableTable()) {
			var.accept(this);
		}
		
		int full = 6 /* initial */ 
				 + 2 /* local variable table length */; 
		
		finishVisit(attr, attr.getLength(), full);
	}

	@Override
	public void visitLocalVariableTypeTable(LocalVariableTypeTable attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		
		for(LocalVariable var : attr.getLocalVariableTypeTable()) {
			var.accept(this);
		}
		
		int full = 6 /* initial */ 
				 + 2 /* local variable table length */; 
		
		finishVisit(attr, attr.getLength(), full);
	}

	@Override
	public void visitMethod(Method method) {
		startVisit(method);
		visitConst(method.getNameIndex());
		visitConst(method.getSignatureIndex());
		
		for(Attribute a : method.getAttributes()) {
			a.accept(this);
		}
				
		finishVisit(method, "methods", 8, 8);
	}

	@Override
	public void visitParameterAnnotation(ParameterAnnotations attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		finishVisit(attr, attr.getLength(), attr.getLength());
	}

	@Override
	public void visitSignature(Signature attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		visitConst(attr.getSignatureIndex());
		finishVisit(attr, attr.getLength(), attr.getLength());
	}

	@Override
	public void visitSourceFile(SourceFile attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		visitConst(attr.getSourceFileIndex());
		finishVisit(attr, attr.getLength(), attr.getLength());
	}

	@Override
	public void visitStackMap(StackMap attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		finishVisit(attr, attr.getLength(), attr.getLength());
	}

	@Override
	public void visitStackMapEntry(StackMapEntry entry) {
		// nothing
	}

	@Override
	public void visitStackMapTable(StackMapTable attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		finishVisit(attr, attr.getLength(), attr.getLength());
	}

	@Override
	public void visitStackMapTableEntry(StackMapTableEntry entry) {
		// nothing
	}

	@Override
	public void visitSynthetic(Synthetic attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		finishVisit(attr, attr.getLength(), attr.getLength());
	}

	@Override
	public void visitUnknown(Unknown attr) {
		startVisit(attr);
		visitConst(attr.getNameIndex());
		finishVisit(attr, attr.getLength(), attr.getLength());
	}
}
