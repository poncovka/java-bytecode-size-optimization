package jbyco.analyze.patterns.parameters;

/**
 * A factory for creating FullParameter objects.
 */
public class FullParameterFactory implements AbstractParameterFactory {
	
	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#init()
	 */
	@Override
	public void init() {}
	
	/**
	 * Creates a new FullParameter object.
	 *
	 * @param type the type
	 * @param components the components
	 * @return the abstract parameter
	 */
	public AbstractParameter createParameter(ParameterType type, Object ...components) {
		return new FullParameter(type, components);
	}
	
	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getVariable(int)
	 */
	@Override
	public AbstractParameter getVariable(int index) {
		return createParameter(ParameterType.VARIABLE, new Integer(index));
	}
	
	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getMethodParameter(int)
	 */
	@Override
	public AbstractParameter getMethodParameter(int index) {
		return createParameter(ParameterType.PARAMETER, new Integer(index));
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getInt(int)
	 */
	@Override
	public AbstractParameter getInt(int i) {
		return createParameter(ParameterType.INT, new Integer(i));
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getFloat(float)
	 */
	@Override
	public AbstractParameter getFloat(float f) {
		return createParameter(ParameterType.FLOAT, new Float(f));
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getLong(long)
	 */
	@Override
	public AbstractParameter getLong(long l) {
		return createParameter(ParameterType.LONG, new Long(l));
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getDouble(double)
	 */
	@Override
	public AbstractParameter getDouble(double d) {
		return createParameter(ParameterType.DOUBLE, new Double(d));
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getString(java.lang.String)
	 */
	@Override
	public AbstractParameter getString(String s) {
		return createParameter(ParameterType.STRING,  s);
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getClass(java.lang.String)
	 */
	@Override
	public AbstractParameter getClass(String internalName) {
		return createParameter(ParameterType.CLASS, internalName);
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getArray(java.lang.String)
	 */
	@Override
	public AbstractParameter getArray(String internalName) {
		return createParameter(ParameterType.ARRAY, internalName);
	}
	
	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getField(java.lang.String, java.lang.String)
	 */
	@Override
	public AbstractParameter getField(String name, String desc) {
		return createParameter(ParameterType.FIELD, name, desc);
	}

	/* (non-Javadoc)
	 * @see jbyco.analyze.patterns.parameters.AbstractParameterFactory#getMethod(java.lang.String, java.lang.String)
	 */
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
