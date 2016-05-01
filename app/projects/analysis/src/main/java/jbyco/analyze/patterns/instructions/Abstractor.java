package jbyco.analyze.patterns.instructions;

import java.util.ArrayList;
import java.util.Collection;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import jbyco.analyze.patterns.labels.AbstractLabelFactory;
import jbyco.analyze.patterns.operations.AbstractOperation;
import jbyco.analyze.patterns.operations.AbstractOperationFactory;
import jbyco.analyze.patterns.parameters.AbstractParameter;
import jbyco.analyze.patterns.parameters.AbstractParameterFactory;

/**
 * The class for abstraction of instructions in a given method.
 * The visited instructions of the method will be abstracted and saved in a collection.
 * The level of abstraction is given by the factories {@link AbstractOperationFactory}, 
 * {@link AbstractParameterFactory} and {@link AbstractLabelFactory}.
 */
public class Abstractor extends MethodVisitor {

	/** The list of abstracted instructions. */
	Collection<AbstractInstruction> list;
	
	/** The operations' factory. */
	AbstractOperationFactory operations;
	
	/** The parameters' factory */
	AbstractParameterFactory parameters;
	
	/** The labels' factory. */
	AbstractLabelFactory labels;
	
	/** The maximal index of method parameters. */
	int maxArgs = 0;
	
	/** Is the current method static. */
	boolean isStatic = false;
		
	/**
	 * Instantiates a new abstractor with method information 
	 * and abstraction factories.
	 *
	 * @param access 		the access flags of the method
	 * @param name 			the name of the method
	 * @param desc 			the descriptor of the method
	 * @param signature 	the signature of the method
	 * @param exceptions 	the exceptions
	 * @param operations 	the operations' factory
	 * @param parameters 	the parameters' factory
	 * @param labels 		the labels' factory
	 */
	public Abstractor(	int access,
   					  	String name,
			            String desc,
			            String signature,
			            String[] exceptions,
			            AbstractOperationFactory operations, 
			            AbstractParameterFactory parameters, 
			            AbstractLabelFactory labels
			          ) {
		
		super(Opcodes.ASM5);
		
		// is the method static?
		this.isStatic = ((access & Opcodes.ACC_STATIC) != 0);
		
		// what is the maximal index of method arguments?
		this.maxArgs = isStatic ? 0 : 1;
		
		for (Type type : Type.getArgumentTypes(desc)) {
			
			// get sort of type
			int sort = type.getSort();
			
			// add number of variables
			maxArgs += (sort == Opcodes.DOUBLE || sort == Opcodes.LONG) ? 2 : 1;
		}
		
		// init
		this.operations = operations;
		this.parameters = parameters;
		this.labels = labels;
		
		// restart
		init();
	}
	
	/**
	 * Initializes the abstractor.
	 */
	public void init() {
		this.list = new ArrayList<>();
	}
	
	/**
	 * Adds the new instruction to the list.
	 *
	 * @param operation 	the operation of the instruction
	 * @param params 		the parameters of the instruction
	 */
	public void add(AbstractOperation operation, AbstractParameter ...params) {
		add(new Instruction(operation, params));
	}
	
	/**
	 *  Adds the new instruction to the list.
	 *
	 * @param operation 	the operation of the instruction
	 * @param params 		the collection of parameters of the instruction
	 */
	public void add(AbstractOperation operation, Collection<AbstractParameter> params) {
		add(new Instruction(operation, (AbstractParameter[]) params.toArray(new AbstractParameter[params.size()])));
	}
	
	/**
	 * Adds the instruction to the list.
	 *
	 * @param instruction the instruction
	 */
	public void add(AbstractInstruction instruction) {
		list.add(instruction);
	}
	
	public Collection<AbstractInstruction> getList() {
		return list;
	}
	
	/**
	 * Returns the abstracted local variable.
	 *
	 * @param index 	the index of the variable
	 * @return the abstract parameter
	 */
	public AbstractParameter processVariable(int index) {
		
		// this
		if (index == 0 && !isStatic) {
			return parameters.getThis();
		}
		// method parameter
		else if (index <= maxArgs) {
			return parameters.getMethodParameter(index);
		}
		// variable
		else {
			return parameters.getVariable(index);
		}
	}
	
	/**
	 * Returns the abstracted class or array type.
	 *
	 * @param internalName 	the internal name
	 * @return the abstract parameter
	 */
	public AbstractParameter getClassOrArray(String internalName) {
		
		int sort = Type.getObjectType(internalName).getSort();
		
		if (sort == Type.ARRAY) {
			return parameters.getArray(internalName);
		}
		else if (sort == Type.OBJECT) {
			return parameters.getClass(internalName);
		}
		else {
			throw new IllegalArgumentException(internalName);
		}
	}
	
	/**
	 * Returns the collection of abstracted constant values.
	 *
	 * @param cst 		the constant object from ASM library
	 * @return the collection of parameters
	 */
	public Collection<AbstractParameter> processConstantValue(Object cst) {
		
		Collection<AbstractParameter> list = new ArrayList<>();
		
		if (cst instanceof Integer) {
			list.add(parameters.getInt((Integer)cst)); 

		} else if (cst instanceof Float) {
			list.add(parameters.getFloat((Float)cst));

		} else if (cst instanceof Long) {
			list.add(parameters.getLong((Long)cst));

		} else if (cst instanceof Double) {
			list.add(parameters.getDouble((Double)cst));

		} else if (cst instanceof String) {
			list.add(parameters.getString(((String)cst)));

		} else if (cst instanceof Type) {

			Type type = (Type) cst;
			list.add(getClassOrArray(type.getInternalName()));
			
			if (type.getSort() == Type.METHOD) {
				list.add(parameters.getMethod("-", type.getDescriptor()));
			} 
			
		} else if (cst instanceof Handle) {
			
			Handle handle = (Handle)cst;
			
			// tag
			list.add(operations.getHandleOperation(handle.getTag()));
			
			// owner
			list.add(getClassOrArray(handle.getOwner()));
			
			// field or method
			if (handle.getTag() <= Opcodes.H_PUTSTATIC) {
				list.add(parameters.getField(handle.getName(), handle.getDesc()));
			}
			else {
				list.add(parameters.getMethod(handle.getName(), handle.getDesc()));
			}

		} else {
			throw new IllegalArgumentException(cst.toString());
		}
		
		return list;
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitInsn(int)
	 */
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
		
		// add an instruction
		if (value == null)   	add(operation);
		else 					add(operation, value);
		
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitIntInsn(int, int)
	 */
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
				case Opcodes.T_BOOLEAN: type = "[Z"; break;
				case Opcodes.T_BYTE: 	type = "[B"; break;
				case Opcodes.T_CHAR: 	type = "[C"; break;
				case Opcodes.T_DOUBLE: 	type = "[D"; break;
				case Opcodes.T_FLOAT: 	type = "[F"; break;
				case Opcodes.T_INT: 	type = "[I"; break;
				case Opcodes.T_LONG: 	type = "[J"; break;
				case Opcodes.T_SHORT: 	type = "[S"; break;
				default: 				throw new IllegalArgumentException("Unknown type of the array.");
			}
			
			param = parameters.getArray(type);
		}
		// SIPUSH, BIPUSH
		else {
			param = parameters.getInt(operand);
		}
				
		// add instruction
		add(operation, param);
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitVarInsn(int, int)
	 */
	@Override
	public void visitVarInsn(int opcode, int var) {
		
		// add instruction
		add(operations.getOperation(opcode), processVariable(var));
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitTypeInsn(int, java.lang.String)
	 */
	@Override
	public void visitTypeInsn(int opcode, String type) {
			
		// add instruction
		add(operations.getOperation(opcode), getClassOrArray(type));
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitFieldInsn(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		
		// get parameters
		AbstractParameter param1 = getClassOrArray(owner);
		AbstractParameter param2 = parameters.getField(name, desc);
				
		// add instruction
		add(operations.getOperation(opcode), param1, param2);
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitMethodInsn(int, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		
		// get parameters
		AbstractParameter param1 = getClassOrArray(owner);
		AbstractParameter param2 = parameters.getMethod(name, desc);
								
		// add instruction
		add(operations.getOperation(opcode), param1, param2);
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitInvokeDynamicInsn(java.lang.String, java.lang.String, org.objectweb.asm.Handle, java.lang.Object[])
	 */
	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {

		// get parameters
		Collection<AbstractParameter> params = new ArrayList<>();
				
		// method
		params.add(parameters.getMethod(name, desc));

		// handle
		params.addAll(processConstantValue(bsm));
				
		// handle arguments
		for (Object arg : bsmArgs) {
			params.addAll(processConstantValue(arg));
		}
						
		// add instruction
		add(operations.getOperation(Opcodes.INVOKEDYNAMIC), params);
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitJumpInsn(int, org.objectweb.asm.Label)
	 */
	@Override
	public void visitJumpInsn(int opcode, Label label) {
				
		// add instruction
		add(operations.getOperation(opcode), labels.getLabel(label));
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitLabel(org.objectweb.asm.Label)
	 */
	@Override
	public void visitLabel(Label label) {
		
		// add instruction
		add(labels.getLabel(label));
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitLdcInsn(java.lang.Object)
	 */
	@Override
	public void visitLdcInsn(Object cst) {
						
		// add instruction
		add(operations.getOperation(Opcodes.LDC), processConstantValue(cst));
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitIincInsn(int, int)
	 */
	@Override
	public void visitIincInsn(int var, int increment) {
		
		// add instruction
		add(operations.getOperation(Opcodes.IINC), processVariable(var), parameters.getInt(increment));
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitTableSwitchInsn(int, int, org.objectweb.asm.Label, org.objectweb.asm.Label[])
	 */
	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... lbs) {
		
		// get parameters
		Collection<AbstractParameter> params = new ArrayList<>();
		
		params.add(parameters.getInt(min));
		params.add(parameters.getInt(max));
		params.add(labels.getLabel(dflt));
		
		for (Label label : lbs) {
			params.add(labels.getLabel(label));
		}
				
		// add instruction
		add(operations.getOperation(Opcodes.TABLESWITCH), params);
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitLookupSwitchInsn(org.objectweb.asm.Label, int[], org.objectweb.asm.Label[])
	 */
	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] lbs) {
		
		// get parameters
		Collection<AbstractParameter> params = new ArrayList<>();
		
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

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitMultiANewArrayInsn(java.lang.String, int)
	 */
	@Override
	public void visitMultiANewArrayInsn(String desc, int dims) {
		
		// get parameter 
		AbstractParameter param1 = parameters.getInt(dims);
		AbstractParameter param2 = parameters.getArray(desc);
		
		// add instruction
		add(operations.getOperation(Opcodes.MULTIANEWARRAY), param1, param2);
	}

}
