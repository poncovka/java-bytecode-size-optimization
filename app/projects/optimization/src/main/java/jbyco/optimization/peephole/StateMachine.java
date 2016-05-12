package jbyco.optimization.peephole;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

/**
 * Created by vendy on 2.5.16.
 */
public class StateMachine {

    String name;
    Symbols[] pattern;
    Action action;

    int state;
    int finalState;
    AbstractInsnNode[] matched;

    public StateMachine(String name, Action action, Symbols[] pattern) {

        // set the simplifications
        this.name = name;
        this.pattern = pattern;
        this.action = action;

        // init the state
        this.state = 0;
        this.finalState = pattern.length;
        this.matched = new AbstractInsnNode[finalState];
    }

    public String getName() { return name; }

    public Action getAction() {
        return action;
    }

    public AbstractInsnNode[] getMatchedInput() {
        return matched;
    }

    public StateMachine copy() {
        return new StateMachine(name, action, pattern);
    }

    public boolean checkInput(AbstractInsnNode i) {
        return 0 <= state && state < finalState && pattern[state].match(i);
    }

    public boolean readInput(AbstractInsnNode i) {

        // can the machine read the input symbol?
        if (checkInput(i)) {

            // store the matched instruction
            matched[state] = i;
            // go to the next state
            state++;
            // continue to read
            return true;
        }

        // stop to read
        return false;
    }

    public boolean inFinalState() {
        return state >= finalState;
    }

    public boolean applyAction(InsnList list) {
        return action.replace(list, matched);
    }

    @Override
    public String toString() {
        return getName();
    }
}
