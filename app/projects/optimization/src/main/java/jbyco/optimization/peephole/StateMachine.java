package jbyco.optimization.peephole;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.util.Arrays;

/**
 * A representation of the finite state machine.
 * Pattern is matched in the reversed order.
 */
public class StateMachine {

    String name;
    Symbols[] pattern;
    Action action;

    int state;
    AbstractInsnNode[] matched;

    public StateMachine(String name, Action action, Symbols[] pattern) {

        // set the simplifications
        this.name = name;
        this.pattern = pattern;
        this.action = action;

        // init the state
        this.state = pattern.length - 1;
        this.matched = new AbstractInsnNode[pattern.length];
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
        return 0 <= state && state < pattern.length && pattern[state].match(i);
    }

    public boolean readInput(AbstractInsnNode i) {

        // can the machine read the input symbol?
        if (checkInput(i)) {

            // store the matched instruction
            matched[state] = i;
            // go to the next state
            state--;
            // continue to read
            return true;
        }

        // stop to read
        return false;
    }

    public boolean inFinalState() {
        return state < 0;
    }

    public boolean applyAction(InsnList list) {
        return action.replace(list, matched);
    }

    @Override
    public String toString() {
        return getName() + Arrays.toString(pattern);
    }
}
