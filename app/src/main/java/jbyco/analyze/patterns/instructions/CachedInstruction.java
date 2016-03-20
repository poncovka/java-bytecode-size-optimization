package jbyco.analyze.patterns.instructions;

public class CachedInstruction implements AbstractInstruction, Comparable<CachedInstruction> {

	final int id;
	static int maxid = 0;
	final AbstractInstruction instruction;
	
	public CachedInstruction(AbstractInstruction instruction) {
		
		this.id = maxid++;
		this.instruction = instruction;
		
		if (maxid == 0) {
			throw new ArithmeticException("Integer overflow.");
		}
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return instruction.toString();
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

	@Override
	public int compareTo(CachedInstruction instruction) {
		return Integer.compare(this.id, instruction.id);
	}

}
