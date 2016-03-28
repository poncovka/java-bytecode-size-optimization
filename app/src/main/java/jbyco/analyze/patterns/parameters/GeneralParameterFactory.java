package jbyco.analyze.patterns.parameters;

public class GeneralParameterFactory implements AbstractParameterFactory {
	
	@Override
	public void init() {}
	
	@Override
	public AbstractParameter getVariable(int index) {
		return Type.VARIABLE;
	}
	
	@Override
	public AbstractParameter getMethodParameter(int index) {
		return Type.PARAMETER;
	}

	@Override
	public AbstractParameter getInt(int i) {
		return Type.INT;
	}

	@Override
	public AbstractParameter getFloat(float f) {
		return Type.FLOAT;
	}

	@Override
	public AbstractParameter getLong(long l) {
		return Type.LONG;
	}

	@Override
	public AbstractParameter getDouble(double d) {
		return Type.DOUBLE;
	}

	@Override
	public AbstractParameter getString(String s) {
		return Type.STRING;
	}

	@Override
	public AbstractParameter getClass(String internalName) {
		return Type.CLASS;
	}

	@Override
	public AbstractParameter getArray(String internalName) {
		return Type.ARRAY;
	}
	
	@Override
	public AbstractParameter getField(String name, String desc) {
		return Type.FIELD;
	}

	@Override
	public AbstractParameter getMethod(String name, String desc) {
		return Type.METHOD;
	}

	@Override
	public AbstractParameter getNull() {
		return Value.NULL;
	}

	@Override
	public AbstractParameter getThis() {
		return Value.THIS;
	}
	
}
