package jbyco.analyze.patterns;

import java.io.IOException;
import java.io.InputStream;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.util.ByteSequence;

import jbyco.analyze.Analyzer;
import jbyco.io.file.BytecodeFile;
import jbyco.pattern.Pattern;
import jbyco.pattern.PatternsFinder;
import jbyco.pattern.graph.SuffixGraph;

public class PatternsAnalyzer implements Analyzer {

	// bytecode file to print
	BytecodeFile file;
	
	// structure of class file
	JavaClass klass;
	
	// suffix graph
	SuffixGraph graph;
	
	// instruction loader
	InstructionsLoader loader;
	
	public PatternsAnalyzer() {
		graph = new SuffixGraph();
		loader = new OpcodesLoader(graph);
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

			// init
			this.file = file;
			
			// process
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
					// init loader
					loader.init();
					
					// read bytecode
					ByteSequence seq = new ByteSequence(code.getCode());
					while (seq.available() > 0) {

						// get an instruction
						Instruction i = Instruction.readInstruction(seq);
						
						// process instruction
						loader.loadInstruction(i);
					}
					
					// finish loading
					loader.finish();
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
	
	@Override
	public void print() {
		
		// print graph
		//System.out.println("Graph:");
		//graph.print(System.out);
		
		System.out.println();
		
		// print header
		String format = "%-15s %s\n";
		System.out.println("Patterns:");
		System.out.printf(format, "COUNT", "PATTERN");
		
		// print patterns
		PatternsFinder finder = new PatternsFinder(graph, "; ");
		
		for(Pattern pattern : finder) {
			System.out.printf(format, pattern.frequency, "\'" + pattern.string +  "\'");
		}	
	}

}
