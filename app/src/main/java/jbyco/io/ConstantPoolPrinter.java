package jbyco.io;

import org.apache.bcel.Constants;
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
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.JavaClass;

import jbyco.lib.Utils;

public class ConstantPoolPrinter extends EmptyVisitor {

	StringBuffer buffer = new StringBuffer();
	
	public void print(JavaClass klass) {
		
		ConstantPool cp = klass.getConstantPool();
		Constant[] pool = cp.getConstantPool();
		
		// print header
		String klassname = getClassName(cp, klass.getClassNameIndex());
		System.out.println("// constant pool of " + klassname);
		System.out.println("[");

		// print constants
		for (int i = 1; i < pool.length; i++) {
			
			// get constant
			Constant c = pool[i];
			String name = getConstantName(c);
			String items = getConstantItems(c);
			
			// print constant
			System.out.printf("   %-5s %-20s (%s)\n", i + ":", name, items);	
		}
		
		// print end
		System.out.println("]\n");
	}
	
	private String getClassName(ConstantPool cp, int index) {

		ConstantClass c1 = (ConstantClass)cp.getConstant(index);
		ConstantUtf8  c2 = (ConstantUtf8)cp.getConstant(c1.getNameIndex());
		return c2.getBytes();
	}
	
	private String getConstantName(Constant c) {
		return Constants.CONSTANT_NAMES[c.getTag()];
	}
	
	private String getConstantItems(Constant c) {
		buffer.setLength(0);
		c.accept(this);
		return buffer.toString();
	}

	@Override
	public void visitConstantClass(ConstantClass c) {
		buffer
		.append("name_index = ")
		.append(c.getNameIndex());
	}

	@Override
	public void visitConstantDouble(ConstantDouble c) {
		buffer
		.append("bytes = ")
		.append(c.getBytes());
	}

	@Override
	public void visitConstantFieldref(ConstantFieldref c) {
		buffer
		.append("class_index = ")
		.append(c.getClassIndex())
		.append(", ")
		.append("name_and_type_index = ")
		.append(c.getNameAndTypeIndex());			
	}

	@Override
	public void visitConstantFloat(ConstantFloat c) {
		buffer
		.append("bytes = ")
		.append(c.getBytes());			
	}

	@Override
	public void visitConstantInteger(ConstantInteger c) {
		buffer
		.append("bytes = ")
		.append(c.getBytes());
	}

	@Override
	public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref c) {
		buffer
		.append("class_index = ")
		.append(c.getClassIndex())
		.append(", ")
		.append("name_and_type_index = ")
		.append(c.getNameAndTypeIndex());			
	}

	@Override
	public void visitConstantLong(ConstantLong c) {
		buffer
		.append("bytes = ")
		.append(c.getBytes());			
	}

	@Override
	public void visitConstantMethodref(ConstantMethodref c) {
		buffer
		.append("class_index = ")
		.append(c.getClassIndex())
		.append(", ")
		.append("name_and_type_index = ")
		.append(c.getNameAndTypeIndex());			
	}

	@Override
	public void visitConstantNameAndType(ConstantNameAndType c) {
		buffer
		.append("name_index = ")
		.append(c.getNameIndex())
		.append(", ")
		.append("descriptor_index = ")
		.append(c.getSignatureIndex());
	}

	@Override
	public void visitConstantString(ConstantString c) {
		buffer
		.append("string_index = ")
		.append(c.getStringIndex());
	}

	@Override
	public void visitConstantUtf8(ConstantUtf8 c) {
		buffer
		.append(Utils.getEscapedString(c.getBytes(), "\""));			
	}	
	
}
