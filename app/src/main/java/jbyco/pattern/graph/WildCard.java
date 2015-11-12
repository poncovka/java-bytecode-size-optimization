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
	
	public void setMin(int min) {
		this.min = min;
		
	}

	public void setMax(int max) {
		this.max = max;
	}
	
	@Override
	public String toString() {
		return "."  + "{" + min + "," + max + "}";
	}
}
