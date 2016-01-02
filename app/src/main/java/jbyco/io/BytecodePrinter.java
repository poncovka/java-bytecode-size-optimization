package jbyco.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
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
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;

import jbyco.io.file.BytecodeFile;

public class BytecodePrinter {
	
	// output stream
	PrintStream out;
	
	// bytecode file to print
	BytecodeFile file;
	
	// structure of class file
	JavaClass klass;
	
	// constant visitor
	ConstantVisitor visitor;
	
	public BytecodePrinter() {
		
		this.out = System.out;
		this.file = null;
		this.klass = null;
		
		this.visitor = new ConstantVisitor();
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
		
		out.println();
		out.printf("%d interfaces:\n", klass.getInterfaceNames().length);
		
		for (String name : klass.getInterfaceNames()) {
			printList(name);
		}
		
		// print fields summary
		
		out.println();
		out.printf("%d fields:\n", klass.getFields().length);
		
		for (Field field : klass.getFields()) {
			printList(field);
		}
		
		// print methods summary
		
		out.println();
		out.printf("%d methods:\n", klass.getMethods().length);
		
		for (Method method : klass.getMethods()) {
			printList(method);
		}
		
		// print attributes summary
		
		out.println();
		out.printf("%d attributes:\n", klass.getAttributes().length);

		for (Attribute attr : klass.getAttributes()) {
			printList(attr);
		}
		
		out.println();
	}
	
	public void print(ConstantPool pool) {
		
		out.println("Constant pool:\n");
		
		for(int i = 1; i < pool.getLength(); i++) {
			
			// get constant
			Constant c = pool.getConstant(i);
			
			// get info
			String info = visitor.getInfo(c);
			
			// print constant
			printConstant(i, c.getTag(), info);
		}
		
		out.println();
	}
	
	public void print(Method[] methods) {
		
		out.println("Methods:\n");
		
		for(int i = 1; i < methods.length; i++) {
			
			// get method
			Method method = methods[i];
			
			// print method
			printMethod(i, method.toString(), method.getCode().toString());			
		}	
		
		out.println();
	}
	
	public void printConstant(int index, int tag, String info) {
		out.printf("%-5s %-20s (%s)\n", "[" + index + "]", Constants.CONSTANT_NAMES[tag], info);
	}
	
	public void printMethod(int index, String method, String code) {
		out.printf("%-5s %s\n\n%s\n", "[" + index + "]", method, code);
	}
	
	public void printDict(String key, String item) {
		out.printf("%-15s %s\n", key, item);
	}
	
	public void printList(Object item) {
		out.printf("      %s\n", item);
	}

	private class ConstantVisitor extends EmptyVisitor {

		StringBuffer buffer = new StringBuffer();
		
		public String getInfo(Constant c) {
			
			// init buffer
			buffer.setLength(0);
			
			// get info
			c.accept(this);
			
			// return string
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
			.append('"')
			.append(Utility.replace(c.getBytes(), "\n", "\\n"))
			.append('"');			
		}	
	}
}
