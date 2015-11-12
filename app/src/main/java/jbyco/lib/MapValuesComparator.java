package jbyco.lib;

public class MapValuesComparator<K,V> extends MapComparator<K, V> {

	@Override
	public int compare(K key1, K key2) {
		// compare keys by values
		return ((Comparable<V>) map.get(key2)).compareTo(map.get(key1));
	}	
	

}
