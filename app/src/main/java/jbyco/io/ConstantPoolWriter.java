package jbyco.io;

import java.io.PrintWriter;

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

/**
 * A class for printing the content of the constant pool.
 */
public class ConstantPoolWriter extends EmptyVisitor {

	/** The output. */
	PrintWriter out;
	
	/** The buffer. */
	StringBuffer buffer = new StringBuffer();
	
	/**
	 * Instantiates a new constant pool writer.
	 *
	 * @param out the out
	 */
	public ConstantPoolWriter(PrintWriter out) {
		this.out = out;
	}
	
	/**
	 * Write.
	 *
	 * @param klass the BCEL class
	 */
	public void write(JavaClass klass) {
		
		ConstantPool cp = klass.getConstantPool();
		Constant[] pool = cp.getConstantPool();
		
		// print header
		String klassname = getClassName(cp, klass.getClassNameIndex());
		out.println("// constant pool of " + klassname);
		out.println("[");

		// print constants
		for (int i = 1; i < pool.length; i++) {
			
			// get constant
			Constant c = pool[i];
			String name = getConstantName(c);
			String items = getConstantItems(c);
			
			// print constant
			out.printf("   %-5s %-20s (%s)\n", i + ":", name, items);	
		}
		
		// print end
		out.println("]\n");
	}
	
	/**
	 * Gets the class name.
	 *
	 * @param cp the BCEL constant pool
	 * @param index the index of the item
	 * @return the class name
	 */
	private String getClassName(ConstantPool cp, int index) {

		ConstantClass c1 = (ConstantClass)cp.getConstant(index);
		ConstantUtf8  c2 = (ConstantUtf8)cp.getConstant(c1.getNameIndex());
		return c2.getBytes();
	}
	
	/**
	 * Gets the constant name.
	 *
	 * @param c the BCEL constant
	 * @return the constant name
	 */
	private String getConstantName(Constant c) {
		return Constants.CONSTANT_NAMES[c.getTag()];
	}
	
	/**
	 * Gets the constant items.
	 *
	 * @param c the BCEL constant
	 * @return the constant items
	 */
	private String getConstantItems(Constant c) {
		buffer.setLength(0);
		c.accept(this);
		return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see org.apache.bcel.classfile.EmptyVisitor#visitConstantClass(org.apache.bcel.classfile.ConstantClass)
	 */
	@Override
	public void visitConstantClass(ConstantClass c) {
		buffer
		.append("name_index = ")
		.append(c.getNameIndex());
	}

	/* (non-Javadoc)
	 * @see org.apache.bcel.classfile.EmptyVisitor#visitConstantDouble(org.apache.bcel.classfile.ConstantDouble)
	 */
	@Override
	public void visitConstantDouble(ConstantDouble c) {
		buffer
		.append("bytes = ")
		.append(c.getBytes());
	}

	/* (non-Javadoc)
	 * @see org.apache.bcel.classfile.EmptyVisitor#visitConstantFieldref(org.apache.bcel.classfile.ConstantFieldref)
	 */
	@Override
	public void visitConstantFieldref(ConstantFieldref c) {
		buffer
		.append("class_index = ")
		.append(c.getClassIndex())
		.append(", ")
		.append("name_and_type_index = ")
		.append(c.getNameAndTypeIndex());			
	}

	/* (non-Javadoc)
	 * @see org.apache.bcel.classfile.EmptyVisitor#visitConstantFloat(org.apache.bcel.classfile.ConstantFloat)
	 */
	@Override
	public void visitConstantFloat(ConstantFloat c) {
		buffer
		.append("bytes = ")
		.append(c.getBytes());			
	}

	/* (non-Javadoc)
	 * @see org.apache.bcel.classfile.EmptyVisitor#visitConstantInteger(org.apache.bcel.classfile.ConstantInteger)
	 */
	@Override
	public void visitConstantInteger(ConstantInteger c) {
		buffer
		.append("bytes = ")
		.append(c.getBytes());
	}

	/* (non-Javadoc)
	 * @see org.apache.bcel.classfile.EmptyVisitor#visitConstantInterfaceMethodref(org.apache.bcel.classfile.ConstantInterfaceMethodref)
	 */
	@Override
	public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref c) {
		buffer
		.append("class_index = ")
		.append(c.getClassIndex())
		.append(", ")
		.append("name_and_type_index = ")
		.append(c.getNameAndTypeIndex());			
	}

	/* (non-Javadoc)
	 * @see org.apache.bcel.classfile.EmptyVisitor#visitConstantLong(org.apache.bcel.classfile.ConstantLong)
	 */
	@Override
	public void visitConstantLong(ConstantLong c) {
		buffer
		.append("bytes = ")
		.append(c.getBytes());			
	}

	/* (non-Javadoc)
	 * @see org.apache.bcel.classfile.EmptyVisitor#visitConstantMethodref(org.apache.bcel.classfile.ConstantMethodref)
	 */
	@Override
	public void visitConstantMethodref(ConstantMethodref c) {
		buffer
		.append("class_index = ")
		.append(c.getClassIndex())
		.append(", ")
		.append("name_and_type_index = ")
		.append(c.getNameAndTypeIndex());			
	}

	/* (non-Javadoc)
	 * @see org.apache.bcel.classfile.EmptyVisitor#visitConstantNameAndType(org.apache.bcel.classfile.ConstantNameAndType)
	 */
	@Override
	public void visitConstantNameAndType(ConstantNameAndType c) {
		buffer
		.append("name_index = ")
		.append(c.getNameIndex())
		.append(", ")
		.append("descriptor_index = ")
		.append(c.getSignatureIndex());
	}

	/* (non-Javadoc)
	 * @see org.apache.bcel.classfile.EmptyVisitor#visitConstantString(org.apache.bcel.classfile.ConstantString)
	 */
	@Override
	public void visitConstantString(ConstantString c) {
		buffer
		.append("string_index = ")
		.append(c.getStringIndex());
	}

	/* (non-Javadoc)
	 * @see org.apache.bcel.classfile.EmptyVisitor#visitConstantUtf8(org.apache.bcel.classfile.ConstantUtf8)
	 */
	@Override
	public void visitConstantUtf8(ConstantUtf8 c) {
		buffer
		.append(Utils.getEscapedString(c.getBytes(), "\""));			
	}	
	
}
