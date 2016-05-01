package jbyco.optimize;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;

public class Predicates {

	public static boolean isConst(AbstractInsnNode i) {
		int op = i.getOpcode();
		return op <= Opcodes.ACONST_NULL && op >= Opcodes.LDC; 
	} 
	
}
