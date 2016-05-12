package jbyco.optimization.peephole;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The class loads patterns and simplifications from given
 * classes, finds given patterns and applies given simplifications on lists
 * of instructions.
 * <p>
 * Each action is an simplifications method that has the @Pattern annotation.
 * The expected definition of the action is:
 * <p>
 * public static boolean actionName(InsnList list, AbstractInsnNode[] matched);
 * <p>
 * The array of matched instructions contains instructions that match the pattern.
 * The method returns true if the list of instructions was modified, else false.
 */
public class Runner {

    Set<StateMachine> loaded;
    Set<StateMachine> running;

    public Runner() {
        loaded = new LinkedHashSet<>();
        init();
    }

    public void init() {
        running = new LinkedHashSet<>();
    }

    public void loadPatterns(Class<?> ...klasses) {

        for (Class<?> klass : klasses) {
            loadPatterns(klass);
        }
    }

    public void loadPatterns(Class<?> klass) {

        // search methods
        for (Method method : klass.getMethods()) {
            loadPatterns(method);
        }
    }

    public void loadPatterns(Method method) {

        // search patterns
        Pattern[] patterns = method.getAnnotationsByType(Pattern.class);

        if (patterns.length > 0) {

            // get an action and its name
            Action action = loadAction(method);
            String name = getActionName(method);

            // for every pattern add a new state machine
            for (Pattern pattern : patterns) {
                loaded.add(new StateMachine(name, action, pattern.value()));
            }
        }
    }

    public Action loadAction(Method method) {

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle handle = null;
        Action action = null;

        try {

            handle = lookup.unreflect(method);
            action = (Action) LambdaMetafactory.metafactory(
                    lookup,
                    "replace",
                    MethodType.methodType(Action.class),
                    handle.type(),
                    handle,
                    handle.type()
            ).getTarget().invokeExact();

        } catch (Throwable e) {
            throw new Error(e);
        }

        return action;
    }

    public String getActionName(Method method) {
        return    method.getDeclaringClass().getCanonicalName()
                + "."
                + method.getName();
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
        InsnUtils.debug(list);

        // iterate over the list while there is is a change
        boolean change = true;
        while (change) {

            // no change
            change = false;

            // initiate running
            init();

            // iterate over instructions of the list
            AbstractInsnNode node = list.getFirst();
            while (node != null) {

                // get the state node
                AbstractInsnNode next = node.getNext();

                // the flag to restart the search
                boolean restart = false;

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

                // initialize the running machines
                if (restart) {
                    init();
                }

                // process the state node
                node = next;
            }
        }
    }

    public boolean replace(StateMachine fsm, InsnList list) {

        // replace matched instructions in a list
        boolean result = fsm.applyAction(list);

        /*
        if (result) {
            System.err.println(">>> Action: " + fsm);
            System.err.println(">>> Matched:");
            InsnUtils.debug(fsm.getMatchedInput());
            System.err.println(">>> Result:");
            InsnUtils.debug(list);
            System.err.println();
        }
        */

        // return the result
        return result;
    }

}
