package jbyco.analyze.patterns.instr;

public class CachedInstruction implements AbstractInstruction {
	
	final int id;
	static int maxid = 0;
	final AbstractInstruction instr;
	
	public CachedInstruction(AbstractInstruction instr) {
		this.id = maxid++;
		this.instr = instr;
		
		if (id > maxid) {
			throw new ArithmeticException("Integer overflow.");
		}
	}
	
	@Override
	public String toString() {
		return instr.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CachedInstruction other = (CachedInstruction) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
}
