package jbyco.optimize.renaming.graph;

public class Graph {

	PackageNode root;
	
	public Graph() {
		root = new PackageNode(null, "");
	}
	
	public PackageNode getRoot() {
		return root;
	}
}
