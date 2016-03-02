package jbyco.analyze.patterns.instr;

import java.util.Collection;
import java.util.LinkedList;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class Abstractor extends MethodVisitor {

	// abstraction of the given instruction node
	Collection<Instruction> list;
	
	// operation factory
	OperationFactory operations;
	
	// parameter factory
	ParameterFactory parameters;
		
	public Abstractor(OperationFactory operations, ParameterFactory parameters) {
		super(Opcodes.ASM5);
		
		this.operations = operations;
		this.parameters = parameters;
		
		this.list = new LinkedList<>();
	}
	
	public Collection<Instruction> getList() {
		return list;
	}
	
	public void add(Operation operation, Object[] params) {
		
		// create instruction
		Instruction instruction = new Instruction(operation, params);
		
		// add to the list
		list.add(instruction);
	}

	@Override
	public void visitInsn(int opcode) {
		
		// get operation
		Operation operation = operations.getOperation(opcode);
		
		// get value
		Object value = null;
		
		switch(opcode) {
			case Opcodes.ACONST_NULL: 	value = parameters.getNull(); break;
			case Opcodes.ICONST_M1:		value = parameters.getInt(-1); break;
			case Opcodes.ICONST_0:		value = parameters.getInt(0); break;
			case Opcodes.ICONST_1:		value = parameters.getInt(1); break;
			case Opcodes.ICONST_2:		value = parameters.getInt(2); break;
			case Opcodes.ICONST_3:		value = parameters.getInt(3); break;
			case Opcodes.ICONST_4:		value = parameters.getInt(4); break;
			case Opcodes.ICONST_5:		value = parameters.getInt(5); break;
			case Opcodes.LCONST_0:		value = parameters.getLong(0); break;
			case Opcodes.LCONST_1:		value = parameters.getLong(1); break;
			case Opcodes.FCONST_0:		value = parameters.getFloat(0); break;
			case Opcodes.FCONST_1:		value = parameters.getFloat(1); break;
			case Opcodes.FCONST_2:		value = parameters.getFloat(2); break;
			case Opcodes.DCONST_0:		value = parameters.getDouble(0); break;
			case Opcodes.DCONST_1:		value = parameters.getDouble(1); break;
		}
		
		// prepare parameters
		Object[] params = (value == null) ? null : new Object[] {value};
		
		// add instruction
		add(operation, params);
	}

	@Override
	public void visitIntInsn(int opcode, int operand) {
		
		// get operation
		Operation operation = operations.getOperation(opcode);
				
		// get value
		Object param = null;

		// NEWARRAY
		if (opcode == Opcodes.NEWARRAY) {
			
			String type;
			
			switch(operand) {
				case Opcodes.T_BOOLEAN: type = "x"; break;
				case Opcodes.T_BYTE: 	type = "x"; break;
				case Opcodes.T_CHAR: 	type = "x"; break;
				case Opcodes.T_DOUBLE: 	type = "x"; break;
				case Opcodes.T_FLOAT: 	type = "x"; break;
				case Opcodes.T_INT: 	type = "x"; break;
				case Opcodes.T_LONG: 	type = "x"; break;
				case Opcodes.T_SHORT: 	type = "x"; break;
				default:				type = "x"; // TODO exception
			}
			
			param = parameters.getClass(type);
		}
		// SIPUSH, BIPUSH
		else {
			param = parameters.getInt(operand);
		}
				
		// add instruction
		add(operation, new Object[] {param});
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		
		// get operation
		Operation operation = operations.getOperation(opcode);
		
		// get parameters
		Object param = parameters.getVariable(var);
				
		// add instruction
		add(operation, new Object[] {param});
	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
		
		// get operation
		Operation operation = operations.getOperation(opcode);
		
		// get parameters
		Object param = parameters.getClass(type);
				
		// add instruction
		add(operation, new Object[] {param});
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		
		// get operation
		Operation operation = operations.getOperation(opcode);
		
		// get parameters
		Object param1 = parameters.getClass(owner);
		Object param2 = parameters.getField(name, desc);
				
		// add instruction
		add(operation, new Object[] {param1, param2});
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		
		// get operation
		Operation operation = operations.getOperation(opcode);
		
		// get parameters
		Object param1 = parameters.getClass(owner);
		Object param2 = parameters.getMethod(name, desc);
								
		// add instruction
		add(operation, new Object[] {param1, param2});
	}

	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
		
		// get operation
		Operation operation = operations.getOperation(Opcodes.INVOKEDYNAMIC);

		// get parameters
		Collection<Object> params = new LinkedList<>();
				
		params.add(parameters.getMethod(name, desc));
		params.add(parameters.getHandle(bsm));
		
		for (Object arg : bsmArgs) {
			// TODO
		}
						
		// add instruction
		add(operation, params.toArray());
	}

	@Override
	public void visitJumpInsn(int opcode, Label label) {
		
		// get operation
		Operation operation = operations.getOperation(opcode);
		
		// get parameter 
		Object param = parameters.getLabel(label);
				
		// add instruction
		add(operation, new Object[] {param});
	}

	@Override
	public void visitLabel(Label label) {
		
		// get operation
		Operation operation = operations.getNone();
		
		// get parameter 
		Object param = parameters.getLabel(label);
				
		// add instruction
		add(operation, new Object[] {param});
	}

	@Override
	public void visitLdcInsn(Object cst) {
		
		// get operation
		Operation operation = operations.getOperation(Opcodes.LDC);
		
		// get parameters
		Collection<Object> params = new LinkedList<>();
		
		if (cst instanceof Integer) {
			params.add(parameters.getInt((Integer)cst)); 

		} else if (cst instanceof Float) {
			params.add(parameters.getFloat((Float)cst));

		} else if (cst instanceof Long) {
			params.add(parameters.getLong((Long)cst));

		} else if (cst instanceof Double) {
			params.add(parameters.getDouble((Double)cst));

		} else if (cst instanceof String) {
			params.add(parameters.getString((String)cst));

		} else if (cst instanceof Type) {

			Type type = (Type) cst;
			int sort = type.getSort();

			if (sort == Type.OBJECT) {
				params.add(parameters.getClass(type.getInternalName()));
				
			} else if (sort == Type.ARRAY) {
				params.add(parameters.getClass(type.getInternalName()));
				
			} else if (sort == Type.METHOD) {
				params.add(parameters.getClass(type.getInternalName()));
				params.add(parameters.getMethod("XXX", type.getDescriptor()));
				
			} else {
				// TODO throw an exception
			}
		} else if (cst instanceof Handle) {
			params.add(parameters.getHandle((Handle)cst));

		} else {
			// TODO throw an exception
		}
						
		// add instruction
		add(operation, params.toArray());
	}

	@Override
	public void visitIincInsn(int var, int increment) {
		
		// get operation
		Operation operation = operations.getOperation(Opcodes.IINC);
		
		// get parameter 
		Object param = parameters.getInt(increment);
				
		// add instruction
		add(operation, new Object[] {param});
	}

	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
		
		// get operation
		Operation operation = operations.getOperation(Opcodes.TABLESWITCH);
		
		// get parameters
		Collection<Object> params = new LinkedList<>();
		
		params.add(parameters.getInt(min));
		params.add(parameters.getInt(max));
		params.add(parameters.getLabel(dflt));
		
		for (Label label : labels) {
			params.add(parameters.getLabel(label));
		}
				
		// add instruction
		add(operation, params.toArray());
	}

	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
		
		// get operation
		Operation operation = operations.getOperation(Opcodes.LOOKUPSWITCH);
		
		// get parameters
		Collection<Object> params = new LinkedList<>();
		
		params.add(parameters.getLabel(dflt));
		
		for (int key : keys) {
			params.add(parameters.getInt(key));
		}
		
		for (Label label : labels) {
			params.add(parameters.getLabel(label));
		}
				
		// add instruction
		add(operation, params.toArray());
	}

	@Override
	public void visitMultiANewArrayInsn(String desc, int dims) {
		
		// get operation
		Operation operation = operations.getOperation(Opcodes.MULTIANEWARRAY);
		
		// get parameter 
		Object param1 = parameters.getClass(desc);
		Object param2 = parameters.getInt(dims);
				
		// add instruction
		add(operation, new Object[] {param1, param2});
	}

}
