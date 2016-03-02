package jbyco.analyze.patterns.instr;

import org.objectweb.asm.Handle;

public class FullParameterFactory extends NumberedParameterFactory {

	@Override
	public Parameter getVariable(int index) {
		// TODO Auto-generated method stub
		return super.getVariable(index);
	}

	@Override
	public Parameter getInt(int i) {
		// TODO Auto-generated method stub
		return super.getInt(i);
	}

	@Override
	public Parameter getFloat(float f) {
		// TODO Auto-generated method stub
		return super.getFloat(f);
	}

	@Override
	public Parameter getLong(long l) {
		// TODO Auto-generated method stub
		return super.getLong(l);
	}

	@Override
	public Parameter getDouble(double d) {
		// TODO Auto-generated method stub
		return super.getDouble(d);
	}

	@Override
	public Parameter getString(String s) {
		// TODO Auto-generated method stub
		return super.getString(s);
	}

	@Override
	public Parameter getClass(String internalName) {
		// TODO Auto-generated method stub
		return super.getClass(internalName);
	}

	@Override
	public Parameter getField(String name, String desc) {
		// TODO Auto-generated method stub
		return super.getField(name, desc);
	}

	@Override
	public Parameter getMethod(String name, String desc) {
		// TODO Auto-generated method stub
		return super.getMethod(name, desc);
	}

	@Override
	public Parameter getHandle(Handle handle) {
		// TODO Auto-generated method stub
		return super.getHandle(handle);
	}

	
}
