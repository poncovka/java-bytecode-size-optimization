package jbyco.analyze.patterns.instructions;

/**
 * The instruction that represents one specific abstracted instruction.
 * Cached instructions are created and stored in {@link Cache}.
 */
public class CachedInstruction implements AbstractInstruction {

	/** The id of the cached instruction. */
	final int id;
	
	/** The maximal id of the cached instructions. */
	static int maxid = 0;
	
	/** The represented abstracted instruction. */
	final AbstractInstruction instruction;
	
	/**
	 * Instantiates a new cached instruction.
	 *
	 * @param instruction the instruction
	 */
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
	
	public AbstractInstruction getInstruction() {
		return instruction;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return instruction.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
