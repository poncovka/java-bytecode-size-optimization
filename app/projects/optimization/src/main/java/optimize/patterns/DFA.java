package jbyco.optimize.patterns;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 *  A switch works with the byte, short, char, and int primitive data types. 
 *  It also works with enumerated types (discussed in Enum Types), the String 
 *  class, and a few special classes that wrap certain primitive types: 
 *  Character, Byte, Short, and Integer (discussed in Numbers and Strings).
 */
public class DFA<Symbol> {

	State initialState;
	Set<State> finalStates;
	Map<Symbol, Map<State, State>> rules;
		
	public DFA() {
		this.initialState = new State();
		this.finalStates = new HashSet<>();
		this.rules = new HashMap<>();
	}
	
	public State getInitialState() {
		return initialState;
	}
	
	public Set<State> getFinalStates() {
		return finalStates;
	}
	
	public Map<Symbol, Map<State, State>> getRules() {
		return rules;
	}
	
	public boolean isFinal(State state) {
		return finalStates.contains(state);
	}
	
	public State jump(State state, Symbol symbol) {
	
		// get rules for the current symbol
		Map<State, State> states = this.rules.get(symbol);
		
		// return next state
		if (states != null) {
			return states.get(state);
		}
		
		// or return null
		return null;
	}
	
	public State addRule(State from, Symbol symbol) {
		
		// get rules 
		Map<State, State> states = rules.get(symbol);
				
		// update rules
		if (states == null) {
			states = new HashMap<>();
			rules.put(symbol, states);
		}
		
		// get the next state
		State to = states.get(from);
		
		// update states
		if (to == null) {
			to = new State();
			states.put(from, to);
		}
		
		// return next state
		return to;
	}
	
}
