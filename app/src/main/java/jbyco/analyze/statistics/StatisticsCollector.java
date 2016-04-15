package jbyco.analyze.statistics;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.util.ByteSequence;
import org.apache.bcel.generic.Instruction;

import jbyco.analyze.Analyzer;
import jbyco.io.BytecodeFilesIterator;
import jbyco.io.CommonFile;

public class StatisticsCollector implements Analyzer {
	
	// collected data
	StatisticsMap map;
	
	public StatisticsCollector() {
		this.map = new StatisticsMap();
	}
	
	@Override
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

			// close stream
			stream.close();
						
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
						Instruction.readInstruction(seq);
						total++;
					}
					
					seq.close();
					
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
	public void writeResults(PrintWriter out) {
		map.write(out);
	}
	
	public static void main(String[] args) throws IOException {
		
		// init analyzer
		Analyzer analyzer = new StatisticsCollector();
		
		// process files
		for (String str : args) {
			
			// get path
			Path path = Paths.get(str);
			
			// process files on the path
			for (CommonFile file : (new BytecodeFilesIterator(path))) {
				analyzer.processFile(file);
			}
		}
		
		// print results
		PrintWriter writer = new PrintWriter(System.out, true);
		analyzer.writeResults(writer);
		writer.close();
	}

}
