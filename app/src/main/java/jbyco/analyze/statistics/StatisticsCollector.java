package jbyco.analyze.statistics;

import java.io.IOException;
import java.io.InputStream;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.util.ByteSequence;

import jbyco.analyze.Analyzer;
import jbyco.io.BytecodeFiles;
import jbyco.io.file.BytecodeFile;

public class StatisticsCollector implements Analyzer {

	// bytecode file to print
	BytecodeFile file;
	
	// structure of class file
	JavaClass klass;
	
	// collected data
	StatisticsMap map;
	
	public StatisticsCollector() {
		this.map = new StatisticsMap();
	}
	
	@Override
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
		
		map.add("FILES", 1);
		map.add("METHODS", klass.getMethods().length);
		map.add("FIELDS", klass.getFields().length);
		map.add("BYTES", klass.getBytes().length);
		map.add("CONSTANTS", klass.getConstantPool().getLength());

		processAttributes(klass);
		processInstructions(klass);

	}
	
	protected void processAttributes(JavaClass klass) {
		
		int total = klass.getAttributes().length;
		
		for (Field f : klass.getFields()) {
			total += f.getAttributes().length;
		}
		
		for (Method m : klass.getMethods()) {
			total += m.getAttributes().length;
			
			Code code = m.getCode();
			if (code != null) {
				total += code.getAttributes().length;
			}
		}
		
		map.add("ATTRIBUTES", total);
	}
	
	protected void processInstructions(JavaClass klass) {
		
		int total = 0;
		
		for (Method m : klass.getMethods()) {
			
			Code code = m.getCode();
			if (code != null) {
				
				try {		
					
					// get byte sequence
					ByteSequence seq = new ByteSequence(code.getCode());
					
					// read instructions
					while(seq.available() > 0) {
						
						// get instruction
						Instruction i = Instruction.readInstruction(seq);
						
						// update total
						total++;
					}
					
				} catch (ClassGenException e) {
					e.printStackTrace();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		map.add("INSTRUCTIONS", total);
	}

	@Override
	public void print() {
		map.print();
	}
	
	public static void main(String[] args) {
		
		// init analyzer
		Analyzer analyzer = new StatisticsCollector();
		
		// process files
		for (String path : args) {
			
			BytecodeFiles files = new BytecodeFiles(path);
			
			for (BytecodeFile file : files) {
				analyzer.processFile(file);
			}
		}
		
		// print results
		analyzer.print();
	}

}
