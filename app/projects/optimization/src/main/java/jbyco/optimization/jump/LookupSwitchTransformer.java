package jbyco.optimization.jump;

import jbyco.lib.Utils;
import jbyco.optimization.Statistics;
import jbyco.optimization.common.ActionLoader;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vendy on 20.5.16.
 */
public class LookupSwitchTransformer extends MethodTransformer implements ActionLoader<LookupSwitchAction> {

    Statistics stats;
    JumpCollector collector;
    Collection<LookupSwitchAction> actions = new ArrayList<>();

    public LookupSwitchTransformer(MethodTransformer mt, JumpCollector collector, Statistics stats) {
        super(mt);
        this.collector = collector;
        this.stats = stats;
    }

    public void loadActions(Class<?> ...libraries) {
        for (LookupSwitchAction action :  Utils.loadInstancesFromLibraries(LookupSwitchAction.class, libraries)) {
            loadAction(action);
        }
    }

    public void loadActions(LookupSwitchAction ...actions) {
        for (LookupSwitchAction action : actions) {
            loadAction(action);
        }
    }

    public void loadAction(LookupSwitchAction action) {
        actions.add(action);
    }

    @Override
    public boolean transform(MethodNode mn) {
        InsnList list = mn.instructions;
        boolean change = false;

        for (LookupSwitchInsnNode node : collector.lookupSwitches) {

            for (LookupSwitchAction action : actions) {

                // do action
                change = action.replace(list, node, collector.addresses);

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
