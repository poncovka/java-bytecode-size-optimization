package jbyco.stat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MapValuesComparator<K,V> implements Comparator<K> {

	Map<K,V> map;
	
	public MapValuesComparator(Map<K, V> map) {
		this.map = map;
	}
	
	@Override
	public int compare(K key1, K key2) {
		// compare keys by values
		return ((Comparable<V>) map.get(key2)).compareTo(map.get(key1));
	}	
	
	
	static <K,V> List<K> getSortedList(Map<K, V> map) {
		
		// create comparator
		MapValuesComparator<K,V> comparator = new MapValuesComparator<K,V>(map);
		
		// create list
		List<K> list = new ArrayList<K>(map.keySet());
		
		// sort
		list.sort(comparator);
		
		return list;
	}
}
