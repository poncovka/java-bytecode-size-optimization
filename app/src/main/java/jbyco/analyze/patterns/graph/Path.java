package jbyco.analyze.patterns.graph;

public class Path implements Comparable<Path> {
	
	int id;
	int counter;
	
	static int maxid = 0;

	public Path() {
		this.id = maxid++;
		this.counter = 1;
	}
	
	public static int getTotal() {
		return maxid;
	}
	
	public void increment() {
		counter++;
	}
	
	public int getCount() {
		return counter;
	}
	
	@Override
	public String toString() {
		return Integer.toString(id);
	}

	@Override
	public int compareTo(Path path) {
		return Integer.compare(id, path.id);
	}
}