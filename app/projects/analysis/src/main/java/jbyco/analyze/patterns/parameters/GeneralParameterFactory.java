package jbyco.analyze.patterns.parameters;

/**
 * A factory for creating general parameters.
 */
public class GeneralParameterFactory implements AbstractParameterFactory {
	
	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#init()
	 */
	@Override
	public void init() {}
	
	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getVariable(int)
	 */
	@Override
	public AbstractParameter getVariable(int index) {
		return ParameterType.VARIABLE;
	}
	
	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getMethodParameter(int)
	 */
	@Override
	public AbstractParameter getMethodParameter(int index) {
		return ParameterType.PARAMETER;
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getInt(int)
	 */
	@Override
	public AbstractParameter getInt(int i) {
		return ParameterType.INT;
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getFloat(float)
	 */
	@Override
	public AbstractParameter getFloat(float f) {
		return ParameterType.FLOAT;
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getLong(long)
	 */
	@Override
	public AbstractParameter getLong(long l) {
		return ParameterType.LONG;
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getDouble(double)
	 */
	@Override
	public AbstractParameter getDouble(double d) {
		return ParameterType.DOUBLE;
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getString(java.lang.String)
	 */
	@Override
	public AbstractParameter getString(String s) {
		return ParameterType.STRING;
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getClass(java.lang.String)
	 */
	@Override
	public AbstractParameter getClass(String internalName) {
		return ParameterType.CLASS;
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getArray(java.lang.String)
	 */
	@Override
	public AbstractParameter getArray(String internalName) {
		return ParameterType.ARRAY;
	}
	
	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getField(java.lang.String, java.lang.String)
	 */
	@Override
	public AbstractParameter getField(String name, String desc) {
		return ParameterType.FIELD;
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getMethod(java.lang.String, java.lang.String)
	 */
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
