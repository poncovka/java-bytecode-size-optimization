package jbyco.optimize;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by vendy on 2.5.16.
 */
public class OptimizationsRunner {

    Set<State> initialStates;
    Set<State> states;

    public OptimizationsRunner(Class<?> klass) {
        initialStates = new LinkedHashSet<>();
        loadPatterns(klass);
        init();
    }

    public void init() {
        states = new LinkedHashSet<>();
    }

    public void loadPatterns(Class<?> klass) {

        // search methods
        for (Method method : klass.getMethods()) {

            // search annotations
            Pattern[] patterns = method.getAnnotationsByType(Pattern.class);

            // for every pattern
            for (Pattern pattern : patterns) {

                // add new initial state for the given optimization
                initialStates.add(new State(pattern.value(), method));
            }
        }
    }

    public void run(InsnList list) {

        // iterate over the list while there is are changes
        boolean change = true;
        while (change) {

            // no change
            change = false;

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
