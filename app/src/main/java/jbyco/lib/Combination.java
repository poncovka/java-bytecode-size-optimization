package jbyco.lib;

import java.util.Arrays;
import java.util.Iterator;

public class Combination implements Iterator<int[]> {
	
	int min;
	int max;
	int index;
	int[] array;
	
	public Combination(int n, int min, int max) {
	
		// check arguments
		if (n <= 0 || min >= max || max - min + 1 < n ) {
			throw new IllegalArgumentException();
		}
		
		// init
		this.min = min;
		this.max = max;
		this.index = 0;
		this.array = new int[n];
		
		// init array
		this.array[0] = min - 1;
		this.index = 0;
	}

	@Override
	public boolean hasNext() {
		return 0 <= index && index < array.length;
	}
	
	@Override
	public int[] next() {
	
		// compute next combination;
		restartCombination(index, array[index] + 1);
		
		// compute next index
		index = computeNextIndex();
		
		return array;
	}
	
	public void restartCombination(int from, int value) {
	
		for(int i = from; i < array.length; i++) {
			array[i] = value;
			value++;
		}
		
	}

	public int computeNextIndex() {
		
		// find highest index with value smaller than the maximal possible one
		for (int i = array.length - 1; i >= 0; i--) {
			
			if (array[i] < max - (array.length - 1 - i)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public static void main(String[] args) {
		
		Combination i = new Combination(3, 1, 6);
		
		while(i.hasNext()) {
			System.out.println(Arrays.toString(i.next()));
		}
		
	}
}
