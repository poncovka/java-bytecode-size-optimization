package jbyco.optimization.peephole;

import jbyco.lib.Utils;
import jbyco.optimization.Statistics;
import jbyco.optimization.common.InsnUtils;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;

/**
 * The class loads patterns and actions from given
 * classes, finds patterns and applies actions on lists
 * of instructions. The patterns are searched in the order of loading.
 * Longer patterns have higher priority. Each list of instructions is searched
 * in the reverse order, so arguments of instructions are processed first.
 * <p>
 * Each action is an simplifications method that has the @Pattern annotation.
 * The expected definition of the action is:
 * <p>
 * public static boolean actionName(InsnList list, AbstractInsnNode[] matched);
 * <p>
 * The array of matched instructions contains instructions that match the pattern.
 * The method returns true if the list of instructions was modified, else false.
 *
 */
public class PeepholeRunner {

    Collection<StateMachine> loaded = new ArrayList<>();
    Collection<StateMachine> running;
    Statistics stats;

    public PeepholeRunner() {
        init();
    }

    public PeepholeRunner(Statistics stats) {
        this();
        this.stats = stats;
    }

    public void init() {
        running = new LinkedList<>();
    }

    public void loadActions(Class<?> ...libraries) {
        for (PeepholeAction action :  Utils.loadInstancesFromLibraries(PeepholeAction.class, libraries)) {
            loadAction(action);
        }
    }

    public void loadActions(PeepholeAction ...actions) {
        for (PeepholeAction action : actions) {
            loadAction(action);
        }
    }

    public void loadAction(PeepholeAction action) {
        for(Pattern pattern : action.getPatterns()) {
            loaded.add(new StateMachine(action, pattern.value()));
        }
    }

    public void findAndReplace(ClassNode cn) {
        //System.err.println(">>> Class:" + cn.name);
        for (Object mn : cn.methods) {
            findAndReplace(((MethodNode)mn));
        }
    }

    public void findAndReplace(MethodNode mn) {
        //System.err.println(">>> Method:" + mn.name);
        findAndReplace(mn.instructions);
    }

    public void findAndReplace(InsnList list) {
        //System.err.println(">>> Instructions:");
        //InsnUtils.debug(list);

        // iterate over the list while there is is a change
        boolean change = true;
        while (change) {

            // there is no change in the list
            change = false;

            // do not restart the search
            boolean restart = false;

            // initiate running
            init();

            // iterate over instructions of the list
            // the list is iterated from the last to the first frame!
            AbstractInsnNode node = list.getLast();
            while (!restart && node != null) {

                // get the state frame
                AbstractInsnNode next = node.getPrevious();

                // iterate over all loaded machines
                for (StateMachine fsm : loaded) {

                    // add some initial running to running
                    if (fsm.checkInput(node)) {
                        running.add(fsm.copy());
                    }
                }

                // iterate over running machines
                Iterator<StateMachine> i = running.iterator();
                while (!restart && i.hasNext()) {

                    // get the machine
                    StateMachine fsm = i.next();

                    // can the machine read the instruction?
                    if (!fsm.readInput(node)) {
                        i.remove();
                    }
                    // is the machine in a final state?
                    else if (fsm.inFinalState()) {

                        // modify the list of instructions
                        if (replace(fsm, list)) {
                            restart = true;
                            change = true;
                        }

                        i.remove();
                    }
                }

                // process the state frame
                node = next;
            }
        }
    }

    public boolean replace(StateMachine fsm, InsnList list) {

        // replace matched instructions in a list
        boolean result = fsm.applyAction(list);

        // record statistics
        if (result && stats != null) {
            stats.addOptimization(fsm.toString());
        }

        if (result) {
            //System.err.println(">>> Action: " + fsm);
            //System.err.println(">>> Matched:");
            //InsnUtils.debug(fsm.getMatchedInput());
            //System.err.println(">>> Result:");
            //InsnUtils.debug(list);
            //System.err.println();
        }

        // return the result
        return result;
    }

}
