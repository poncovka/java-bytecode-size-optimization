package jbyco.analyze.patterns.instr;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;

public class GeneralParameterFactory implements ParameterFactory {
	
	enum Type implements Parameter {
		
		LABEL 		("LABEL"),
		VARIABLE 	("V"),
		INT 		("I"),
		FLOAT 		("F"),
		LONG 		("L"),
		DOUBLE 		("D"),
		STRING 		("S"),
		CLASS		("CLASS"),
		FIELD		("FIELD"),
		METHOD		("METHOD"),
		HANDLER		("HANDLER");
		
		private final String abbr;
		
		Type(String abbr) {
			this.abbr = abbr;
		}
		
		@Override
		public String toString() {
			return this.abbr;
		}
	}

	enum Value implements Parameter {
		
		NULL		("null"),
		THIS		("this");
		
		private final String abbr;
		
		Value(String abbr) {
			this.abbr = abbr;
		}
		
		@Override
		public String toString() {
			return this.abbr;
		}
	}
	
	@Override
	public Parameter getLabel(Label label) {
		return Type.LABEL;
	}

	@Override
	public Parameter getVariable(int index) {
		return Type.VARIABLE;
	}

	@Override
	public Parameter getInt(int i) {
		return Type.INT;
	}

	@Override
	public Parameter getFloat(float f) {
		return Type.FLOAT;
	}

	@Override
	public Parameter getLong(long l) {
		return Type.LONG;
	}

	@Override
	public Parameter getDouble(double d) {
		return Type.DOUBLE;
	}

	@Override
	public Parameter getString(String s) {
		return Type.STRING;
	}

	@Override
	public Parameter getClass(String internalName) {
		return Type.CLASS;
	}

	@Override
	public Parameter getField(String name, String desc) {
		return Type.FIELD;
	}

	@Override
	public Parameter getMethod(String name, String desc) {
		return Type.METHOD;
	}

	@Override
	public Parameter getHandle(Handle handle) {
		return Type.HANDLER;
	}

	@Override
	public Parameter getNull() {
		return Value.NULL;
	}

	@Override
	public Parameter getThis() {
		return Value.THIS;
	}
	
}
