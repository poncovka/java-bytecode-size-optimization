package jbyco.analyze;

import java.io.PrintWriter;

import jbyco.io.files.BytecodeFile;

public interface Analyzer {
	
	public void processFile(BytecodeFile file);
	public void writeResults(PrintWriter out);

}
