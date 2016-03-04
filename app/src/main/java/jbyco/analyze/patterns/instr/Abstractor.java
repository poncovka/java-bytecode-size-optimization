package jbyco.analyze.patterns.instr;

import java.util.Collection;
import java.util.LinkedList;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import jbyco.analyze.patterns.instr.label.AbstractLabelFactory;
import jbyco.analyze.patterns.instr.operation.AbstractOperation;
import jbyco.analyze.patterns.instr.operation.AbstractOperationFactory;
import jbyco.analyze.patterns.instr.param.AbstractParameter;
import jbyco.analyze.patterns.instr.param.AbstractParameterFactory;

public class Abstractor extends MethodVisitor {

	// abstraction of the given instruction node
	Collection<AbstractInstruction> list;
	
	// operation factory
	AbstractOperationFactory operations;
	
	// parameter factory
	AbstractParameterFactory parameters;
	
	// label factory
	AbstractLabelFactory labels;
		
	public Abstractor(AbstractOperationFactory operations, AbstractParameterFactory parameters, AbstractLabelFactory labels) {
		super(Opcodes.ASM5);
		
		this.operations = operations;
		this.parameters = parameters;
		this.labels = labels;
		
		this.list = new LinkedList<>();
	}
	
	public Collection<AbstractInstruction> getList() {
		return list;
	}
	
	public void add(AbstractOperation operation, Collection<AbstractParameter> params) {
		add(new Instruction(operation, (AbstractParameter[]) params.toArray()));
	}
	
	public void add(AbstractOperation operation, AbstractParameter ...params) {
		add(new Instruction(operation, params));
	}
	
	public void add(AbstractInstruction instruction) {
		list.add(instruction);
	}

	@Override
	public void visitInsn(int opcode) {
		
		// get operation
		AbstractOperation operation = operations.getOperation(opcode);
		
		// get value
		AbstractParameter value = null;
		
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
		
		// add an instruction with null or array of parameters
		add(operation, value);
	}

	@Override
	public void visitIntInsn(int opcode, int operand) {
		
		// get operation
		AbstractOperation operation = operations.getOperation(opcode);
				
		// get value
		AbstractParameter param = null;

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
		add(operation, param);
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		
		// add instruction
		add(operations.getOperation(opcode), parameters.getVariable(var));
	}

	@Override
	public void visitTypeInsn(int opcode, String type) {

		// add instruction
		add(operations.getOperation(opcode), parameters.getClass(type));
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		
		// get parameters
		AbstractParameter p1 = parameters.getClass(owner);
		AbstractParameter p2 = parameters.getField(name, desc);
				
		// add instruction
		add(operations.getOperation(opcode), p1, p2);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		
		// get parameters
		AbstractParameter param1 = parameters.getClass(owner);
		AbstractParameter param2 = parameters.getMethod(name, desc);
								
		// add instruction
		add(operations.getOperation(opcode), param1, param2);
	}

	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {

		// get parameters
		Collection<AbstractParameter> params = new LinkedList<>();
				
		params.add(parameters.getMethod(name, desc));
		params.add(parameters.getHandle(bsm));
		
		for (Object arg : bsmArgs) {
			// TODO
		}
						
		// add instruction
		add(operations.getOperation(Opcodes.INVOKEDYNAMIC), params);
	}

	@Override
	public void visitJumpInsn(int opcode, Label label) {
				
		// add instruction
		add(operations.getOperation(opcode), labels.getLabel(label));
	}

	@Override
	public void visitLabel(Label label) {
		
		// add instruction
		add(labels.getLabel(label));
	}

	@Override
	public void visitLdcInsn(Object cst) {
		
		// get parameters
		Collection<AbstractParameter> params = new LinkedList<>();
		
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
		add(operations.getOperation(Opcodes.LDC), params);
	}

	@Override
	public void visitIincInsn(int var, int increment) {
		
		// add instruction
		add(operations.getOperation(Opcodes.IINC), parameters.getInt(increment));
	}

	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... lbs) {
		
		// get parameters
		Collection<AbstractParameter> params = new LinkedList<>();
		
		params.add(parameters.getInt(min));
		params.add(parameters.getInt(max));
		params.add(labels.getLabel(dflt));
		
		for (Label label : lbs) {
			params.add(labels.getLabel(label));
		}
				
		// add instruction
		add(operations.getOperation(Opcodes.TABLESWITCH), params);
	}

	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] lbs) {
		
		// get parameters
		Collection<AbstractParameter> params = new LinkedList<>();
		
		params.add(labels.getLabel(dflt));
		
		for (int key : keys) {
			params.add(parameters.getInt(key));
		}
		
		for (Label label : lbs) {
			params.add(labels.getLabel(label));
		}
				
		// add instruction
		add(operations.getOperation(Opcodes.LOOKUPSWITCH), params);
	}

	@Override
	public void visitMultiANewArrayInsn(String desc, int dims) {
		
		// get parameter 
		AbstractParameter param1 = parameters.getClass(desc);
		AbstractParameter param2 = parameters.getInt(dims);
				
		// add instruction
		add(operations.getOperation(Opcodes.MULTIANEWARRAY), param1, param2);
	}

}
