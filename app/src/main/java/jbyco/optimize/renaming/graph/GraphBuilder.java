package jbyco.optimize.renaming.graph;

public class GraphBuilder {

	Graph graph;
	
	public GraphBuilder(Graph graph) {
		this.graph = graph;
	}
	
	public void addConstantPool(ConstantPool pool) {
		// TODO
	}
	
	public KlassNode addKlass(String name) {
		
		// get simple names
		String[] names = name.split("/");
		
		// get or create packages
		PackageNode packet = graph.getRoot();
		packet.increment();
		
		for (int i = 0; i < names.length - 1; i++) {
			packet = packet.addPackage(names[i]);
			packet.increment();
		}
		
		KlassNode klass = packet.getKlass(names[names.length - 1]);
		klass.increment();
		
		return klass;
	}
	
	public MethodNode addMethod(KlassNode klass, String name, String descriptor) {
		
		MethodNode method = klass.addMethod(name, descriptor);
		method.increment();
		return method;
	}
	
	public FieldNode addField(KlassNode klass, String name, String descriptor) {
		
		FieldNode field = klass.addField(name, descriptor);
		field.increment();
		return field;
	}}
