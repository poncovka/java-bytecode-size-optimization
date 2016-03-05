package jbyco.analyze.patterns.instr.param;

import org.objectweb.asm.Handle;

public interface AbstractParameterFactory {
	
	public AbstractParameter getVariable(int index);
	public AbstractParameter getInt(int i);
	public AbstractParameter getFloat(float f);
	public AbstractParameter getLong(long l);
	public AbstractParameter getDouble(double d);
	public AbstractParameter getString(String s);
	public AbstractParameter getClass(String internalName);
	public AbstractParameter getField(String name, String desc);
	public AbstractParameter getMethod(String name, String desc);
	public AbstractParameter getHandle(Handle handle);
	
	public AbstractParameter getNull();
	public AbstractParameter getThis();
	
	public void restart();
}
