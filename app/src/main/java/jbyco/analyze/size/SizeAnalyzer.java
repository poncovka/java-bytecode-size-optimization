package jbyco.analyze.size;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.util.ByteSequence;

import jbyco.analyze.Analyzer;
import jbyco.io.BytecodeFilesIterator;
import jbyco.io.CommonFile;
import jbyco.io.TemporaryFiles;

public class SizeAnalyzer implements Analyzer {
	
	// dictionary of sizes
	SizeMap map;
	
	public SizeAnalyzer() {
		map = new SizeMap();
	}
	
	public void processFile(CommonFile file) {
		
		try {		
			// get input stream
			String filename = file.getName();
			InputStream stream = file.getInputStream();
			
			// parse class file
			ClassParser parser = new ClassParser(stream, filename);
			JavaClass klass = parser.parse();
						
			// process file
			processClassFile(klass);
			
			// process strings in a file
			processStrings(klass);
			
			// close stream
			stream.close();
						
		} catch (ClassFormatException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
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
	
	protected void processClassFile(JavaClass klass) {
		
		processConstantPool(klass.getConstantPool());
		processFields(klass.getFields());
		processMethods(klass.getMethods());
		processAttributes(klass.getAttributes());
		
		// size of a class file
		map.add("FILE", klass.getBytes().length);
		
	}

	protected void processConstantPool(ConstantPool pool) {
		
		for (Constant c : pool.getConstantPool()) {
			
			// skip zero index
			if (c == null) continue;
			
			// get key
			String key = Constants.CONSTANT_NAMES[c.getTag()];
			
			// get size
			int size = getConstantSize(c);
			
			// size of constants in all files
			map.add(key, size);
			
		}
	}
	
	protected void processFields(Field[] fields) {
		
		// process attributes
		for (Field f : fields) {
			processAttributes(f.getAttributes());
		}
		
	}

	protected void processMethods(Method[] methods) {

		for (Method m : methods) {
			
			// process attributes
			processAttributes(m.getAttributes());
			
			Code code = m.getCode();
			if (code != null) {
				
				// process attributes
				processAttributes(code.getAttributes());
				
				// process instructions
				processInstructions(code.getCode());
			}
		}
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
	
	private void processStrings(JavaClass klass) {
		
		// get constant pool
		ConstantPool cp = klass.getConstantPool();
		
		// calculate length of constant pool
		int length = cp.getLength() + 1;
		
		// init bit sets
		BitSet classNames = new BitSet(length);
		BitSet methodNames = new BitSet(length);
		BitSet fieldNames = new BitSet(length);
		BitSet methodSignatures = new BitSet(length);
		BitSet fieldSignatures = new BitSet(length);
		
		// process fields
		for (Field f : klass.getFields()) {
			fieldNames.set(f.getNameIndex());
			fieldSignatures.set(f.getSignatureIndex());
		}
		
		// process methods
		for (Method m : klass.getMethods()) {
			methodNames.set(m.getNameIndex());
			methodSignatures.set(m.getSignatureIndex());
		}
		
		// process constants
		for (Constant c : cp.getConstantPool()) {
			
			//  ConstantFieldref, ConstantInterfaceMethodref, ConstantMethodref
			if(c instanceof ConstantCP) {
								
				Constant c2 = cp.getConstant(((ConstantCP)c).getNameAndTypeIndex());
				
				// ConstantNameAndType
				if (c2 instanceof ConstantNameAndType) {
					
					ConstantNameAndType c3 = (ConstantNameAndType) c2;
					int nameIndex = c3.getNameIndex();
					int sigIndex = c3.getSignatureIndex();
					
					// process field name and signature
					if (c.getTag() == Constants.CONSTANT_Fieldref) {
						fieldNames.set(nameIndex);
						fieldSignatures.set(sigIndex);
					}
					// process method name and signature
					else {
						methodNames.set(nameIndex);
						methodSignatures.set(sigIndex);
					}
				}
			}
			// ConstantClass
			else if (c instanceof ConstantClass) {
				classNames.set(((ConstantClass)c).getNameIndex());
			}
		}
		
		// process indexes
		processStringIndexes(cp, classNames, 		"CLASS_NAME");
		processStringIndexes(cp, methodNames, 		"METHOD_NAME");
		processStringIndexes(cp, fieldNames, 		"FIELD_NAME");
		processStringIndexes(cp, methodSignatures, 	"METHOD_SIGNATURE");
		processStringIndexes(cp, fieldSignatures, 	"FIELD_SIGNATURE");
	}
	
	protected void processStringIndexes(ConstantPool cp, BitSet set, String description) {
		
		for (int i = set.nextSetBit(0); i >= 0; i = set.nextSetBit(i+1)) {
			
			// get size
			int size = getConstantSize(cp.getConstant(i)) - 3;
			
			// add size to map
			map.add("STRING_" + description, size);
			
		     // overflow protection
		     if (i == Integer.MAX_VALUE) {
		         break;
		     }
		 }
	}

	@Override
	public void writeResults(PrintWriter out) {
		this.map.write(out);
	}
	
	public static void main(String[] args) throws IOException {
		
		// init analyzer
		Analyzer analyzer = new SizeAnalyzer();
		
		// create temporary directory
		Path workingDirectory = TemporaryFiles.createDirectory();
				
		try {
			
			// process files
			for (String str : args) {
				
				// get path
				Path path = Paths.get(str);
				
				// process files on the path
				for (CommonFile file : (new BytecodeFilesIterator(path, workingDirectory))) {
					analyzer.processFile(file);
				}
			}
		}
		finally {
			TemporaryFiles.deleteDirectory(workingDirectory);
		}
		
		// print results
		PrintWriter writer = new PrintWriter(System.out, true);
		analyzer.writeResults(writer);
		writer.close();
	}
	
}
