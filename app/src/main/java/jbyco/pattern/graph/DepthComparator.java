package jbyco.pattern.graph;

import java.util.Comparator;

public class DepthComparator implements Comparator<SuffixNode> {
	
	static final DepthComparator comparator = new DepthComparator();
	
	static public DepthComparator getInstance() {
		return comparator;
	}
	
	@Override
	public int compare(SuffixNode n1, SuffixNode n2) {
		return n1.getDepth() - n2.getDepth();
	}

}
