package jbyco.pattern.graph;

public class Path implements Comparable<Path> {
	
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
		return id + "x" + counter;
	}

	@Override
	public int compareTo(Path path) {
		return Integer.compare(id, path.id);
	}
}
