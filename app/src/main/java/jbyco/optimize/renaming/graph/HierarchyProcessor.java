package jbyco.optimize.renaming.graph;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class HierarchyProcessor extends ClassVisitor {

	Graph graph;
	
	public HierarchyProcessor(Graph graph) {
		super(Opcodes.ASM5);
		this.graph = graph;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		// TODO
	}

}
