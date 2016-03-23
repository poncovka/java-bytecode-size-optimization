package jbyco.analyze.patterns.instructions;

import jbyco.analyze.patterns.graph.WildCard;

public class WildInstruction implements WildCard, AbstractInstruction {

	// singleton
	static final WildInstruction instance = new WildInstruction();
		
	// get instance
	public static WildInstruction getInstance() {
		return instance;
	}
		
	private WildInstruction() {
		// nothing
	}
			
	@Override
	public String toString() {
		return "*";
	}
}
