package jbyco.analyze.patterns.wildcards;

import java.util.Arrays;
import java.util.Iterator;

/**
 * A class for iterating over combinations of numbers.
 */
public class CombinationIterator implements Iterator<int[]> {
	
	/** The minimal value. */
	int min;
	
	/** The maximal value. */
	int max;
	
	/** The index of next change. */
	int index;
	
	/** The current combination. */
	int[] array;
	
	/** The result of the check method. */
	boolean check;
	
	/**
	 * Instantiates a new combination iterator.
	 *
	 * @param n the length of the combination
	 * @param min the minimal value
	 * @param max the maximal value
	 */
	public CombinationIterator(int n, int min, int max) {
		
		// init
		this.min = min;
		this.max = max;
		this.index = 0;
		this.array = new int[n];
		
		// init array
		this.array[0] = min - 1;
		this.index = 0;
		
		// check parameters
		this.check = check(n, min, max);
	}

	/**
	 * Check the conditions.
	 *
	 * @param n the length of the combination
	 * @param min the minimal value
	 * @param max the maximal value
	 * @return true, if successful
	 */
	public boolean check(int n, int min, int max) {
		return n > 0 && min < max && max - min + 1 >= n;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return 0 <= index && index < array.length && check;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public int[] next() {
	
		// compute next combination;
		restartCombination(index, array[index] + 1);
		
		// compute next index
		index = computeNextIndex();
		
		return array;
	}
	
	/**
	 * Restart combination.
	 *
	 * @param from the index from where the restart starts
	 * @param value the value used for initialization
	 */
	public void restartCombination(int from, int value) {
	
		for(int i = from; i < array.length; i++) {
			array[i] = value;
			value++;
		}
		
	}

	/**
	 * Compute next index of change.
	 *
	 * @return the index or -1
	 */
	public int computeNextIndex() {
		
		// find highest index with value smaller than the maximal possible one
		for (int i = array.length - 1; i >= 0; i--) {
			
			if (array[i] < max - (array.length - 1 - i)) {
				return i;
			}
		}
		
		return -1;
	}
	
}
