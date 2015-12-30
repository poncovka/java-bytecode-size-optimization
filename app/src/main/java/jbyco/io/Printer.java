package jbyco.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
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
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;

import jbyco.io.file.BytecodeFile;

public class Printer extends EmptyVisitor {
	
	// output stream
	PrintStream s;
	
	// global counter
	int counter;
	
	// bytecode file to print
	BytecodeFile file;
	
	// structure of class file
	JavaClass klass;
	
	public Printer() {
		this.s = System.out;
		this.counter = 0;
		this.file = null;
		this.klass = null;
	}
	
	public void setFile(BytecodeFile file) {
		
		try {		
			// get input stream
			String filename = file.getName();
			InputStream stream = file.getInputStream();
			
			// parse class file
			ClassParser parser = new ClassParser(stream, filename);
			this.klass = parser.parse();

			// set file
			this.file = file;
			
		} catch (ClassFormatException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printSummary() {
		print(klass, file.getName());	
	}
	
	public void printConstantPool() {
		print(klass.getConstantPool());
	}
	
	public void printMethods() {
		print(klass.getMethods());
	}
		
	public void print(JavaClass klass, String filename) {
		
		// print header summary
		
		printDict("File:", filename);
		printDict("Version:", klass.getMajor() + "." + klass.getMinor());
		printDict("Constant pool:", klass.getConstantPool().getLength() - 1 + " entries");
		
		printDict("Access flags:", Utility.accessToString(klass.getAccessFlags()));
		printDict("Class:", klass.getClassName());
		printDict("Superclass:", klass.getSuperclassName());
		
		// print interfaces summary
		
		s.println();
		s.printf("%d interfaces:\n", klass.getInterfaceNames().length);
		
		for (String name : klass.getInterfaceNames()) {
			printList(name);
		}
		
		// print fields summary
		
		s.println();
		s.printf("%d fields:\n", klass.getFields().length);
		
		for (Field field : klass.getFields()) {
			printList(field);
		}
		
		// print methods summary
		
		s.println();
		s.printf("%d methods:\n", klass.getMethods().length);
		
		for (Method method : klass.getMethods()) {
			printList(method);
		}
		
		// print attributes summary
		
		s.println();
		s.printf("%d attributes:\n", klass.getAttributes().length);

		for (Attribute attr : klass.getAttributes()) {
			printList(attr);
		}
		
		s.println();
	}
	
	public void print(ConstantPool pool) {
		
		s.println("Constant pool:\n");
		
		for(counter = 1; counter < pool.getLength(); counter++) {
			pool.getConstant(counter).accept(this);			
		}
		
		s.println();
	}
	
	public void print(Method[] methods) {
		
		s.println("Methods:\n");
		
		for(counter = 1; counter < methods.length; counter++) {
			methods[counter].accept(this);			
		}	
		
		s.println();
	}
	
	public void printConstant(int index, int tag, String info) {
		s.printf("%-5s %-20s (%s)\n", "[" + index + "]", Constants.CONSTANT_NAMES[tag], info);
	}
	
	public void printMethod(int index, String method, String code) {
		s.printf("%-5s %s\n\n%s\n", "[" + index + "]", method, code);
	}
	
	public void printDict(String key, String item) {
		s.printf("%-15s %s\n", key, item);
	}
	
	public void printList(Object item) {
		s.printf("      %s\n", item);
	}

	@Override
	public void visitConstantClass(ConstantClass c) {
		
		String info = 
			new StringBuffer()
			.append("name_index = ")
			.append(c.getNameIndex())
			.toString();
		
		printConstant(counter, c.getTag(), info);
	}

	@Override
	public void visitConstantDouble(ConstantDouble c) {

		String info = 
				new StringBuffer()
				.append("bytes = ")
				.append(c.getBytes())
				.toString();
			
		printConstant(counter, c.getTag(), info);
	}

	@Override
	public void visitConstantFieldref(ConstantFieldref c) {
		
		String info = 
				new StringBuffer()
				.append("class_index = ")
				.append(c.getClassIndex())
				.append(", ")
				.append("name_and_type_index = ")
				.append(c.getNameAndTypeIndex())
				.toString();
			
		printConstant(counter, c.getTag(), info);
	}

	@Override
	public void visitConstantFloat(ConstantFloat c) {
		
		String info = 
				new StringBuffer()
				.append("bytes = ")
				.append(c.getBytes())
				.toString();
			
		printConstant(counter, c.getTag(), info);
	}

	@Override
	public void visitConstantInteger(ConstantInteger c) {
		
		String info = 
				new StringBuffer()
				.append("bytes = ")
				.append(c.getBytes())
				.toString();
			
		printConstant(counter, c.getTag(), info);
	}

	@Override
	public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref c) {
		
		String info = 
				new StringBuffer()
				.append("class_index = ")
				.append(c.getClassIndex())
				.append(", ")
				.append("name_and_type_index = ")
				.append(c.getNameAndTypeIndex())
				.toString();
			
		printConstant(counter, c.getTag(), info);
	}

	@Override
	public void visitConstantLong(ConstantLong c) {
		
		String info = 
				new StringBuffer()
				.append("bytes = ")
				.append(c.getBytes())
				.toString();
			
		printConstant(counter, c.getTag(), info);
	}

	@Override
	public void visitConstantMethodref(ConstantMethodref c) {
		
		String info = 
				new StringBuffer()
				.append("class_index = ")
				.append(c.getClassIndex())
				.append(", ")
				.append("name_and_type_index = ")
				.append(c.getNameAndTypeIndex())
				.toString();
			
		printConstant(counter, c.getTag(), info);
	}

	@Override
	public void visitConstantNameAndType(ConstantNameAndType c) {
		
		String info = 
				new StringBuffer()
				.append("name_index = ")
				.append(c.getNameIndex())
				.append(", ")
				.append("descriptor_index = ")
				.append(c.getSignatureIndex())
				.toString();
			
		printConstant(counter, c.getTag(), info);
	}

	@Override
	public void visitConstantString(ConstantString c) {
		
		String info = 
				new StringBuffer()
				.append("string_index = ")
				.append(c.getStringIndex())
				.toString();
			
		printConstant(counter, c.getTag(), info);
	}

	@Override
	public void visitConstantUtf8(ConstantUtf8 c) {
		
		String info = 
				new StringBuffer()
				.append('"')
				.append(Utility.replace(c.getBytes(), "\n", "\\n"))
				.append('"')
				.toString();
			
		printConstant(counter, c.getTag(), info);
	}

	@Override
	public void visitConstantValue(ConstantValue c) {

		String info = 
				new StringBuffer()
				.append("name_index = ")
				.append(c.getNameIndex())
				.append(", ")
				.append("constant_value_index = ")
				.append(c.getConstantValueIndex())
				.toString();
			
		printConstant(counter, c.getTag(), info);
	}

	@Override
	public void visitMethod(Method method) {
		printMethod(counter, method.toString(), method.getCode().toString());
	}

}
