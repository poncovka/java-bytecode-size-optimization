package jbyco.optimize.renaming.graph;

import java.util.HashMap;
import java.util.Map;

public class PackageNode {
	
	PackageNode parent;
	
	String name;
	String rename;
	
	Map<String, PackageNode> packages;
	Map<String, KlassNode> klasses;
	
	int counter;
	
	public PackageNode(PackageNode parent, String name) {
		this.parent = parent;
		this.name = name;
		this.packages = new HashMap<>(0);
		this.klasses = new HashMap<>(0);
		this.counter = 0;
	}
	
	public PackageNode addPackage(String name) {
		
		PackageNode node = packages.get(name);
		
		if (node == null) {
			node = new PackageNode(this, name);
			packages.put(name, node);
		}
		
		return node;
	}
	
	public KlassNode addKlass(String name) {
		
		KlassNode node = klasses.get(name);
		
		if (node == null) {
			node = new KlassNode(this, name);
			klasses.put(name, node);
		}
		
		return node;
	}
	
	public PackageNode getPackage(String name) {
		return packages.get(name);
	}
	
	public KlassNode getKlass(String name) {
		return klasses.get(name);
	}
	
	public String getKey() {
		return name;
	}
	
	public void increment() {
		counter++;
	}

}
