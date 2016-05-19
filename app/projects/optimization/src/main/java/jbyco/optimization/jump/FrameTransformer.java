package jbyco.optimization.jump;

import jbyco.lib.Utils;
import jbyco.optimization.Statistics;
import jbyco.optimization.common.ActionLoader;
import jbyco.optimization.common.MethodTransformer;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vendy on 19.5.16.
 */
public class FrameTransformer extends MethodTransformer implements ActionLoader<FrameAction> {

    Statistics stats;
    JumpCollector collector;
    Collection<FrameAction> actions = new ArrayList<>();

    public FrameTransformer(JumpCollector collector, Statistics stats) {
        this.stats = stats;
        this.collector = collector;
    }

    public FrameTransformer(MethodTransformer mt, JumpCollector collector, Statistics stats) {
        super(mt);
        this.stats = stats;
        this.collector = collector;
    }

    @Override
    public void loadActions(Class<?>... libraries) {
        for (FrameAction action :  Utils.loadInstancesFromLibraries(FrameAction.class, libraries)) {
            loadAction(action);
        }
    }

    @Override
    public void loadActions(FrameAction... actions) {
        for (FrameAction action : actions) {
            loadAction(action);
        }
    }

    @Override
    public void loadAction(FrameAction action) {
        actions.add(action);
    }

    @Override
    public boolean transform(MethodNode mn) {
        InsnList list = mn.instructions;
        boolean change = false;

        for (FrameNodeInfo info : collector.frames.values()) {

            for (FrameAction action : actions) {
                if (action.replace(list, info, collector.labels)) {

                    // update statistics
                    if (change && stats != null) {
                        stats.addOptimization(action.toString());
                    }

                    // stop
                    if (change) {
                        break;
                    }
                }
            }

            // stop
            if (change) {
                break;
            }
        }

        if (!change) {
            change = super.transform(mn);
        }

        return change;
    }
}

