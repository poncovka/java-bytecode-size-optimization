package jbyco.analyze.patterns.instr;

import java.util.Collection;
import java.util.LinkedList;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Abstractor extends MethodVisitor {

	// abstraction of the given instruction node
	Collection<Instruction> list;
	
	public Abstractor() {
		super(Opcodes.ASM5);
		list = new LinkedList<>();
	}
	
	public Collection<Instruction> getList() {
		return list;
	}
	
	public void clear() {
		list.clear();
	}

	// TODO create abstractions of instructions
}
