package jbyco.analyze.patterns.instr;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;

public interface ParameterFactory {
	
	public Parameter getLabel(Label label);
	public Parameter getVariable(int index);
	public Parameter getInt(int i);
	public Parameter getFloat(float f);
	public Parameter getLong(long l);
	public Parameter getDouble(double d);
	public Parameter getString(String s);
	public Parameter getClass(String internalName);
	public Parameter getField(String name, String desc);
	public Parameter getMethod(String name, String desc);
	public Parameter getHandle(Handle handle);
	
	public Parameter getNull();
	public Parameter getThis();
}
