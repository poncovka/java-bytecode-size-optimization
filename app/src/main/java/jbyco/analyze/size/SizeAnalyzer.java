package jbyco.analyze.size;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.util.ByteSequence;

import jbyco.analyze.Analyzer;
import jbyco.io.file.BytecodeFile;

public class SizeAnalyzer implements Analyzer {

	// bytecode file to print
	BytecodeFile file;
	
	// structure of class file
	JavaClass klass;
	
	// dictionary of sizes
	SizeMap map;
	
	public SizeAnalyzer() {
		map = new SizeMap();
	}
	
	public void processFile(BytecodeFile file) {
		
		try {		
			// get input stream
			String filename = file.getName();
			InputStream stream = file.getInputStream();
			
			// parse class file
			ClassParser parser = new ClassParser(stream, filename);
			this.klass = parser.parse();
			this.file = file;
						
			// process file
			processClassFile(this.klass);
						
		} catch (ClassFormatException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void processClassFile(JavaClass klass) {
		
		processConstantPool(klass.getConstantPool());
		processFields(klass.getFields());
		processMethods(klass.getMethods());
		processAttributes(klass.getAttributes());
		
		// size of a class file
		map.add("FILE", klass.getBytes().length);
		
	}
	
	protected void processConstantPool(ConstantPool pool) {
		
		int total = 0;
		
		for (Constant c : pool.getConstantPool()) {
			
			// skip zero index
			if (c == null) continue;
			
			// get key
			String key = Constants.CONSTANT_NAMES[c.getTag()];
			
			// get size
			int size = getConstantSize(c);
			
			// size of constants in all files
			map.add(key, size);
			
			// add to total
			total += size;
			
		}
		
		// size of constants in a class file
		map.add("FILE_CONSTANTS", 2 + total);
	}
	
	protected void processFields(Field[] fields) {
		
		// process attributes
		for (Field f : fields) {
			processAttributes(f.getAttributes());
		}
		
		// size of fields in a class file - attributes
		map.add("FILE_FIELDS", 8 * fields.length);
	}

	protected void processMethods(Method[] methods) {
		
		// total lenght of code
		int codeLenghs = 0;
		int exceptionTableLengths = 0;

		for (Method m : methods) {
			
			// process attributes
			processAttributes(m.getAttributes());
			
			Code code = m.getCode();
			if (code != null) {
				
				// add code length
				codeLenghs += code.getLength();
				
				// process attributes
				processAttributes(code.getAttributes());
				
				// process exception table
				exceptionTableLengths += code.getExceptionTable().length;
				
				// process instructions
				processInstructions(code.getCode());
			}
		}
		
		// size of methods in a class file - attributes
		map.add("FILE_METHODS", 8 * methods.length);
		
		// size of methods in a class file - attributes
		map.add("FILE_CODES", 6 + codeLenghs);
		
		// size of exception tables in a class file
		map.add("FILE_EXCEPTION_TABLES", 2 + 8 * exceptionTableLengths);
	}

	protected void processInstructions(byte[] instructions) {
		
		try {		
			
			// get byte sequence
			ByteSequence seq = new ByteSequence(instructions);
			
			// read instructions
			while(seq.available() > 0) {
				
				// get instruction
				Instruction i = Instruction.readInstruction(seq);
				
				// size of instructions in all files
				map.add("INSTRUCION_" + i.getName(), i.getLength());
			}
			
		} catch (ClassGenException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void processAttributes(Attribute[] attributes) {
		
		// size of attributes in all files
		for (Attribute a : attributes) {
			map.add("ATTRIBUTE_" + a.getName(), 6 + a.getLength());
		}	
	}
	
	static public int getConstantSize(Constant c) {
	
		int size = 0;
		
		// get basic size
		switch(c.getTag()) {
			case Constants.CONSTANT_Integer: 			
			case Constants.CONSTANT_Float: 				size = 5; break;
			case Constants.CONSTANT_Long: 				
			case Constants.CONSTANT_Double: 			size = 9; break;
			case Constants.CONSTANT_Utf8: 				size = 3; break;
			case Constants.CONSTANT_String: 			size = 3; break;
			case Constants.CONSTANT_NameAndType: 		size = 5; break;
			case Constants.CONSTANT_Class: 				size = 3; break;
			case Constants.CONSTANT_Fieldref: 			
			case Constants.CONSTANT_Methodref: 				
			case Constants.CONSTANT_InterfaceMethodref: size = 5; break;
			case Constants.CONSTANT_MethodHandle: 		size = 4; break;
			case Constants.CONSTANT_MethodType: 		size = 3; break;
			case Constants.CONSTANT_InvokeDynamic: 		size = 5; break;
			default: throw new IllegalArgumentException("Unknown constant tag " + c + ".");
		}
		
		// add variable size
		if (c instanceof ConstantUtf8) {
			try {
				
				ConstantUtf8 c2 = (ConstantUtf8) c;
				size += c2.getBytes().getBytes("UTF-8").length;
				
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException("Unreadable CONSTANT_Utf8 " + c + ".");
			}
		}
		
		return size;
	}

	
	@Override
	public void print() {
		this.map.print();
	}
	
}
