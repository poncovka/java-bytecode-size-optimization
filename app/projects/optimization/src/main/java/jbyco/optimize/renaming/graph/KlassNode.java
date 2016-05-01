package jbyco.optimize.renaming.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class KlassNode {

    PackageNode packet;

    String name;
    String rename;

    Collection<KlassNode> parents;
    Map<String, FieldNode> fields;    // key is name and descriptor
    Map<String, MethodNode> methods; // key is name and descriptor

    int counter;

    public KlassNode(PackageNode packet, String name) {
        this.packet = packet;
        this.name = name;
        this.parents = new ArrayList<>();
        this.fields = new HashMap<>(0);
        this.methods = new HashMap<>(0);
        this.counter = 0;
    }

    public void addParent(KlassNode parent) {
        parents.add(parent);
    }

    public FieldNode addField(String name, String descriptor) {

        String key = FieldNode.getKey(name, descriptor);
        FieldNode node = fields.get(key);

        if (node == null) {
            node = new FieldNode(this, name, descriptor);
            fields.put(key, node);
        }

        return node;
    }

    public MethodNode addMethod(String name, String descriptor) {

        String key = MethodNode.getKey(name, descriptor);
        MethodNode node = methods.get(key);

        if (node == null) {
            node = new MethodNode(this, name, descriptor);
            methods.put(key, node);
        }

        return node;
    }

    public FieldNode getField(String key) {
        return fields.get(key);
    }

    public MethodNode getMethod(String key) {
        return methods.get(key);
    }

    public String getKey() {
        return name;
    }

    public void increment() {
        counter++;
    }
}

