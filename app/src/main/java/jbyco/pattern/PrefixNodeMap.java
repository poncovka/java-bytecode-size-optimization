package jbyco.pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jbyco.pattern.graph.PrefixNode;

public class PrefixNodeMap {
	
	// number of restarts
	int cycle;
	
	// map items to node lists
	Map<Object, PrefixNodeList> map;
	
	public PrefixNodeMap() {
		map = new LinkedHashMap<>();
	}
	
	public void init() {
		
		// oveflow - init all lists
		if (cycle == Integer.MAX_VALUE) {
			
			cycle = 0;
			
			for(PrefixNodeList list : map.values()) {
				list.init(cycle);
			}
		}
		// inkrement cycle
		else {
			cycle++;
		}
	}
	
	private PrefixNodeList get(PrefixNode node) {

		// get list
		Object item = node.getItem();
		PrefixNodeList list = map.get(item);

		// create list
		if (list == null) {
			list = new PrefixNodeList(cycle);
			map.put(item, list);
		}
		// restart
		else if (cycle != list.getCycle()){
			list.init(cycle);
		}
		
		// return list
		return list;
	}
	
	public void add(PrefixNode node) {
		PrefixNodeList list = get(node);
		list.add(node);
	}
	
	public List<PrefixNode> getList(PrefixNode node) {
		return get(node).getList();
	}
	
	
	
	public class PrefixNodeList {
		
		int cycle;
		List<PrefixNode> list;

		public PrefixNodeList(int cycle) {
			list = new ArrayList<>();
			this.cycle = cycle;
		}
		
		public void add(PrefixNode node) {
			list.add(node);
		}
		
		public int getCycle() {
			return cycle;
		}
		
		public List<PrefixNode> getList() {
			return list;
		}
		
		public void init(int cycle) {
			this.cycle = cycle;
			list.clear();
		}
	}	
}
