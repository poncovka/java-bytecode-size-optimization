package jbyco.optimization.jump;

import jbyco.optimization.Statistics;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * Created by vendy on 19.5.16.
 */
public class JumpTransformer extends ClassTransformer {

    Statistics stats;
    JumpCollector collector;
    MethodTransformer mt;

    public JumpTransformer(MethodTransformer mt, JumpCollector collector, Statistics stats) {
        this.mt = mt;
        this.collector = collector;
        this.stats = stats;
    }

    public JumpTransformer(ClassTransformer ct, MethodTransformer mt, JumpCollector collector, Statistics stats) {
        super(ct);
        this.mt = mt;
        this.collector = collector;
        this.stats = stats;
    }

    @Override
    public boolean transform(ClassNode cn) {
        // no change in the class
        boolean classChange = false;

        // process methods
        for (MethodNode mn : (List<MethodNode>)cn.methods) {

            // get instruction list
            InsnList list = mn.instructions;

            // do while there are changes in the method
            boolean change = true;
            while(change) {

                // no change in the method
                change = false;

                // collect data
                collector.collect(mn);

                // transform method with chain of transformers
                if (mt.transform(mn)) {
                    classChange = true;
                    change = true;
                    continue;
                }
            }
        }

        // call next class transformer
        return classChange | super.transform(cn);
    }
}
