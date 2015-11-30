package jbyco.pattern.graph;

public class WildCard {
	
	int min;
	int max;
	
	public WildCard() {
		this.min = 1;
		this.max = 1;
	}

	public WildCard(int min, int max) {
		this.min = min;
		this.max = max;
	}

	
	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
	
	public void addParallel(WildCard w) {
		this.min = Integer.min(this.min, w.min);
		this.max = Integer.max(this.max, w.max);
	}

	public void addSequencial(WildCard w) {
		this.min += w.min;
		this.max += w.max;
	}

	
	@Override
	public String toString() {
		return "."  + "{" + min + "," + max + "}";
	}
}
