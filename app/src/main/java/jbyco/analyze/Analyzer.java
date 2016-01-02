package jbyco.analyze;

import jbyco.io.file.BytecodeFile;

public interface Analyzer {
	
	public void processFile(BytecodeFile file);
	public void print();

}
