package jbyco.optimize.patterns;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.tree.AbstractInsnNode;

public class InstructionRecognitionBuilder implements PatternsRecognitionBuilder<Integer, AbstractInsnNode>{

	int symbolEpsilon;
	DFA<Integer> dfa;
	Map<State, Pattern<AbstractInsnNode>> patterns;
	
	public InstructionRecognitionBuilder() {
		this.dfa = new DFA<>();
		this.patterns = new HashMap<>();
	}
	
	@Override
	public void addPattern(Pattern<AbstractInsnNode> pattern) {
		String s = pattern.getPattern();
		
		for (String item : s.split(" ")) {
			
			switch(item) {
			
				case "|":	break;
				case "(":	break;
				case ")":	break;
				default:	
			
			
			}
			
		}
	}

	@Override
	public PatternsRecognitionBuilder<Integer, AbstractInsnNode> build() {
		return null;
	}
	

}
