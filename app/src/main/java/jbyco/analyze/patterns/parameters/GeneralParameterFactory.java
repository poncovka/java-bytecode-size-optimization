package jbyco.analyze.patterns.parameters;

public class GeneralParameterFactory implements AbstractParameterFactory {
	
	@Override
	public void init() {}
	
	@Override
	public AbstractParameter getVariable(int index) {
		return ParameterType.VARIABLE;
	}
	
	@Override
	public AbstractParameter getMethodParameter(int index) {
		return ParameterType.PARAMETER;
	}

	@Override
	public AbstractParameter getInt(int i) {
		return ParameterType.INT;
	}

	@Override
	public AbstractParameter getFloat(float f) {
		return ParameterType.FLOAT;
	}

	@Override
	public AbstractParameter getLong(long l) {
		return ParameterType.LONG;
	}

	@Override
	public AbstractParameter getDouble(double d) {
		return ParameterType.DOUBLE;
	}

	@Override
	public AbstractParameter getString(String s) {
		return ParameterType.STRING;
	}

	@Override
	public AbstractParameter getClass(String internalName) {
		return ParameterType.CLASS;
	}

	@Override
	public AbstractParameter getArray(String internalName) {
		return ParameterType.ARRAY;
	}
	
	@Override
	public AbstractParameter getField(String name, String desc) {
		return ParameterType.FIELD;
	}

	@Override
	public AbstractParameter getMethod(String name, String desc) {
		return ParameterType.METHOD;
	}

	@Override
	public AbstractParameter getNull() {
		return ParameterValue.NULL;
	}

	@Override
	public AbstractParameter getThis() {
		return ParameterValue.THIS;
	}
	
}
