package jbyco.analyze.patterns.instr;

public class Parameter {
	
	// type of parameter
	public final Type type;
	
	// number of the parameter
	public final int number;
	
	public Parameter(Type type) {
		this.type = type;
		this.number = type.getNumber();
	}
	
	@Override
	public String toString() {
		return type.getName() + number;
	}
	
	enum Type {
		
		LABEL 		("LABEL"),
		VARIABLE 	("V"),
		OBJECT 		("O"),
		INT 		("I"),
		FLOAT 		("F"),
		LONG 		("L"),
		DOUBLE 		("D"),
		STRING 		("S"),
		CLASS		("CLASS"),
		FIELD		("FIELD"),
		METHOD		("METHOD"),
		INTERFACE 	("INTERFACE"),
		DYNAMIC		("DYNAMIC");
		
		private final String name;
		private int number;
		
		Type(String name) {
			this.name = name;
			this.number = 0;
		}
		
		public String getName() {
			return this.name;
		}
		
		public int getNumber() {
			return this.number++;
		}
		
		static public void reset() {
			
			for (Type t : Type.values()) {
				t.number = 0;
			}		
		}
	}

	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		
		result = prime * result + number;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		
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
		
		Parameter other = (Parameter) obj;
		
		if (number != other.number) {
			return false;
		}
		
		if (type != other.type) {
			return false;
		}
		
		return true;
	}	
}
