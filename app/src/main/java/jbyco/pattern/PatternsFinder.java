package jbyco.pattern;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import jbyco.lib.SearchIterator;
import jbyco.pattern.graph.SuffixGraph;
import jbyco.pattern.graph.SuffixNode;
import jbyco.pattern.graph.WildCard;

public class PatternsFinder extends SearchIterator<Pattern> {

	SuffixGraph graph;
	String delimiter;
	PriorityQueue<QueueItem> queue;
	
	public PatternsFinder(SuffixGraph graph, String delimiter) {
		
		this.graph = graph;
		this.delimiter = delimiter;
		
		initQueue();
	}
	
	public void initQueue() {
		
		// init queue
		this.queue = new PriorityQueue<>();
		SuffixNode root = graph.getRoot();
		
		// skip root and first level wild cards
		for(SuffixNode next:root.getOutputNodes()) {
					
			if(!(next.getItem() instanceof WildCard)) {
				queue.add(new QueueItem(next, "", root.getEdgePaths(next)));
			}		
		}	
	}
	
	public Pattern search() {
		
		Pattern pattern = null;
		
		// process queue
		while (!queue.isEmpty() && pattern == null) {
			
			// get first item
			QueueItem item = queue.poll();
			
			SuffixNode node = item.node;
			String string = new String(item.string);
			Set<Integer> paths = new HashSet<>(item.paths);
			
			// update pattern
			if(string.length() > 0) {
				string += delimiter;
			}
			
			string += node.getItem();

			// return the pattern if it does not end with wild card
			if (!(node.getItem() instanceof WildCard)) {
				pattern = new Pattern(string, paths.size());
			}
			
			// add next nodes
			for(SuffixNode next:node.getOutputNodes()) {
				
				// get paths on edge
				Set<Integer> nextPaths = node.getEdgePaths(next);

				// get intersection with paths
				nextPaths.retainAll(paths);
				
				// add to queue if it has at least one path
				if(nextPaths.size() > 0) {
					queue.add(new QueueItem(next, string, nextPaths));
				}
			}

			//System.out.println("PATTERN: " + node + "\t" +  pattern);
		}
		
		return pattern;
	}

	class QueueItem implements Comparable<QueueItem>{
		
		public final SuffixNode node;
		public final String string;
		public final Set<Integer> paths;
		
		public QueueItem(SuffixNode node, String string, Set<Integer> paths) {
			
			this.node = node;
			this.string = string;
			this.paths = paths;
		}

		@Override
		public int compareTo(QueueItem o) {
			return ((Integer)paths.size()).compareTo(o.paths.size());
		}
	}
	
}
