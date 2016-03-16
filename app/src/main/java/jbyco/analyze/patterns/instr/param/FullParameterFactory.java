package jbyco.analyze.patterns.instr.param;

import org.apache.commons.lang3.StringEscapeUtils;

import jbyco.lib.Utils;

public class FullParameterFactory implements AbstractParameterFactory {
	
	public AbstractParameter createParameter(Type type, Object ...components) {
		return new MulticomponentParameter(type, components);
	}
	
	@Override
	public AbstractParameter getVariable(int index) {
		return createParameter(Type.VARIABLE, new Integer(index));
	}
	
	@Override
	public AbstractParameter getMethodParameter(int index) {
		return createParameter(Type.PARAMETER, new Integer(index));
	}

	@Override
	public AbstractParameter getInt(int i) {
		return createParameter(Type.INT, new Integer(i));
	}

	@Override
	public AbstractParameter getFloat(float f) {
		return createParameter(Type.FLOAT, new Float(f));
	}

	@Override
	public AbstractParameter getLong(long l) {
		return createParameter(Type.LONG, new Long(l));
	}

	@Override
	public AbstractParameter getDouble(double d) {
		return createParameter(Type.DOUBLE, new Double(d));
	}

	@Override
	public AbstractParameter getString(String s) {
		return createParameter(Type.STRING,  Utils.getEscapedString(s, "\""));
	}

	@Override
	public AbstractParameter getClass(String internalName) {
		return createParameter(Type.CLASS, internalName);
	}

	@Override
	public AbstractParameter getField(String name, String desc) {
		return createParameter(Type.FIELD, name, desc);
	}

	@Override
	public AbstractParameter getMethod(String name, String desc) {
		return createParameter(Type.METHOD, name, desc);
	}

	@Override
	public AbstractParameter getNull() {
		return Value.NULL;
	}

	@Override
	public AbstractParameter getThis() {
		return Value.THIS;
	}

	@Override
	public void restart() {}
	
}
