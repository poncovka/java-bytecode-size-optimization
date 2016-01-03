package jbyco.analyze.patterns;

import org.apache.bcel.generic.Instruction;

public interface InstructionsLoader {
	
	public void init();
	public void loadInstruction(Instruction i);
	public void finish();

}
