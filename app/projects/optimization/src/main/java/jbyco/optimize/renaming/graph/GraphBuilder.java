package jbyco.optimize.renaming.graph;

import jbyco.optimize.renaming.pool.StringPool;
import jbyco.optimize.renaming.pool.StringPool.Entity;
import jbyco.optimize.renaming.pool.StringPool.EntityType;

public class GraphBuilder {

    Graph graph;

    public GraphBuilder(Graph graph) {
        this.graph = graph;
    }

    public void addStringPool(StringPool pool) {

        for (Entity entity : pool.getPool()) {

            if (entity.type == EntityType.FIELD) {

                // add klass
                KlassNode klass = addKlass(entity.items[0]);

                // add field
                FieldNode field = addField(klass, entity.items[1], entity.items[2]);
            }
            else if (entity.type == EntityType.METHOD) {

                // add klass
                KlassNode klass = addKlass(entity.items[0]);

                // add method
                MethodNode method = addMethod(klass, entity.items[1], entity.items[2]);
            }
            else {

                String desc =  entity.items[0];


            }
        }
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
