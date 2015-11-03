package jbyco.stat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class MapComparator <K,V> implements Comparator<K> {
	
	Map<K,V> map;
	
	public MapComparator() {}
	
	public MapComparator(Map<K, V> map) {
		setMap(map);
	}
	
	public void setMap(Map<K, V> map) {
		this.map = map;
	}
	
	public List<K> getSortedKeys(Map<K, V> map) {
		
		// set map
		setMap(map);
		
		// create list
		List<K> list = new ArrayList<K>(map.keySet());
		
		// sort
		list.sort(this);
		
		return list;
	}

}
