package jbyco;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
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
import org.apache.bcel.classfile.DescendingVisitor;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.InnerClass;
import org.apache.bcel.classfile.InnerClasses;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Signature;
import org.apache.bcel.classfile.SourceFile;
import org.apache.bcel.classfile.StackMap;
import org.apache.bcel.classfile.StackMapEntry;
import org.apache.bcel.classfile.Synthetic;
import org.apache.bcel.classfile.Unknown;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.classfile.Visitor;

import jbyco.io.file.BytecodeFile;

public class Printer {
	
	PrintStream s;
	
	public void print(BytecodeFile file) {
		
		try {
			
			// prepare output stream
			this.s = System.out;
			
			// get input stream
			String filename = file.getName();
			InputStream stream = file.getInputStream();
			
			// parse class file
			ClassParser parser = new ClassParser(stream, filename);
			JavaClass klass = parser.parse();
			
			printSummary(klass, file.getName());
			
		} catch (ClassFormatException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printSummary(JavaClass klass, String filename) {

		String dict  = "%-15s %-30s\n";
		String list  = "        %-20s\n";
		
		// print header
		
		s.printf(dict, "File:", filename);
		s.printf(dict, "Version:", klass.getMajor() + "." + klass.getMinor());
		s.printf(dict, "Constant pool:", klass.getConstantPool().getLength() + " entries");
		
		s.printf(dict, "Access flags:", Utility.accessToString(klass.getAccessFlags()));
		s.printf(dict, "Class:", klass.getClassName());
		s.printf(dict, "Superclass:", klass.getSuperclassName());
		
		// print interfaces
		
		s.println();
		s.printf("%d interfaces:\n", klass.getInterfaceNames().length);
		
		for (String name : klass.getInterfaceNames()) {
			s.printf(list, name);
		}
		
		// print fields
		
		s.println();
		s.printf("%d fields:\n", klass.getFields().length);
		
		for (Field field : klass.getFields()) {
			s.printf(list, getFieldSummary(field));
		}
		
		// print methods
		
		s.println();
		s.printf("%d methods:\n", klass.getMethods().length);
		
		for (Method method : klass.getMethods()) {
			s.printf(list, method);
		}
		
		// print attributes
		
		s.println();
		s.printf("%d attributes:\n", klass.getAttributes().length);

		for (Attribute attr : klass.getAttributes()) {
			s.printf(list, attr);
		}
	}
	
	public String getFieldSummary(Field field) {
		
		StringBuffer buffer = new StringBuffer();
		
		// access flags
		if (field.getAccessFlags() > 0) {
			buffer.append(Utility.accessToString(field.getAccessFlags()));
			buffer.append(" ");
		}
		
		// field type and name
		buffer.append(field);
		
		// append attributes
		if (field.getAttributes().length > 0) {
			
			boolean first = true;
			
			// start parentheses
			buffer.append(" (");
			
			for (Attribute a:field.getAttributes()) {
				
				// append comma
				if (first) {
					first = false;
				}
				else {
					buffer.append(", ");
				}
				
				// append attribute
				buffer.append(a);
			}
			
			// end parentheses
			buffer.append(")");
		}
		
		return buffer.toString();
	}

}
