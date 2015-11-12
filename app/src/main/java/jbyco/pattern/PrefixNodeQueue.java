package jbyco.pattern;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jbyco.pattern.graph.PrefixNode;

public class PrefixNodeQueue {
	
	int queueFirst;
	List<PrefixNode> queue;
	Set<PrefixNode> visited;
	
	public PrefixNodeQueue() {
		queue = new ArrayList<PrefixNode>();
		visited = new HashSet<PrefixNode>();
	}

	public void init() {
		queueFirst = 0;
		queue.clear();
		visited.clear();
	}
	
	public void add(PrefixNode node) {
		if (!visited.contains(node)) {
			queue.add(node);
			visited.add(node);
		}
	}
	
	public List<PrefixNode> all() {
		return queue;
	}
	
	public PrefixNode get() {
		
		PrefixNode node = null;
		
		if (queueFirst < queue.size()) {
				
			node = queue.get(queueFirst);
			queueFirst++;
		}
		
		return node;
	}
	
	
}
