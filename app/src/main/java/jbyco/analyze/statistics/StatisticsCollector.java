package jbyco.analyze.statistics;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.util.ByteSequence;

import jbyco.analyze.Analyzer;
import jbyco.io.BytecodeFilesIterator;
import jbyco.io.CommonFile;
import jbyco.io.TemporaryFiles;

/**
 * A tool for getting basic statistics about class files.
 */
public class StatisticsCollector implements Analyzer {
	
	/** The map with collected data. */
	StatisticsMap map;
	
	/**
	 * Instantiates a new statistics collector.
	 */
	public StatisticsCollector() {
		this.map = new StatisticsMap();
	}
	
	/* (non-Javadoc)
	 * @see jbyco.analyze.Analyzer#processClassFile(java.io.InputStream)
	 */
	@Override
	public void processClassFile(InputStream in) throws IOException {
				
		// parse class file
		ClassParser parser = new ClassParser(in, null);
		JavaClass klass = parser.parse();
						
		// process file
		processClassFile(klass);
		
	}

	/**
	 * Process class file.
	 *
	 * @param klass the BCEL class
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void processClassFile(JavaClass klass) throws IOException {
		
		map.add("FILES", 1);
		map.add("METHODS", klass.getMethods().length);
		map.add("FIELDS", klass.getFields().length);
		map.add("BYTES", klass.getBytes().length);
		map.add("CONSTANTS", klass.getConstantPool().getLength());

		processAttributes(klass);
		processInstructions(klass);

	}
	
	/**
	 * Process attributes.
	 *
	 * @param klass the BCEL class
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void processAttributes(JavaClass klass) throws IOException {
		
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
	
	/**
	 * Process instructions.
	 *
	 * @param klass the BCEL class
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void processInstructions(JavaClass klass) throws IOException {
		
		int total = 0;
		
		for (Method m : klass.getMethods()) {
			
			Code code = m.getCode();
			if (code != null) {
					
				// get byte sequence
				ByteSequence seq = new ByteSequence(code.getCode());
					
				// read instructions
				while(seq.available() > 0) {
					Instruction.readInstruction(seq);
					total++;
				}
					
				seq.close();					
			}
		}
		
		map.add("INSTRUCTIONS", total);
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.Analyzer#writeResults(java.io.PrintWriter)
	 */
	@Override
	public void writeResults(PrintWriter out) {
		map.write(out);
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		
		// init analyzer
		Analyzer analyzer = new StatisticsCollector();
		
		// create temporary directory
		Path workingDirectory = TemporaryFiles.createDirectory();
				
		try {
			
			// process files
			for (String str : args) {
				
				// get path
				Path path = Paths.get(str);
				
				// process files on the path
				for (CommonFile file : (new BytecodeFilesIterator(path, workingDirectory))) {
					
					InputStream in = file.getInputStream();
					analyzer.processClassFile(in);
					in.close();
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
