package jbyco.optimize;

import java.util.ArrayList;
import java.util.List;

import jbyco.optimize.patterns.State;

public class Configuration<Item> {

    State state;
    List<Item> matched;

    public Configuration(State state) {
        this.state = state;
        this.matched = new ArrayList<>();
    }

    public State getState() {
        return state;
    }

    public List<Item> getMatched() {
        return matched;
    }

    public void doTransition(State state, Item item) {
        this.state = state;
        this.matched.add(item);
    }
}
