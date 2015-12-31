package jbyco.analyze.size;

import java.io.UnsupportedEncodingException;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.AnnotationDefault;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Annotations;
import org.apache.bcel.classfile.Attribute;
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
import org.apache.bcel.classfile.Visitor;

public class SizeVisitor implements Visitor {
	
	// real size of node
	int realSize;
	
	// full size of node
	int fullSize;	
	
	// currently visited node
	Object visited;
	
	// constant pool
	ConstantPool pool;
	
	// dictionary of file statistics
	SizeMap map;
	
	public SizeVisitor() {
		this.map = new SizeMap();
	}
	
	public void init(ConstantPool pool) {

		this.pool = pool;
		this.visited = null;
		
		map.init();
	}
	
	public SizeMap getStatistics() {
		return map;
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

	public void acceptFromConst(int index) {
		pool.getConstant(index).accept(this);
	}
	
	public String getConstName(int tag) {
		return Constants.CONSTANT_NAMES[tag];
	}
	
	public String getAttrName(Attribute a) {
		return pool.getConstant(a.getNameIndex()).toString();
	}

	@Override
	public void visitAnnotation(Annotations attrs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitAnnotationDefault(AnnotationDefault attr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitAnnotationEntry(AnnotationEntry entry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCode(Code attr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCodeException(CodeException arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantClass(ConstantClass c) {
		
		startVisit(c);
		acceptFromConst(c.getNameIndex());
		
		realSize = 3;
		fullSize += realSize;
		
		if (endVisit(c)) {
			map.add(getConstName(c.getTag()), realSize, fullSize);
		}		
	}

	@Override
	public void visitConstantDouble(ConstantDouble c) {
		
		startVisit(c);
		
		realSize = 9;
		fullSize += realSize;
		
		if (endVisit(c)) {
			map.add(getConstName(c.getTag()), realSize, fullSize);
		}
	}

	@Override
	public void visitConstantFieldref(ConstantFieldref c) {
		
		startVisit(c);
		acceptFromConst(c.getClassIndex());
		acceptFromConst(c.getNameAndTypeIndex());
		
		realSize = 5;
		fullSize += realSize;
		
		if (endVisit(c)) {
			map.add(getConstName(c.getTag()), realSize, fullSize);
		}
	}

	@Override
	public void visitConstantFloat(ConstantFloat c) {

		startVisit(c);
		
		realSize = 5;
		fullSize += realSize;
		
		if (endVisit(c)) {
			map.add(getConstName(c.getTag()), realSize, fullSize);
		}
	}

	@Override
	public void visitConstantInteger(ConstantInteger c) {
		
		startVisit(c);
		
		realSize = 5;
		fullSize += realSize;
		
		if (endVisit(c)) {
			map.add(getConstName(c.getTag()), realSize, fullSize);
		}
	}

	@Override
	public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref c) {
		
		startVisit(c);
		acceptFromConst(c.getClassIndex());
		acceptFromConst(c.getNameAndTypeIndex());
		
		realSize = 5;
		fullSize += realSize;
		
		if (endVisit(c)) {
			map.add(getConstName(c.getTag()), realSize, fullSize);
		}
	}

	@Override
	public void visitConstantLong(ConstantLong c) {
		
		startVisit(c);
		
		realSize = 9;
		fullSize += realSize;
		
		if (endVisit(c)) {
			map.add(getConstName(c.getTag()), realSize, fullSize);
		}
	}

	@Override
	public void visitConstantMethodref(ConstantMethodref c) {
		
		startVisit(c);
		acceptFromConst(c.getClassIndex());
		acceptFromConst(c.getNameAndTypeIndex());
		
		realSize = 5;
		fullSize += realSize;
		
		if (endVisit(c)) {
			map.add(getConstName(c.getTag()), realSize, fullSize);
		}
	}

	@Override
	public void visitConstantNameAndType(ConstantNameAndType c) {
		
		startVisit(c);
		acceptFromConst(c.getNameIndex());
		acceptFromConst(c.getSignatureIndex());
		
		realSize = 5;
		fullSize += realSize;
		
		if (endVisit(c)) {
			map.add(getConstName(c.getTag()), realSize, fullSize);
		}
	}

	@Override
	public void visitConstantPool(ConstantPool pool) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstantString(ConstantString c) {
		
		startVisit(c);
		acceptFromConst(c.getStringIndex());
		
		realSize = 3;
		fullSize += realSize;
		
		if (endVisit(c)) {
			map.add(getConstName(c.getTag()), realSize, fullSize);
		}
	}

	@Override
	public void visitConstantUtf8(ConstantUtf8 c) {
		
		try {	
			startVisit(c);
			
			realSize = 3 + c.getBytes().getBytes("UTF-8").length;
			fullSize += realSize;
			
			if (endVisit(c)) {
				map.add(getConstName(c.getTag()), realSize, fullSize);
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitConstantValue(ConstantValue attr) {
		
		startVisit(attr);
		acceptFromConst(attr.getNameIndex());
		acceptFromConst(attr.getConstantValueIndex());
		
		realSize = attr.getLength();
		fullSize += realSize;
		
		if (endVisit(attr)) {
			map.add("ATTRIBUTE_" + getAttrName(attr), realSize, fullSize);
		}
	}

	@Override
	public void visitDeprecated(Deprecated attr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitEnclosingMethod(EnclosingMethod attr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitExceptionTable(ExceptionTable table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitField(Field field) {
		
		startVisit(field);
		acceptFromConst(field.getNameIndex());
		acceptFromConst(field.getSignatureIndex());
		
		for(Attribute a : field.getAttributes()) {
			a.accept(this);
		}
		
		realSize = 8;
		fullSize += realSize;
		
		if (endVisit(field)) {
			map.add("field", realSize, fullSize);
		}
	}

	@Override
	public void visitInnerClass(InnerClass cls) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitInnerClasses(InnerClasses attr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitJavaClass(JavaClass klass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLineNumber(LineNumber num) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLineNumberTable(LineNumberTable table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLocalVariable(LocalVariable var) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLocalVariableTable(LocalVariableTable table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitLocalVariableTypeTable(LocalVariableTypeTable table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitMethod(Method method) {
		
		startVisit(method);
		acceptFromConst(method.getNameIndex());
		acceptFromConst(method.getSignatureIndex());
		
		for(Attribute a : method.getAttributes()) {
			a.accept(this);
		}
		
		realSize = 8;
		fullSize += realSize;
		
		if (endVisit(method)) {
			map.add("method", realSize, fullSize);
		}
	}

	@Override
	public void visitParameterAnnotation(ParameterAnnotations attr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitSignature(Signature attr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitSourceFile(SourceFile attr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitStackMap(StackMap map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitStackMapEntry(StackMapEntry entry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitStackMapTable(StackMapTable attr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitStackMapTableEntry(StackMapTableEntry entry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitSynthetic(Synthetic attr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitUnknown(Unknown attr) {
		// TODO Auto-generated method stub
		
	}
	

	
	
}
