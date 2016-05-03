package jbyco.optimize;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.lang.reflect.Method;

/**
 * Created by vendy on 2.5.16.
 */
public class State {

    Symbols[] pattern;
    Method action;

    int size;
    int next;
    AbstractInsnNode[] matched;

    public State(Symbols[] pattern, Method action) {

        // set the optimization
        this.pattern = pattern;
        this.action = action;

        // init the state
        this.next = 0;
        this.size = pattern.length;
        this.matched = new AbstractInsnNode[size];
    }

    public State copyInitial() {
        return new State(pattern, action);
    }

    public boolean check(AbstractInsnNode i) {
        return 0 <= next && next < size && pattern[next].match(i);
    }

    public boolean accept(AbstractInsnNode i) {

        // check if the next symbol matches the instruction
        if (check(i)) {

            // store the matched instruction
            matched[next] = i;
            next++;
            return true;
        }

        return false;
    }

    public boolean isFinal() {
        return next >= size;
    }

    public boolean doAction(InsnList list) {
        try {
            Object result = action.invoke(list, (Object[]) matched);
            return ((Boolean) result).booleanValue();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
