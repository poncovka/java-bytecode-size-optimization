package jbyco.analyze.variables;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.StoreInstruction;
import org.apache.bcel.util.ByteSequence;

import jbyco.analyze.Analyzer;
import jbyco.io.BytecodeFilesIterator;
import jbyco.io.CommonFile;

public class VariablesAnalyzer implements Analyzer {

	// map
	VariablesMap map;
	
	public VariablesAnalyzer() {
		this.map = new VariablesMap();
	}
	
	public void processFile(CommonFile file) {
		
		try {		
			// get input stream
			String filename = file.getName();
			InputStream stream = file.getInputStream();
			
			// parse class file
			ClassParser parser = new ClassParser(stream, filename);
			JavaClass klass = parser.parse();
			
			// process code
			processMethods(klass.getMethods());
						
			// close stream
			stream.close();
			
		} catch (ClassFormatException e) {
			e.printStackTrace();
		} catch (ClassGenException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void processMethods(Method[] methods) throws IOException {
		
		// for all methods
		for(Method m : methods) {
			
			// get code
			Code code = m.getCode();
			if (code != null) {
							
				// get number of parameters
				int nparams = m.getArgumentTypes().length + (m.isStatic() ? 0 : 1);
					
				// get number of local variables
				int nvars = code.getMaxLocals();
					
				// add method
				map.addMethod(nparams, nvars);
					
				// read bytecode
				ByteSequence seq = new ByteSequence(code.getCode());
				while (seq.available() > 0) {

					// get an instruction
					Instruction i = Instruction.readInstruction(seq);
						
					// process instruction
					processInstruction(i);
				}
			}	
		}
	}
	
	protected void processInstruction(Instruction i) {
		
		if (i instanceof LocalVariableInstruction) {
			
			int var = ((LocalVariableInstruction) i).getIndex();
			
			if (i instanceof LoadInstruction) {
				map.add(var, "LOAD");
			}
			else if (i instanceof StoreInstruction) {
				map.add(var, "STORE");
			}
			else {
				map.add(var, "OTHER");
			}
		}
	}
	
	public void writeResults(PrintWriter out) {
		map.write(out);
	}
	
	public static void main(String[] args) throws IOException {
		
		// init analyzer
		Analyzer analyzer = new VariablesAnalyzer();
		
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
