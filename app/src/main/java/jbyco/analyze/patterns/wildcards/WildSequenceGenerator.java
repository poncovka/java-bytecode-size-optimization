package jbyco.analyze.patterns.wildcards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class WildSequenceGenerator<T> implements Iterator<Collection<T>> {

	// number of wild cards in a list
	int wildcards;
	
	// iterator over all allowed indexes of wild cards in a list
	CombinationIterator iterator;
	
	// list of values
	T[] list;
	
	// wild card
	T wildcard;
	
	@SuppressWarnings("unchecked")
	public WildSequenceGenerator(Collection<T> list, T wildcard, int wildcards) {
		
		this.list = (T[]) list.toArray();
		this.wildcard = wildcard;
		this.wildcards = wildcards;
		this.iterator = new CombinationIterator(2 * wildcards, 1, list.size() - 1);
		
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Collection<T> next() {
		
		// get next combination of indexes
		int[] combination = iterator.next();
		
		// return list of values and wild cards
		return applyCombination(list, wildcard, combination);
	}
	
	public static <T> boolean check(Collection<T> list, int wildcards) {
		//return list.size() <= 2 || list.size() < 2 * wildcards + 1;
		return CombinationIterator.check(2 * wildcards, 1, list.size() - 1);
	}
	
	public static int getOrDefault(int[] combination, int index, int value) {
		
		if (0 <= index && index < combination.length) {
			return combination[index];
		}
		else {
			return value;
		}
	}
	
	static public <T> Collection<T> applyCombination(T[] values, T value, int[] combination) {
		
		// get size of the array
		int length = values.length;
		
		// create a list of
		Collection<T> result = new ArrayList<>(length);

		int i = 0;
		int j = 0;
			
		while (0 <= i && i < length) {
				
			// get interval (a,b)
			int a = getOrDefault(combination, j++, length);
			int b = getOrDefault(combination, j++, length);
				
			// add instructions before a
			while (i < a) {
				result.add(values[i]);
				i++;
			}
				
			// i == a, add wild card if a is in boundaries
			if (i < length) {
				result.add(value);
			}
				
			// skip instructions till b
			i = b;
		}
			
		return result;
	}
	
}
