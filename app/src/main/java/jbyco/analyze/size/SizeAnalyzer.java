package jbyco.analyze.size;

import java.io.IOException;
import java.io.InputStream;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.DescendingVisitor;
import org.apache.bcel.classfile.JavaClass;

import jbyco.analyze.Analyzer;
import jbyco.io.file.BytecodeFile;

public class SizeAnalyzer implements Analyzer {

	// bytecode file to print
	BytecodeFile file;
	
	// structure of class file
	JavaClass klass;
	
	// dictionary of sizes
	SizeMap map;
	
	// visitor
	SizeVisitor visitor;
	
	public SizeAnalyzer() {
		map = new SizeMap();
		visitor = new SizeVisitor(map);
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
			this.visitor.init(this.klass.getConstantPool());
						
			// process file
			new DescendingVisitor(this.klass, this.visitor).visit();
						
		} catch (ClassFormatException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void print() {
		this.map.print();
	}
	
}
