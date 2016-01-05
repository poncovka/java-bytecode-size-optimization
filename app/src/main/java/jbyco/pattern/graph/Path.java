package jbyco.pattern.graph;

public class Path {
	
	int id;
	int counter;
	
	static int maxid = 0;

	public Path() {
		this.id = maxid++;
		this.counter = 1;
	}
	
	public void increment() {
		counter++;
	}
	
	@Override
	public String toString() {
		return Integer.toString(id);
	}
}
