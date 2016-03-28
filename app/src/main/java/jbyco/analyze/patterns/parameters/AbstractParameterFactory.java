package jbyco.analyze.patterns.parameters;

public interface AbstractParameterFactory {
	
	public void init();
	
	public AbstractParameter getVariable(int index);
	public AbstractParameter getInt(int i);
	public AbstractParameter getFloat(float f);
	public AbstractParameter getLong(long l);
	public AbstractParameter getDouble(double d);
	public AbstractParameter getString(String s);
	public AbstractParameter getClass(String internalName);
	public AbstractParameter getField(String name, String desc);
	public AbstractParameter getMethod(String name, String desc);
	public AbstractParameter getMethodParameter(int index);
	
	public AbstractParameter getNull();
	public AbstractParameter getThis();
}
