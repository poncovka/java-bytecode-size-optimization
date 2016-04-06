package jbyco.analyze;

import java.io.PrintWriter;

import jbyco.io.FileAbstraction;

public interface Analyzer {
	
	public void processFile(FileAbstraction file);
	public void writeResults(PrintWriter out);

}
