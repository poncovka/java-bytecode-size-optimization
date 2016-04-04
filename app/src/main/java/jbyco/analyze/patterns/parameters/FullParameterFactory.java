package jbyco.analyze.patterns.parameters;

public class FullParameterFactory implements AbstractParameterFactory {
	
	@Override
	public void init() {}
	
	public AbstractParameter createParameter(ParameterType type, Object ...components) {
		return new FullParameter(type, components);
	}
	
	@Override
	public AbstractParameter getVariable(int index) {
		return createParameter(ParameterType.VARIABLE, new Integer(index));
	}
	
	@Override
	public AbstractParameter getMethodParameter(int index) {
		return createParameter(ParameterType.PARAMETER, new Integer(index));
	}

	@Override
	public AbstractParameter getInt(int i) {
		return createParameter(ParameterType.INT, new Integer(i));
	}

	@Override
	public AbstractParameter getFloat(float f) {
		return createParameter(ParameterType.FLOAT, new Float(f));
	}

	@Override
	public AbstractParameter getLong(long l) {
		return createParameter(ParameterType.LONG, new Long(l));
	}

	@Override
	public AbstractParameter getDouble(double d) {
		return createParameter(ParameterType.DOUBLE, new Double(d));
	}

	@Override
	public AbstractParameter getString(String s) {
		return createParameter(ParameterType.STRING,  s);
	}

	@Override
	public AbstractParameter getClass(String internalName) {
		return createParameter(ParameterType.CLASS, internalName);
	}

	@Override
	public AbstractParameter getArray(String internalName) {
		return createParameter(ParameterType.ARRAY, internalName);
	}
	
	@Override
	public AbstractParameter getField(String name, String desc) {
		return createParameter(ParameterType.FIELD, name, desc);
	}

	@Override
	public AbstractParameter getMethod(String name, String desc) {
		return createParameter(ParameterType.METHOD, name, desc);
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
