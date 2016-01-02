package jbyco.analyze.locals;

import java.io.IOException;
import java.io.InputStream;

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
import jbyco.io.file.BytecodeFile;

public class LocalsAnalyzer implements Analyzer {
	
	// bytecode file to print
	BytecodeFile file;
	
	// structure of class file
	JavaClass klass;
	
	// map
	LocalsMap map;
	
	public LocalsAnalyzer() {
		this.map = new LocalsMap();
	}
	
	public void processFile(BytecodeFile file) {
		
		try {		
			// get input stream
			String filename = file.getName();
			InputStream stream = file.getInputStream();
			
			// parse class file
			ClassParser parser = new ClassParser(stream, filename);
			this.klass = parser.parse();

			// init
			this.file = file;
			
			// process code
			processMethods();
						
		} catch (ClassFormatException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void processMethods() {
		
		// for all methods
		for(Method m : klass.getMethods()) {
			
			// get code
			Code code = m.getCode();
			if (code != null) {
				
				try {				
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
				catch (ClassGenException e) {
					System.err.println("Could read instructions from " + file.getName());
					e.printStackTrace();
				}
				catch (IOException e) {
					System.err.println("Could read instructions from " + file.getName());
					e.printStackTrace();
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
	
	public void print() {
		map.print();
	}
}