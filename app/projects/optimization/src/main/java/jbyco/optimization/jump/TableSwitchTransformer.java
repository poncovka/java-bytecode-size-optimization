package jbyco.optimization.jump;

import jbyco.lib.Utils;
import jbyco.optimization.Statistics;
import jbyco.optimization.common.ActionLoader;
import jbyco.optimization.common.MethodTransformer;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vendy on 20.5.16.
 */
public class TableSwitchTransformer extends MethodTransformer implements ActionLoader<TableSwitchAction> {

    Statistics stats;
    JumpCollector collector;
    Collection<TableSwitchAction> actions = new ArrayList<>();

    public TableSwitchTransformer(MethodTransformer mt, JumpCollector collector, Statistics stats) {
        super(mt);
        this.collector = collector;
        this.stats = stats;
    }

    public void loadActions(Class<?> ...libraries) {
        for (TableSwitchAction action :  Utils.loadInstancesFromLibraries(TableSwitchAction.class, libraries)) {
            loadAction(action);
        }
    }

    public void loadActions(TableSwitchAction ...actions) {
        for (TableSwitchAction action : actions) {
            loadAction(action);
        }
    }

    public void loadAction(TableSwitchAction action) {
        actions.add(action);
    }

    @Override
    public boolean transform(MethodNode mn) {
        InsnList list = mn.instructions;
        boolean change = false;

        for (TableSwitchInsnNode node : collector.tableSwitches) {

            for (TableSwitchAction action : actions) {

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
