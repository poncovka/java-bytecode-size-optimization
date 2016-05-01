package jbyco.optimize.renaming.graph;

public class MethodNode {

    KlassNode klass;
    String name;
    String rename;

    String descriptor;

    int counter;

    public MethodNode(KlassNode klass, String name, String descriptor) {
        this.name = name;
        this.descriptor = descriptor;
        this.klass = klass;
        this.counter = 0;
    }

    public static String getKey(String name, String descriptor) {
        return name + " " + descriptor;
    }

    public String getKey() {
        return getKey(name, descriptor);
    }

    public void increment() {
        counter++;
    }
}
