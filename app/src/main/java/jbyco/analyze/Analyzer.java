package jbyco.analyze;

import java.io.PrintWriter;

import jbyco.io.CommonFile;

public interface Analyzer {
	
	public void processFile(CommonFile file);
	public void writeResults(PrintWriter out);

}
