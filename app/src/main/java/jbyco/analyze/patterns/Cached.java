package jbyco.analyze.patterns;

public class Cached<T> {
	
	final int id;
	static int maxid = 0;
	final T item;
	
	public Cached(T item) {
		this.id = maxid++;
		this.item = item;
		
		if (maxid == 0) {
			throw new ArithmeticException("Integer overflow.");
		}
	}
	
	@Override
	public String toString() {
		return item.toString();
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
		
		Cached<?> other = (Cached<?>) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}


	
}
