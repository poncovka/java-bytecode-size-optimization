package jbyco.optimization.jump;

import jbyco.lib.Utils;
import jbyco.optimization.Statistics;
import jbyco.optimization.common.ActionLoader;
import jbyco.optimization.common.MethodTransformer;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vendy on 20.5.16.
 */
public class LookupSwitchTransformer extends MethodTransformer implements ActionLoader<LookupSwitchAction> {

    Statistics stats;
    JumpCollector collector;
    MethodTransformer mt;
    Collection<LookupSwitchAction> actions = new ArrayList<>();

    public LookupSwitchTransformer(MethodTransformer mt, JumpCollector collector, Statistics stats) {
        this.mt = mt;
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

        if (!change) {
            change = super.transform(mn);
        }

        return change;
    }
}
