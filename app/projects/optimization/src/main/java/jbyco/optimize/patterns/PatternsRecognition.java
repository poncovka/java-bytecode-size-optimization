package jbyco.optimize.patterns;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jbyco.optimize.Configuration;

public class PatternsRecognition<Symbol, Item> {

    DFA<Symbol> dfa;
    Set<Configuration<Item>> configurations;
    Map<State, Pattern<Item>> patterns;

    public PatternsRecognition(DFA<Symbol> dfa, Map<State, Pattern<Item>> patterns) {
        this.dfa = dfa;
        this.patterns = patterns;
        this.configurations = new LinkedHashSet<>();
    }

    public boolean findAndReplace(Symbol symbol, Item item) {

        boolean replaced = false;

        // update all configurations with the current symbol
        updateConfigurations(symbol, item);

        // process all final states
        for (Configuration<Item> config : configurations) {

            State state = config.getState();
            List<Item> matched = config.getMatched();

            // is final?
            if (dfa.isFinal(state)) {

                Pattern<Item> pattern = patterns.get(state);

                // does it match the pattern?
                if (pattern.check(matched)) {

                    // replace the matched items
                    pattern.replace(matched);
                    replaced = true;
                }
            }
        }

        return replaced;
    }

    public void updateConfigurations(Symbol symbol, Item item) {

        // add initial configuration
        configurations.add(new Configuration<>(dfa.getInitialState()));

        // process all configurations
        for (Configuration<Item> config : configurations) {

            // jump from the current state
            State state = dfa.jump(config.getState(), symbol);

            // update the configuration
            if (state != null) {
                config.doTransition(state, item);
            }
            // remove the configuration
            else {
                configurations.remove(config);
            }
        }
    }
}
