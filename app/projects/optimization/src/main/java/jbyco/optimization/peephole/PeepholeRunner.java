package jbyco.optimization.peephole;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by vendy on 2.5.16.
 */
public class PeepholeRunner {

    Set<State> initialStates;
    Set<State> states;

    public PeepholeRunner() {
        initialStates = new LinkedHashSet<>();
        init();
    }

    public void init() {
        states = new LinkedHashSet<>();
    }

    public void loadPatterns(Class<?> klass) {

        // search methods
        for (Method method : klass.getMethods()) {
            loadPatterns(method);
        }
    }

    public void loadPatterns(Method method) {

        // search annotations
        Pattern[] patterns = method.getAnnotationsByType(Pattern.class);

        // for every pattern
        for (Pattern pattern : patterns) {

            // add new initial state for the given optimization
            initialStates.add(new State(pattern.value(), method));
        }

    }

    public void run(ClassNode cn) {

        System.err.println(">>> Class:" + cn.name);
        for (Object mn : cn.methods) {

            System.err.println(">>> Method:" + ((MethodNode)mn).name);
            run(((MethodNode)mn).instructions);
        }
    }

    public void run(InsnList list) {

        System.err.println(">>> Instructions:");
        InsnUtils.debug(list);

        // iterate over the list while there is are changes
        boolean change = true;
        while (change) {

            // no change
            change = false;

            // initiate states
            init();

            // iterate over instructions of the list
            AbstractInsnNode node = list.getFirst();
            while (node != null) {

                // get the next node
                AbstractInsnNode next = node.getNext();

                // the flag to restart the search
                boolean restart = false;

                // iterate over initial states
                for (State initial : initialStates) {

                    // add some initial states to states
                    if (initial.check(node)) {
                        states.add(initial.copyInitial());
                    }
                }

                // iterate over states
                Iterator<State> i = states.iterator();
                while (!restart && i.hasNext()) {

                    // get the state
                    State state = i.next();

                    // can the state accept the instruction?
                    if (!state.accept(node)) {
                        i.remove();
                    }
                    // is the state final?
                    else if (state.isFinal()) {

                        // modify the list of instructions
                        if (state.doAction(list)) {
                            restart = true;
                            change = true;
                        }

                        i.remove();
                    }
                }

                // initialize the states
                if (restart) {
                    init();
                }

                // process the next node
                node = next;
            }
        }
    }
}
