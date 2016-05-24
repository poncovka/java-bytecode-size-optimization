package jbyco.optimization.jump;

import jbyco.lib.Utils;
import jbyco.optimization.Statistics;
import jbyco.optimization.common.ActionLoader;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Method transformer to transform labels.
 */
public class LabelTransformer extends MethodTransformer implements ActionLoader<LabelAction> {

    Statistics stats;
    JumpCollector collector;
    Collection<LabelAction> actions = new ArrayList<>();

    public LabelTransformer(JumpCollector collector, Statistics stats) {
        this.collector = collector;
        this.stats = stats;
    }

    public LabelTransformer(MethodTransformer mt, JumpCollector collector, Statistics stats) {
        super(mt);
        this.collector = collector;
        this.stats = stats;
    }

    public void loadActions(Class<?> ...libraries) {
        for (LabelAction action :  Utils.loadInstancesFromLibraries(LabelAction.class, libraries)) {
            loadAction(action);
        }
    }

    public void loadActions(LabelAction ...actions) {
        for (LabelAction action : actions) {
            loadAction(action);
        }
    }

    public void loadAction(LabelAction action) {
        actions.add(action);
    }

    @Override
    public boolean transform(MethodNode mn) {
        InsnList list = mn.instructions;
        boolean change = false;

        for (LabelNodeInfo info : collector.labels.values()) {

            for (LabelAction action : actions) {

                // do action
                change = action.replace(list, info, collector.addresses);

                // update statistics
                if (change && stats != null) {
                    stats.addOptimization(action.getName());
                }

                // stop
                if (change) {
                    break;
                }
            }

            // stop
            if (change) {
                break;
            }
        }

        return change | super.transform(mn);
    }
}
