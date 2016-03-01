package jbyco.analyze.patterns.instr;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;

public class ActiveLabelsFinder extends MethodVisitor {

	Set<LabelNode> active = new HashSet<>();
	
	public ActiveLabelsFinder() {
		super(Opcodes.ASM5);
	}
	
	public boolean isActive(AbstractInsnNode node) {	
		return !(node instanceof LabelNode) || active.contains((LabelNode)node);
	}
	
	// TODO find all active labels

}
