package jbyco.analyze;

import jbyco.io.file.BytecodeFile;

public interface Analyzer {
	
	public void processFile(BytecodeFile file);
	default public void finish() {};
	public void print();

}
