package jbyco.stat;

public class MapKeysComparator <K,V> extends MapComparator<K, V> {

	@Override
	public int compare(K key1, K key2) {
		// compare keys by values
		return ((Comparable<K>) key2).compareTo(key1);
	}	



}
