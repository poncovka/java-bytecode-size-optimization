package jbyco.analyze.patterns.instr;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ActiveLabelsFinder extends MethodVisitor {

	Set<Label> active = new HashSet<>();
	
	public ActiveLabelsFinder() {
		super(Opcodes.ASM5);
	}
	
	public boolean isActive(Label label) {	
		return active.contains(label);
	}
	
	protected void add(Label label) {
		active.add(label);
	}

	@Override
	public void visitJumpInsn(int opcode, Label label) {
		add(label);
	}

	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
		
		// add the default label
		add(dflt);
		
		// add the table of labels
		for (Label label : labels) {
			add(label);
		}
	}

	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
		
		// add the default label
		add(dflt);
		
		// add the table of labels
		for (Label label : labels) {
			add(label);
		}
	}

	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		add(handler);
	}
	
}
